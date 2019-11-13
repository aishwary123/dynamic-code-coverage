package com.dynamic.codecoverage.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dynamic.codecoverage.messages.CustomMessages;
import com.dynamic.codecoverage.model.dto.ServiceObject;
import com.dynamic.codecoverage.model.dto.ServiceObjectCollection;
import com.dynamic.codecoverage.model.dto.TestType;
import com.dynamic.codecoverage.service.CoverageConfigService;
import com.dynamic.codecoverage.service.JacocoGatingService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/gating")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CodeCoverageGatingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(
                CodeCoverageGatingController.class);

    @Autowired
    private JacocoGatingService jacocoGatingService;

    @Autowired
    private CoverageConfigService coverageConfigService;

    /**
     * Get Mapping to update the service threshold.<br>
     * It will read the code coverage information from the Jenkins job for build
     * number {@code buildNumber}<br>
     * It the threshold is higher than the current threshold, it will update the
     * threshold in the service configuration. <br>
     * It is an async operation.
     * 
     * @return ResponseEntity
     * @throws IOException
     * @throws NumberFormatException
     */

    @ApiOperation(value = "Update the threshold for service object", response = ServiceObjectCollection.class)
    @GetMapping(value = "/services/{serviceName}/testType/{testType}/build/{buildNumber}")
    @Async
    public ResponseEntity updateServiceThreshold(@PathVariable String serviceName,
                                                 @PathVariable TestType testType,
                                                 @PathVariable String buildNumber)
        throws IOException {

        LOGGER.info(CustomMessages.RECEIVED_REQUEST_FOR_THRESHOLD_UPDATION);
        ServiceObject serviceObject = coverageConfigService.getServiceObject(
                    serviceName, testType);
        // Updating the threshold values
        // If threshold value is updated, we will update the service
        // configuration
        if (jacocoGatingService.updateGatingThresholdForBuild(serviceObject,
                    Integer.parseInt(buildNumber))) {
            // Updating the last reported build number to current build number
            serviceObject.setLastReportedBuildNumber(
                        Integer.parseInt(buildNumber));
            coverageConfigService.updateServiceObject(serviceName, testType,
                        serviceObject);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

}
