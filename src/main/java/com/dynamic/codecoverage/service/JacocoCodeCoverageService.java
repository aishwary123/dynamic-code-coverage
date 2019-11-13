package com.dynamic.codecoverage.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.dynamic.codecoverage.exceptions.DataCollectionException;
import com.dynamic.codecoverage.exceptions.DataReportingException;
import com.dynamic.codecoverage.exceptions.WavefrontClientException;
import com.dynamic.codecoverage.messages.CustomMessages;
import com.dynamic.codecoverage.model.ICodeCoverageState;
import com.dynamic.codecoverage.model.JacocoCoverageDetails;
import com.dynamic.codecoverage.model.JacocoCoverageState;
import com.dynamic.codecoverage.model.dto.ServiceObject;
import com.dynamic.codecoverage.rest.clients.JenkinsRestClient;
import com.dynamic.codecoverage.wavefront.JacocoWavefrontSender;

@Service
public class JacocoCodeCoverageService extends CodeCoverageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
                JacocoCodeCoverageService.class);

    @Autowired
    JenkinsRestClient jenkinsRestClient;

    @Value("${JENKINS_JOB_MAX_LOOKUP}")
    private int maxLookupAttempts;

    @Value("${JENKINS_JOB_LOOKUP_ON_FAIL_ENABLED}")
    private boolean jobLookupOnFailEnabled;

    @Value("${JENKINS_JOB_USE_BUILDID_FROM_CONF}")
    private boolean useBuildIdFromConf;

    @Override
    public ICodeCoverageState collectCodeCoverageData(final ServiceObject serviceObject) {
        try {
            final JacocoCoverageDetails jacocoCoverageDetails = getJacocoCoverageDetails(
                        serviceObject);
            return jacocoCoverageDetails.getJacocoCoverageState();
        } catch (Exception exception) {
            LOGGER.error(
                        CustomMessages.EXCEPTION_DETAILS.concat(
                                    CustomMessages.PARAMETER_PLACEHOLDER),
                        exception.getMessage());
            throw new DataCollectionException(exception.getMessage());
        }
    }

    @Override
    public void publishDataToWavefront(final ServiceObject serviceObject,
                                       ICodeCoverageState codeCoverageState) {
        try {
            wavefrontSender = new JacocoWavefrontSender(wavefrontProxyUrl,
                        wavefrontProxyPort);

            wavefrontSender.sendDataToWavefront(reporterPrefix, serviceObject,
                        codeCoverageState);
        } catch (WavefrontClientException wavefrontClientException) {
            LOGGER.error(
                        CustomMessages.EXCEPTION_DETAILS.concat(
                                    CustomMessages.PARAMETER_PLACEHOLDER),
                        wavefrontClientException.getMessage());
            LOGGER.error(CustomMessages.DATA_REPORTING_FAILED.concat(
                        "for service").concat(
                                    CustomMessages.PARAMETER_PLACEHOLDER),
                        serviceObject.getJobName());

            throw new DataReportingException(
                        wavefrontClientException.getMessage());
        }
    }

    /**
     * This method will get the Java Code Coverage score for service whose
     * definition is passed in {@code ServiceObject} parameter.<br>
     * It will check for lastSuccessful build and will get the code coverage
     * details. It uses {@code JacocoCoverageState} to keep the coverage
     * information.<br>
     * If we retrigger a build, code coverage is not generated in that case. So,
     * the build might have succeeded but we won't be able to get the coverage
     * information. <br>
     * So,we are looking for some previous build that succeeded with coverage
     * report. <br>
     * We have defined two methods for fallback lookup if the user wants. <br>
     * 1. Looking for last few builds from the last successful build<br>
     * 2. Using the configuration file to get the build number which was last
     * reported
     * 
     * @param serviceObject
     * @return {@link JacocoCoverageDetails}
     * @throws IOException
     * @throws DataCollectionException if any exception occurs during data fetch
     *             operation
     */
    public JacocoCoverageDetails getJacocoCoverageDetails(final ServiceObject serviceObject)
        throws IOException {
        try {
            JacocoCoverageState jacocoCoverageState = jenkinsRestClient.getJacocoCodeCoverageData(
                        serviceObject.getJobName());
            if (null != jacocoCoverageState) {
                JacocoCoverageDetails jacocoCoverageDetails = new JacocoCoverageDetails();
                jacocoCoverageDetails.setJacocoCoverageState(
                            jacocoCoverageState);
                jacocoCoverageDetails.setBuildNumber(
                            jenkinsRestClient.getLastSuccessfulBuildNumber(
                                        serviceObject.getJobName()));
                return jacocoCoverageDetails;
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            if (jobLookupOnFailEnabled
                        && httpClientErrorException.getStatusCode() == HttpStatus.NOT_FOUND) {
                LOGGER.info(CustomMessages.ATTEMPTING_RERUN_OF_JOB.concat(
                            CustomMessages.PARAMETER_PLACEHOLDER),
                            serviceObject.getJobName());
                return checkPreviousJobs(serviceObject);
            }
            LOGGER.error(
                        CustomMessages.EXCEPTION_DETAILS.concat(
                                    CustomMessages.PARAMETER_PLACEHOLDER),
                        httpClientErrorException.getMessage());
            throw new DataCollectionException(
                        httpClientErrorException.getMessage());

        }
        return null;
    }

    private JacocoCoverageDetails checkPreviousJobs(final ServiceObject serviceObject)
        throws IOException {

        int lastSuccessfulBuildNumber = jenkinsRestClient.getLastSuccessfulBuildNumber(
                    serviceObject.getJobName());

        int lookupPosition = 1;
        int buildIdFromConf = serviceObject.getLastReportedBuildNumber();
        int buildNumber = lastSuccessfulBuildNumber - lookupPosition;
        JacocoCoverageState jacocoCoverageState = null;
        do {

            try {
                jacocoCoverageState = jenkinsRestClient.getJacocoCodeCoverageDataForBuild(
                            serviceObject.getJobName(), buildNumber);
                if (null != jacocoCoverageState) {
                    JacocoCoverageDetails jacocoCoverageDetails = new JacocoCoverageDetails();
                    jacocoCoverageDetails.setJacocoCoverageState(
                                jacocoCoverageState);
                    jacocoCoverageDetails.setBuildNumber(buildNumber);
                    return jacocoCoverageDetails;
                }
            } catch (HttpClientErrorException httpClientErrorException) {
                if (httpClientErrorException.getStatusCode() != HttpStatus.NOT_FOUND) {
                    LOGGER.error(CustomMessages.RERUN_ATTEMPT_FAILED);
                    LOGGER.error(CustomMessages.EXCEPTION_DETAILS.concat(
                                CustomMessages.PARAMETER_PLACEHOLDER),
                                httpClientErrorException.getMessage());
                    throw httpClientErrorException;
                }
            }
            lookupPosition += 1;
            buildNumber -= 1;

        } while ((!useBuildIdFromConf && lookupPosition <= maxLookupAttempts)
                    || (useBuildIdFromConf && buildNumber > buildIdFromConf));
        LOGGER.info(CustomMessages.RERUN_FAILED_AFTER_ATTEMPTS,
                    lookupPosition - 1);
        return null;
    }
}
