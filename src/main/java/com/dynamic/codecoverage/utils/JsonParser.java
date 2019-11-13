package com.dynamic.codecoverage.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.dynamic.codecoverage.messages.CustomMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JsonParser utility class do JSON related operation.All JSON related
 * operations which are commonly used are listed here.
 * 
 * @author aishwaryt
 */
public class JsonParser {

    private JsonParser() {
        throw new IllegalStateException(CustomMessages.UTILITY_CLASS);
    }

    public static ObjectMapper getObjectMapperObject() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

    /**
     * It converts the data provided as JSON string input to Java POJO.
     * 
     * @param jsonData Input data
     * @param clazz target class type
     * @return
     * @throws IOException
     */
    public static <T> T convertJsonStringToJavaObject(final String jsonData,
                                                      final Class<T> clazz)
        throws IOException {
        ObjectMapper objectMapper = getObjectMapperObject();
        return objectMapper.readValue(jsonData, clazz);
    }

    /**
     * It converts the data provided as Java POJO to JSON string.
     * 
     * @param object
     * @return Stringified JSON Object
     * @throws JsonProcessingException
     */
    public static String convertJavaObjectToJsonString(Object object)
        throws JsonProcessingException {
        ObjectMapper objectMapper = getObjectMapperObject();
        return objectMapper.writeValueAsString(object);
    }

    /**
     * It converts the data provided as JSON string input at some location to
     * Java POJO.
     * 
     * @param jsonData Input data
     * @param clazz target class type
     * @return
     * @throws IOException
     */
    public static <T> T convertFileContentToJavaObject(final String fileLocation,
                                                       final Class<T> clazz)
        throws IOException {
        ObjectMapper objectMapper = getObjectMapperObject();
        return objectMapper.readValue(new File(fileLocation), clazz);
    }

    /**
     * It saves the data provided as Java POJO into file at the specified
     * location.
     * 
     * @param object
     * @param fileLocation
     * @throws IOException
     */
    public static <T> void convertJavaObjectToFileContent(final Object object,
                                                          final String fileLocation)
        throws IOException {
        ObjectMapper objectMapper = getObjectMapperObject();
        objectMapper.writeValue(new File(fileLocation), object);
    }

    /**
     * It converts the data present at the specified URL location to Java POJO.
     * 
     * @param url
     * @param clazz
     * @return
     * @throws IOException
     */
    public static <T> T convertURLContentToJavaObject(final URL url,
                                                      final Class<T> clazz)
        throws IOException {
        ObjectMapper objectMapper = getObjectMapperObject();
        return objectMapper.readValue(url, clazz);
    }

}
