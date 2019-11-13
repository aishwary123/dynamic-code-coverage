package com.dynamic.codecoverage.rest.clients;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.dynamic.codecoverage.exceptions.RestClientException;
import com.dynamic.codecoverage.messages.CustomMessages;
import com.dynamic.codecoverage.utils.SystemUtils;

/**
 * It is a helper class that can be used to upload and download artifacts from
 * JFrog artifactory.
 * 
 * @author aishwaryt
 */

@Component
@SuppressWarnings("rawtypes")
public class JFrogArtifactoryRestClient {

    @Value("${JFROG_ARTIFACTORY_API_BASE_URL}")
    private String defaultJFrogArtifactoryAPIBaseURL;

    @Value("${CODE_COVERAGE_SERVICE_DEFINITION_FILE_LOCATION}")
    private String defaultCodeCoverageServiceDefinitionFileLocation;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Properties artifactoryCreds;

    private static final String JFROG_ARTIFACTORY_API_BASE_URL = "JFROG_ARTIFACTORY_API_BASE_URL";
    private static final String CODE_COVERAGE_SERVICE_DEFINITION_FILE_LOCATION = "CODE_COVERAGE_SERVICE_DEFINITION_FILE_LOCATION";

    private String jFrogArtifactoryAPIBaseURL;
    private String codeCoverageServiceDefinitionFileLocation;

    private static final Logger LOGGER = LoggerFactory.getLogger(
                JFrogArtifactoryRestClient.class);

    @PostConstruct
    public void init() {
        this.jFrogArtifactoryAPIBaseURL = SystemUtils.getSystemVariableOrDefault(
                    JFROG_ARTIFACTORY_API_BASE_URL,
                    defaultJFrogArtifactoryAPIBaseURL);
        this.codeCoverageServiceDefinitionFileLocation = SystemUtils.getSystemVariableOrDefault(
                    CODE_COVERAGE_SERVICE_DEFINITION_FILE_LOCATION,
                    defaultCodeCoverageServiceDefinitionFileLocation);
    }

    /**
     * It is used to upload a file to JFrog artifactory.
     * 
     * @param filePath
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @Retryable(value = { Exception.class }, maxAttempts = 3)
    public ResponseEntity uploadCodeCoverageServiceDefinitionFile(final Path filePath) {

        try {
            final URL jFrogArtifactoryEndpointToUploadFile = getCodeCoverageServiceDefinitionURL();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(CustomMessages.UPLOADING_FILE_TO_JFROG_ARTIFACTORY.concat(
                            CustomMessages.PARAMETER_PLACEHOLDER),
                            jFrogArtifactoryEndpointToUploadFile);
            }
            HttpHeaders headers = new HttpHeaders();
            addBasicAuthenticationHeader(headers);
            HttpEntity<byte[]> requestEntity = new HttpEntity<>(
                        IOUtils.toByteArray(
                                    new FileInputStream(filePath.toFile())),
                        headers);

            return restTemplate.exchange(
                        jFrogArtifactoryEndpointToUploadFile.toURI(),
                        HttpMethod.PUT, requestEntity, String.class);
        } catch (IOException | URISyntaxException exception) {
            LOGGER.error(CustomMessages.REST_CLIENT_EXCEPTION);
            throw new RestClientException(exception.getMessage());
        }
    }

    private void addBasicAuthenticationHeader(final HttpHeaders httpHeaders) {
        final String artifactoryUser = artifactoryCreds.getProperty(
                    "ARTIFACTORY_USER");
        final String artifactoryPassword = artifactoryCreds.getProperty(
                    "ARTIFACTORY_PASSWORD");
        if (StringUtils.isEmpty(artifactoryUser)
                    || StringUtils.isEmpty(artifactoryPassword)) {
            return;
        }
        httpHeaders.setBasicAuth(artifactoryUser, artifactoryPassword);
    }

    public URL getCodeCoverageServiceDefinitionURL()
        throws MalformedURLException {
        return new URL(jFrogArtifactoryAPIBaseURL.concat(
                    codeCoverageServiceDefinitionFileLocation));
    }
}