package com.dynamic.codecoverage.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dynamic.codecoverage.utils.SystemUtils;

@Configuration
public class ArtifactoryCredsConfig {

    private static final String ARTIFACTORY_USER = "ARTIFACTORY_USER";
    private static final String ARTIFACTORY_PASSWORD = "ARTIFACTORY_PASSWORD";

    @Value("${ARTIFACTORY_USER}")
    private String defaultArtifactoryUser;

    @Value("${ARTIFACTORY_PASSWORD}")
    private String defaultArtifactoryPassword;

    @Bean
    public Properties artifactoryCreds() {

        final String artifactoryUserName = SystemUtils.getSystemVariableOrDefault(
                    ARTIFACTORY_USER, defaultArtifactoryUser);
        final String artifactoryPassword = SystemUtils.getSystemVariableOrDefault(
                    ARTIFACTORY_PASSWORD, defaultArtifactoryPassword);
        Properties artifactoryCreds = new Properties();
        artifactoryCreds.setProperty(ARTIFACTORY_USER, artifactoryUserName);
        artifactoryCreds.setProperty(ARTIFACTORY_PASSWORD, artifactoryPassword);
        return artifactoryCreds;
    }

}
