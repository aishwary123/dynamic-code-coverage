package com.dynamic.codecoverage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.dynamic.codecoverage.messages.CustomMessages;
import com.dynamic.codecoverage.model.ICodeCoverageState;
import com.dynamic.codecoverage.model.dto.ServiceObject;
import com.dynamic.codecoverage.wavefront.WavefrontSender;

/**
 * This class is creating a contract to perform two tasks. <br>
 * 1. Collect the code coverage details from the job whose information is passed
 * as {@code ServiceObject}<br>
 * 2. Report the coverage info as custom metrics to Wavefront.
 * 
 * @author aishwaryt
 */
public abstract class CodeCoverageService {

    @Value("${wavefront.proxy.url}")
    String wavefrontProxyUrl;

    @Value("${wavefront.proxy.port}")
    int wavefrontProxyPort;

    @Value("${reporter.prefix}")
    String reporterPrefix;

    WavefrontSender wavefrontSender;

    private static final Logger LOGGER = LoggerFactory.getLogger(
                CodeCoverageService.class);

    abstract ICodeCoverageState collectCodeCoverageData(final ServiceObject serviceObject);

    abstract void publishDataToWavefront(final ServiceObject serviceObject,
                                         ICodeCoverageState codeCoverageState);

    public void process(final ServiceObject serviceObject) {
        try {
            ICodeCoverageState codeCoverageState = collectCodeCoverageData(
                        serviceObject);
            publishDataToWavefront(serviceObject, codeCoverageState);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(CustomMessages.DATA_PROCESSING_SUCCESSFUL.concat(
                            CustomMessages.PARAMETER_PLACEHOLDER),
                            serviceObject.toString());
            }
        } catch (Exception exception) {
            LOGGER.error(
                        CustomMessages.EXCEPTION_DETAILS.concat(
                                    CustomMessages.PARAMETER_PLACEHOLDER),
                        exception.getMessage());
            LOGGER.error(
                        CustomMessages.DATA_PROCESSING_FAILED.concat(
                                    CustomMessages.PARAMETER_PLACEHOLDER),
                        serviceObject.toString());
        }
    }
}
