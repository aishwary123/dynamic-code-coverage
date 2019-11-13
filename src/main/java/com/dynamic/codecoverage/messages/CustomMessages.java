package com.dynamic.codecoverage.messages;

/**
 * All the text which is used within the application is listed here. Log
 * messages are not listed here.
 * 
 * @author aishwaryt
 */
public class CustomMessages {

    public static final String UTILITY_CLASS = "Utility class";
    public static final String ATTEMPTING_RERUN_OF_JOB = "Attempting rerun of job";
    public static final String PARAMETER_PLACEHOLDER = " {}";
    public static final String STRING_FORMAT_PLACEHOLDER = " %s";
    public static final String UNABLE_TO_FIND_SUCCESSFUL_BUILD = "Unable to find build number of last successfull build";
    public static final String RERUN_SUCCESSFUL_AFTER_ATTEMPTS = "Rerun successfull after".concat(
                PARAMETER_PLACEHOLDER).concat("attempts");
    public static final String RERUN_ATTEMPT_FAILED = "Rerun attempt after";
    public static final String RERUN_FAILED_AFTER_ATTEMPTS = "Rerun failed after".concat(
                PARAMETER_PLACEHOLDER).concat(" attempts");
    public static final String FAILED_TO_PROCESS_JSON = "Failed to process JSON ".concat(
                STRING_FORMAT_PLACEHOLDER);
    public static final String SYSTEM_VARIABLE_NOT_FOUND = "System variable not found";
    public static final String CALLING_SONAR_PLUGIN_TO_GET_COVERAGE_REPORT = "Calling Sonar plugin to get the coverage report";
    public static final String SUCCESSFULLY_GOT_THE_RESPONSE_FROM_SONAR_PLUGIN = "Successfully got the response from sonar plugin";
    public static final String POPULATING_RESPONSE_INTO_SONAR_COVERAGE_OBJECT = "Populating response into sonar coverage object";
    public static final String CLASS_CAST_EXCEPTION = "Class cast exception";
    public static final String CONNECTION_TO_WAVEFRONT_SERVER_FAILED = "Connection to wavefront server/agent failed";
    public static final String SENDING_DATA_TO_WAVEFRONT_SERVER_FAILED = "Sending data to wavefront server/agent failed";
    public static final String FLUSHING_DATA_TO_WAVEFRONT_SERVER_FAILED = "Flushing data to wavefront server/agent failed";
    public static final String WAVEFRONT_CLIENT_NOT_INITIALIZED = "Wavefront client not initialized";
    public static final String EXCEPTION_DETAILS = "Exception details";
    public static final String DATA_COLLECTION_FAILED = "Data collection failed";
    public static final String DATA_REPORTING_FAILED = "Data reporting failed";
    public static final String DATA_PROCESSING_FAILED = "Data processing failed";
    public static final String DATA_PROCESSING_SUCCESSFUL = "Data processing successful";
    public static final String SERVICE_INITIALIZATION_FAILED = "Service initialization failed";
    public static final String SCHEDULED_PROCESSING_FAILED = "Scheduled processing failed";
    public static final String INVALID_CONFIG_FILE = "Invalid config file";
    public static final String UPLOADING_FILE_TO_JFROG_ARTIFACTORY = "Uploading file to Jfrog artifactory";
    public static final String DOWNLOADING_FILE_FROM_JFROG_ARTIFACTORY = "Downloading file from Jfrog artifactory";
    public static final String FETCHING_SERVICE_DETAILS_COLLECTION_FAILED = "Fetching service details collection failed";
    public static final String UPDATING_SERVICE_DETAILS_COLLECTION_FAILED = "Updaing service details collection failed";
    public static final String FETCHING_SERVICE_DETAIL_OBJECT_FAILED = "Fetching service detail object failed";
    public static final String DELETING_SERVICE_DETAIL_OBJECT_FAILED = "Deleting service detail object failed";
    public static final String UPDATING_SERVICE_DETAIL_OBJECT_FAILED = "Updating service detail object failed";
    public static final String ADDING_SERVICE_DETAILS_COLLECTION_FAILED = "Adding service details collection failed";
    public static final String MORE_THAN_ONE_OR_NO_SERVICE_OBJECT_FOUND = "More than one or no service object found";
    public static final String NO_SERVICE_OBJECT_FOUND = "No service object found";
    public static final String REST_CLIENT_EXCEPTION = "Rest client exception";
    public static final String SERVICE_OBJECT_ALREADY_EXISTS = "Service object already exists";
    public static final String SERVER_SIDE_ISSUE = "Server side issue";
    public static final String CODE_COVERAGE_SERVICE = "Code coverage service";
    public static final String CONTENT_UPDATED_SUCCESSFULLY_IN_ARTIFACTORY = "Content updated successfully in artifactory";
    public static final String COMPARING_AND_UPDATING_THRESHOLD_VALUE = "Comparing and updating the threshold value";
    public static final String THRESHOLD_VALUE_UPDATED = "Threshold value updated";
    public static final String RECEIVED_REQUEST_FOR_THRESHOLD_UPDATION = "Received the request for threshold updation";
    public static final String RECORD_ALREADY_EXISTS = "Record already exists";
    public static final String GATING_SERVICE_PROCESSING_FAILED = "Gating service processing failed";

    private CustomMessages() {
        throw new IllegalStateException(CustomMessages.UTILITY_CLASS);
    }

}
