package com.dynamic.codecoverage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dynamic.codecoverage.exceptions.DataCollectionException;
import com.dynamic.codecoverage.exceptions.DataReportingException;
import com.dynamic.codecoverage.exceptions.WavefrontClientException;
import com.dynamic.codecoverage.messages.CustomMessages;
import com.dynamic.codecoverage.model.ICodeCoverageState;
import com.dynamic.codecoverage.model.SonarCoverageState;
import com.dynamic.codecoverage.model.dto.ReportType;
import com.dynamic.codecoverage.model.dto.ServiceObject;
import com.dynamic.codecoverage.rest.clients.JenkinsRestClient;
import com.dynamic.codecoverage.wavefront.SonarWavefrontSender;

@Service
public class SonarCodeCoverageService extends CodeCoverageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
                SonarCodeCoverageService.class);

    @Autowired
    JenkinsRestClient jenkinsRestClient;

    private static final String LAST_PREFIX = "api/measures/component?componentKey=";

    private static final String METRICS_KEY = "&metricKeys=line_coverage,branch_coverage,lines_to_cover";

    @Override
    public ICodeCoverageState collectCodeCoverageData(final ServiceObject serviceObject) {
        String jenkinsUrl = serviceObject.getJobName() + LAST_PREFIX
                    + serviceObject.getServiceName() + METRICS_KEY;
        SonarCoverageState sonarCoverageState = jenkinsRestClient.getSonarCoverageData(
                    jenkinsUrl);
        if (null == sonarCoverageState) {
            throw new DataCollectionException(
                        ReportType.SONAR.toString().concat(
                                    CustomMessages.DATA_COLLECTION_FAILED));
        }
        return sonarCoverageState;
    }

    @Override
    public void publishDataToWavefront(final ServiceObject serviceObject,
                                       ICodeCoverageState codeCoverageState) {
        try {
            wavefrontSender = new SonarWavefrontSender(wavefrontProxyUrl,
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
}
