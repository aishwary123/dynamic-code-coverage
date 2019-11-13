package com.dynamic.codecoverage.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.dynamic.codecoverage.messages.CustomMessages;
import com.dynamic.codecoverage.model.JacocoCoverageState;
import com.dynamic.codecoverage.model.dto.CodeCoverageGatingMetrics;
import com.dynamic.codecoverage.model.dto.ServiceObject;
import com.dynamic.codecoverage.model.dto.Thresholds;
import com.dynamic.codecoverage.model.metrics.CodeCoverageMetrics;
import com.dynamic.codecoverage.rest.clients.JenkinsRestClient;

@Component
public class JacocoGatingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
                JacocoGatingService.class);
    @Autowired
    private JenkinsRestClient jenkinsRestClient;

    @Autowired
    private CoverageConfigService coverageConfigService;

    public void process(final ServiceObject serviceObject) {
        try {
            if (serviceObject.isDynamicGatingEnabled()
                        && updateGatingThreshold(serviceObject)) {
                coverageConfigService.updateServiceObject(
                            serviceObject.getServiceName(),
                            serviceObject.getTestType(), serviceObject);
            }
        } catch (Exception exception) {
            LOGGER.error(
                        CustomMessages.EXCEPTION_DETAILS.concat(
                                    CustomMessages.PARAMETER_PLACEHOLDER),
                        exception.getMessage());
            LOGGER.error(
                        CustomMessages.GATING_SERVICE_PROCESSING_FAILED.concat(
                                    CustomMessages.PARAMETER_PLACEHOLDER),
                        serviceObject.toString());
        }
    }

    /**
     * It fetches the {@link JacocoCoverageState} for all the build from the
     * last build to the last reported build maintained in
     * {@link ServiceObject}.<br>
     * If any build is having threshold greater than the threshold which is
     * maintained in {@code ServiceObject}, the values in {@code ServiceObject}
     * will be updated.
     * 
     * @param serviceObject
     * @return true/false It tells whether the gating threshold of service
     *         object is updated or not.
     */
    public boolean updateGatingThreshold(final ServiceObject serviceObject) {
        int lastBuildNumber = jenkinsRestClient.getLastBuildNumber(
                    serviceObject.getJobName());
        int lastReportedBuildNumber = serviceObject.getLastReportedBuildNumber();
        while (lastBuildNumber > lastReportedBuildNumber) {
            try {
                if (updateGatingThresholdForBuild(serviceObject,
                            lastBuildNumber)) {
                    serviceObject.setLastReportedBuildNumber(lastBuildNumber);
                    return true;
                }

            } catch (HttpClientErrorException httpClientErrorException) {
                if (httpClientErrorException.getStatusCode() == HttpStatus.NOT_FOUND) {
                    LOGGER.info(CustomMessages.ATTEMPTING_RERUN_OF_JOB.concat(
                                CustomMessages.PARAMETER_PLACEHOLDER).concat(
                                            " for Build Number ").concat(
                                                        CustomMessages.PARAMETER_PLACEHOLDER),
                                serviceObject.getJobName(),
                                lastBuildNumber - 1);
                    lastBuildNumber -= 1;
                    continue;
                }
                throw httpClientErrorException;
            } catch (Exception exception) {
                LOGGER.error(
                            CustomMessages.EXCEPTION_DETAILS.concat(
                                        CustomMessages.PARAMETER_PLACEHOLDER),
                            exception.getMessage());
            }
            return false;
        }
        return false;
    }

    /**
     * It retrieves the {@link JacocoCoverageState} for any specific Jenkins
     * build.<br>
     * It then calls the {@code compareAndUpdateThreshold} to update the
     * threshold values of {@link ServiceObject}
     * 
     * @param serviceObject
     * @param buildNumber
     * @return
     * @throws IOException
     * @throws {@link HttpClientErrorException} It is thrown by rest client.
     */
    public boolean updateGatingThresholdForBuild(final ServiceObject serviceObject,
                                                 final int buildNumber)
        throws IOException {

        JacocoCoverageState jacocoCoverageState = jenkinsRestClient.getJacocoCodeCoverageDataForBuild(
                    serviceObject.getJobName(), buildNumber);
        if (null != jacocoCoverageState) {
            return compareAndUpdateThreshold(serviceObject,
                        jacocoCoverageState);
        }
        return false;
    }

    /**
     * It will compare the threshold values present in the {@link ServiceObject}
     * to the {@link JacocoCoverageState}.<br>
     * {@code ServiceObject} represents the data as per the configuration
     * maintained for the service whereas {@code JacocoCoverageState} represents
     * the Java code coverage value for a specific build.
     * 
     * @param serviceObject
     * @param jacocoCoverageState
     * @return true/false It tells whether the threshold is updated or not.
     */
    private boolean compareAndUpdateThreshold(final ServiceObject serviceObject,
                                              final JacocoCoverageState jacocoCoverageState) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(CustomMessages.COMPARING_AND_UPDATING_THRESHOLD_VALUE.concat(
                        serviceObject.toString()));
        }
        boolean isThresholdValueModified = false;
        if (null != serviceObject.getThresholds()
                    && null != jacocoCoverageState) {
            Thresholds thresholds = serviceObject.getThresholds();
            if (null != thresholds.getLineCoverageGatingMetrics()
                        && null != jacocoCoverageState.getLineCoverage()) {
                isThresholdValueModified |= updateThreshold(
                            jacocoCoverageState.getLineCoverage(),
                            thresholds.getLineCoverageGatingMetrics());
            }
            if (null != thresholds.getBranchCoverageGatingMetrics()
                        && null != jacocoCoverageState.getBranchCoverage()) {
                isThresholdValueModified |= updateThreshold(
                            jacocoCoverageState.getBranchCoverage(),
                            thresholds.getBranchCoverageGatingMetrics());
            }
            if (null != thresholds.getMethodCoverageGatingMetrics()
                        && null != jacocoCoverageState.getMethodCoverage()) {
                isThresholdValueModified |= updateThreshold(
                            jacocoCoverageState.getMethodCoverage(),
                            thresholds.getMethodCoverageGatingMetrics());
            }
            if (null != thresholds.getClassCoverageGatingMetrics()
                        && null != jacocoCoverageState.getClassCoverage()) {
                isThresholdValueModified |= updateThreshold(
                            jacocoCoverageState.getClassCoverage(),
                            thresholds.getClassCoverageGatingMetrics());
            }
            if (null != thresholds.getInstructionCoverageGatingMetrics()
                        && null != jacocoCoverageState.getInstructionCoverage()) {
                isThresholdValueModified |= updateThreshold(
                            jacocoCoverageState.getInstructionCoverage(),
                            thresholds.getInstructionCoverageGatingMetrics());
            }
            if (null != thresholds.getComplexityCoverageGatingMetrics()
                        && null != jacocoCoverageState.getComplexityScore()) {
                isThresholdValueModified |= updateThreshold(
                            jacocoCoverageState.getComplexityScore(),
                            thresholds.getComplexityCoverageGatingMetrics());
            }
        }
        if (isThresholdValueModified && LOGGER.isInfoEnabled())

        {
            LOGGER.info(CustomMessages.THRESHOLD_VALUE_UPDATED.concat(
                        serviceObject.toString()));
        }
        return isThresholdValueModified;
    }

    private boolean updateThreshold(final CodeCoverageMetrics codeCoverageMetrics,
                                    final CodeCoverageGatingMetrics codeCoverageGatingMetrics) {
        int percentage = codeCoverageMetrics.getPercentage();
        if (percentage > codeCoverageGatingMetrics.getMaximumCoveragePercentageDynamic()) {
            codeCoverageGatingMetrics.setMaximumCoveragePercentageDynamic(
                        percentage);
            codeCoverageGatingMetrics.setMinimumCoveragePercentageDynamic(
                        percentage);
            return true;
        }
        return false;
    }

}
