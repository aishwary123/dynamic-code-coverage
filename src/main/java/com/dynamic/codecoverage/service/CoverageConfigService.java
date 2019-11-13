package com.dynamic.codecoverage.service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dynamic.codecoverage.exceptions.RequestProcessingException;
import com.dynamic.codecoverage.messages.CustomMessages;
import com.dynamic.codecoverage.model.dto.ServiceObject;
import com.dynamic.codecoverage.model.dto.ServiceObjectCollection;
import com.dynamic.codecoverage.model.dto.TestType;
import com.dynamic.codecoverage.rest.clients.JFrogArtifactoryRestClient;
import com.dynamic.codecoverage.utils.JsonParser;

@Component
public class CoverageConfigService {

    @Autowired
    private JFrogArtifactoryRestClient jFrogArtifactoryRestClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(
                CoverageConfigService.class);

    /**
     * It will return {@link ServiceObjectCollection}
     * 
     * @return
     * @throws RequestProcessingException
     */
    public ServiceObjectCollection getServiceObjectCollection() {
        try {
            final URL codeCoverageServiceDefinitionURL = jFrogArtifactoryRestClient.getCodeCoverageServiceDefinitionURL();

            return JsonParser.convertURLContentToJavaObject(
                        codeCoverageServiceDefinitionURL,
                        ServiceObjectCollection.class);

        } catch (Exception exception) {
            LOGGER.error(
                        CustomMessages.FETCHING_SERVICE_DETAILS_COLLECTION_FAILED);
            throw new RequestProcessingException(exception.getMessage());
        }
    }

    /**
     * It will replace the old {@link ServiceObjectCollection} with the one that
     * is provided as parameter.
     * 
     * @param serviceObjectCollection
     * @return
     * @throws RequestProcessingException
     */
    public ServiceObjectCollection updateServiceObjectCollection(final ServiceObjectCollection serviceObjectCollection) {
        try {
            updateContentsInArtifactory(serviceObjectCollection);
            return serviceObjectCollection;

        } catch (Exception exception) {
            LOGGER.error(
                        CustomMessages.UPDATING_SERVICE_DETAILS_COLLECTION_FAILED);
            throw new RequestProcessingException(exception.getMessage());
        }
    }

    /**
     * It will return the {@link ServiceObject} with the name
     * {@code serviceName} and {@code testType} provided as parameter.<br>
     * It will return {@code null} if no object found or more than one objects
     * are found.
     * 
     * @param serviceName
     * @param testType
     * @return
     * @throws RequestProcessingException
     */
    public ServiceObject getServiceObject(final String serviceName,
                                          final TestType testType) {
        try {
            final ServiceObjectCollection serviceObjectCollection = getServiceObjectCollection();
            List<ServiceObject> filteredServiceObjectList = filterServiceObjects(
                        serviceObjectCollection, serviceName, testType);
            if (null == filteredServiceObjectList
                        || filteredServiceObjectList.isEmpty()
                        || filteredServiceObjectList.size() > 1) {
                LOGGER.error(
                            CustomMessages.MORE_THAN_ONE_OR_NO_SERVICE_OBJECT_FOUND);
                return null;
            }
            return filteredServiceObjectList.get(0);
        } catch (Exception exception) {
            LOGGER.error(CustomMessages.FETCHING_SERVICE_DETAIL_OBJECT_FAILED);
            throw new RequestProcessingException(exception.getMessage());
        }
    }

    /**
     * It will add the {@link ServiceObject} provided as parameter in the
     * {@link ServiceObjectCollection}.
     * 
     * @param serviceObject
     * @return
     * @throws RequestProcessingException
     */
    public ServiceObjectCollection addServiceObject(final ServiceObject serviceObject) {
        try {
            final ServiceObjectCollection serviceObjectCollection = getServiceObjectCollection();
            serviceObjectCollection.getServiceObjectList().add(serviceObject);
            updateContentsInArtifactory(serviceObjectCollection);
            return serviceObjectCollection;
        } catch (Exception exception) {
            LOGGER.error(
                        CustomMessages.ADDING_SERVICE_DETAILS_COLLECTION_FAILED);
            throw new RequestProcessingException(exception.getMessage());
        }

    }

    /**
     * It will replace the {@link ServiceObject} in the
     * {@link ServiceObjectCollection} with {@code ServiceObject} provided as
     * parameter.<br>
     * It will return the updated {@code ServiceObject} if found else it will
     * return {@code null}.
     * 
     * @param serviceName
     * @param testType
     * @param serviceObject
     * @return
     * @throws RequestProcessingException
     */
    public ServiceObjectCollection updateServiceObject(final String serviceName,
                                                       final TestType testType,
                                                       final ServiceObject serviceObject) {

        try {

            ServiceObjectCollection serviceObjectCollection = getServiceObjectCollection();
            ServiceObject existingServiceObject = getServiceObject(serviceName,
                        testType);
            if (null != existingServiceObject) {
                serviceObjectCollection.getServiceObjectList().remove(
                            existingServiceObject);
                serviceObjectCollection.getServiceObjectList().add(
                            serviceObject);
                updateContentsInArtifactory(serviceObjectCollection);
                return serviceObjectCollection;
            }

        } catch (Exception exception) {

            LOGGER.error(
                        CustomMessages.UPDATING_SERVICE_DETAILS_COLLECTION_FAILED);
            throw new RequestProcessingException(exception.getMessage());
        }
        return null;

    }

    /**
     * It will delete the {@link ServiceObject} from the
     * {@link ServiceObjectCollection}.<br>
     * It will return {@code ServiceObject} which was requested for deletion if
     * the object is found else it will return {@code null} if the object is not
     * found.
     * 
     * @param serviceName
     * @param testType
     * @return
     * @throws RequestProcessingException
     */
    public ServiceObject deleteServiceObject(final String serviceName,
                                             final TestType testType) {

        try {
            final ServiceObjectCollection serviceObjectCollection = getServiceObjectCollection();
            ServiceObject serviceObject = getServiceObject(serviceName,
                        testType);
            if (null != serviceObject) {
                serviceObjectCollection.getServiceObjectList().remove(
                            serviceObject);
                updateContentsInArtifactory(serviceObjectCollection);
                return serviceObject;
            }
        } catch (Exception exception) {
            LOGGER.error(CustomMessages.DELETING_SERVICE_DETAIL_OBJECT_FAILED);
            throw new RequestProcessingException(exception.getMessage());
        }
        return null;
    }

    private List<ServiceObject> filterServiceObjects(final ServiceObjectCollection serviceObjectCollection,
                                                     final String serviceName,
                                                     final TestType testType) {

        return serviceObjectCollection.getServiceObjectList().parallelStream().filter(
                    serviceObject -> {
                        boolean elementFound = false;
                        if (null != serviceObject.getServiceName()
                                    && null != serviceObject.getTestType()
                                    && serviceObject.getServiceName().equalsIgnoreCase(
                                                serviceName)
                                    && serviceObject.getTestType() == testType) {
                            elementFound = true;

                        }
                        return elementFound;
                    }).collect(Collectors.toList());
    }

    private synchronized void updateContentsInArtifactory(final ServiceObjectCollection serviceObjectCollection)
        throws IOException {
        JsonParser.convertJavaObjectToFileContent(serviceObjectCollection,
                    "config.json");
        jFrogArtifactoryRestClient.uploadCodeCoverageServiceDefinitionFile(
                    Paths.get("config.json"));
        LOGGER.info(CustomMessages.CONTENT_UPDATED_SUCCESSFULLY_IN_ARTIFACTORY);
    }
}
