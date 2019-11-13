package com.dynamic.codecoverage.rest.clients;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dynamic.codecoverage.messages.CustomMessages;
import com.dynamic.codecoverage.model.JacocoCoverageState;
import com.dynamic.codecoverage.model.SonarCoverageState;
import com.dynamic.codecoverage.utils.JsonParser;

@Component
public class JenkinsRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(
                JenkinsRestClient.class);

    private static final String METRIC_KEY = "metric";
    private static final String METRIC_VALUE = "value";
    private static final String JACOCO_URL = "/jacoco/api/json";
    private static final String JENKINS_LAST_SUCCESSFUL_BUILD_PREFIX = "/lastSuccessfulBuild";
    private static final String JENKINS_LAST_BUILD_PREFIX = "/lastBuild";
    private static final String JENKINS_LAST_SUCCESSFUL_BUILD_NUMBER = JENKINS_LAST_SUCCESSFUL_BUILD_PREFIX
                + "/buildNumber";
    private static final String JENKINS_LAST_BUILD_NUMBER = JENKINS_LAST_BUILD_PREFIX
                + "/buildNumber";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * It will call the Jenkins job for code coverage information and will
     * return {@link JacocoCoverageState} after populating all the information
     * in it.<br>
     * It uses the "/lastSuccessfulBuild" tag in the url which is resolved by
     * Jenkins.
     * 
     * @param jenkinsJobUrl
     * @return
     * @throws IOException It might be thrown when JSON parsing fails
     * @throws {@link HttpClientErrorException} It might be thrown be when
     *             resource is not found
     */
    @Retryable(value = { IOException.class }, maxAttempts = 3)
    public JacocoCoverageState getJacocoCodeCoverageData(final String jenkinsJobUrl)
        throws IOException {

        String getCodeCoverageUrl = jenkinsJobUrl
                    + JENKINS_LAST_SUCCESSFUL_BUILD_PREFIX + JACOCO_URL;

        ResponseEntity<String> response = restTemplate.getForEntity(
                    getCodeCoverageUrl, String.class);
        if (response.getStatusCodeValue() == HttpStatus.OK.value()) {
            String jsonResponse = response.getBody();
            return JsonParser.convertJsonStringToJavaObject(jsonResponse,
                        JacocoCoverageState.class);
        }
        return null;
    }

    /**
     * It will call the Jenkins job for code coverage information and will
     * return {@link JacocoCoverageState} after populating all the information
     * in it.<br>
     * It uses the {@code buildNumber} tag in the url which is provided as
     * parameter.
     * 
     * @param jenkinsJobUrl
     * @param buildNumber
     * @return
     * @throws IOException It might be thrown when JSON parsing fails
     * @throws {@link HttpClientErrorException} It might be thrown be when
     *             resource is not found
     */
    @Retryable(value = { IOException.class }, maxAttempts = 3)
    public JacocoCoverageState getJacocoCodeCoverageDataForBuild(final String jenkinsJobUrl,
                                                                 final int buildNumber)
        throws IOException {

        final String getCodeCoverageUrl = jenkinsJobUrl + "/" + buildNumber
                    + JACOCO_URL;

        ResponseEntity<String> response = restTemplate.getForEntity(
                    getCodeCoverageUrl, String.class);
        if (response.getStatusCodeValue() == HttpStatus.OK.value()) {
            String jsonResponse = response.getBody();
            return JsonParser.convertJsonStringToJavaObject(jsonResponse,
                        JacocoCoverageState.class);
        }
        return null;
    }

    /**
     * It will return the last successful build number of a Jenkins job.
     * 
     * @param jenkinsJobUrl
     * @return
     */
    public int getLastSuccessfulBuildNumber(final String jenkinsJobUrl) {
        String lastBuildUrl = jenkinsJobUrl
                    + JENKINS_LAST_SUCCESSFUL_BUILD_NUMBER;
        ResponseEntity<String> response = restTemplate.getForEntity(
                    lastBuildUrl, String.class);
        String jsonResponse = response.getBody();
        return Integer.parseInt(jsonResponse);
    }

    /**
     * It will return the last build number of a Jenkins job.
     * 
     * @param jenkinsJobUrl
     * @return
     */
    public int getLastBuildNumber(final String jenkinsJobUrl) {
        String lastBuildUrl = jenkinsJobUrl + JENKINS_LAST_BUILD_NUMBER;
        ResponseEntity<String> response = restTemplate.getForEntity(
                    lastBuildUrl, String.class);
        String jsonResponse = response.getBody();
        return Integer.parseInt(jsonResponse);
    }

    /**
     * It will call the Sonar plugin to get the coverage information.
     * 
     * @param jenkinsUrl
     * @return
     */
    public SonarCoverageState getSonarCoverageData(final String jenkinsUrl) {

        LOGGER.info(CustomMessages.CALLING_SONAR_PLUGIN_TO_GET_COVERAGE_REPORT);
        ResponseEntity<String> response = restTemplate.getForEntity(jenkinsUrl,
                    String.class);
        SonarCoverageState sonarCoverageState = null;
        if (response.getStatusCodeValue() == HttpStatus.OK.value()) {
            LOGGER.info(CustomMessages.SUCCESSFULLY_GOT_THE_RESPONSE_FROM_SONAR_PLUGIN);
            sonarCoverageState = new SonarCoverageState();
            String jsonResponse = response.getBody();
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONObject component = jsonObj.getJSONObject("component");
            JSONArray measures = component.getJSONArray("measures");
            if (measures != null) {
                populateSonarCoverageState(sonarCoverageState, measures);
            }
        }
        return sonarCoverageState;
    }

    private SonarCoverageState populateSonarCoverageState(final SonarCoverageState sonarCoverageState,
                                                          final JSONArray measures) {
        LOGGER.info(CustomMessages.POPULATING_RESPONSE_INTO_SONAR_COVERAGE_OBJECT);
        for (int i = 0; i < measures.length(); i++) {
            JSONObject metric = measures.getJSONObject(i);
            if (metric.getString(METRIC_KEY).equalsIgnoreCase(
                        "line_coverage")) {
                sonarCoverageState.setLineCoverage(
                            metric.getFloat(METRIC_VALUE));
            }
            if (metric.getString(METRIC_KEY).equalsIgnoreCase(
                        "branch_coverage")) {
                sonarCoverageState.setBranchCoverage(
                            metric.getFloat(METRIC_VALUE));
            }
            if (metric.getString(METRIC_KEY).equalsIgnoreCase(
                        "lines_to_cover")) {
                sonarCoverageState.setLinesToCover(metric.getInt(METRIC_VALUE));
            }
        }
        return sonarCoverageState;
    }

}
