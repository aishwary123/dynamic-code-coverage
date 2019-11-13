package com.dynamic.codecoverage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dynamic.codecoverage.messages.CustomMessages;
import com.dynamic.codecoverage.model.dto.ReportType;
import com.dynamic.codecoverage.model.dto.ServiceObjectCollection;

@Service
public class SchedulerService {

    @Autowired
    private JacocoCodeCoverageService jacocoCodeCoverageService;

    @Autowired
    private SonarCodeCoverageService sonarCodeCoverageService;

    @Autowired
    private JacocoGatingService jacocoGatingService;

    @Autowired
    private CoverageConfigService coverageConfigService;

    private static final Logger LOGGER = LoggerFactory.getLogger(
                SchedulerService.class);

    @Scheduled(fixedRate = 1800 * 1000)
    public void scheduleDataCollection() {

        try {
            final ServiceObjectCollection serviceObjectCollection = coverageConfigService.getServiceObjectCollection();
            if (null != serviceObjectCollection) {
                serviceObjectCollection.getServiceObjectList().parallelStream().forEach(
                            serviceObject -> {
                                // Collecting the data and reporting it to
                                // wavefront
                                if (serviceObject.getType() == ReportType.JACOCO) {
                                    jacocoCodeCoverageService.process(
                                                serviceObject);
                                    jacocoGatingService.process(serviceObject);
                                } else if (serviceObject.getType() == ReportType.SONAR) {
                                    sonarCodeCoverageService.process(
                                                serviceObject);
                                }

                            });
            }
        } catch (Exception exception) {
            LOGGER.error(
                        CustomMessages.EXCEPTION_DETAILS.concat(
                                    CustomMessages.PARAMETER_PLACEHOLDER),
                        exception.getMessage());
            LOGGER.error(CustomMessages.SCHEDULED_PROCESSING_FAILED);

        }

    }

}
