package com.dynamic.codecoverage.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dynamic.codecoverage.messages.CustomMessages;
import com.dynamic.codecoverage.model.dto.ServiceObject;
import com.dynamic.codecoverage.model.dto.ServiceObjectCollection;
import com.dynamic.codecoverage.model.dto.TestType;
import com.dynamic.codecoverage.service.CoverageConfigService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@SuppressWarnings("rawtypes")
public class CodeCoverageConfigController {

    @Autowired
    private CoverageConfigService coverageConfigService;

    /**
     * Get mapping to fetch the complete service configurations
     * 
     * @return ResponseEntity
     */

    @ApiOperation(value = "Get all the service definitions", response = ServiceObjectCollection.class)
    @GetMapping(value = "/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllServices() {

        final ServiceObjectCollection serviceObjectCollection = coverageConfigService.getServiceObjectCollection();
        if (serviceObjectCollection.getServiceObjectList().isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<ServiceObjectCollection>(
                    serviceObjectCollection, HttpStatus.OK);
    }

    /**
     * Post mapping to update the complete services configurations
     * 
     * @return ResponseEntity
     */

    @ApiOperation(value = "Updates the service collection", response = ServiceObjectCollection.class)
    @PostMapping(value = "/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateServiceCollection(@RequestBody ServiceObjectCollection serviceObjectCollection) {

        coverageConfigService.updateServiceObjectCollection(
                    serviceObjectCollection);
        return new ResponseEntity<ServiceObjectCollection>(
                    serviceObjectCollection, HttpStatus.OK);
    }

    /**
     * Get Mapping to fetch specific Service configuration.<br>
     * Returns Bad request if the object is not found.
     * 
     * @return ResponseEntity
     */

    @ApiOperation(value = "Get service definitions for any specific service", response = ServiceObject.class)
    @GetMapping(value = "/services/{serviceName}/testType/{testType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getService(@PathVariable String serviceName,
                                     @PathVariable TestType testType) {

        final ServiceObject serviceObject = coverageConfigService.getServiceObject(
                    serviceName, testType);
        if (null == serviceObject) {
            return new ResponseEntity<ServiceObject>(serviceObject,
                        HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<ServiceObject>(serviceObject, HttpStatus.OK);

    }

    /**
     * Put apping to add a new Service. It returns NOT MODIFIED status if
     * service already exists.
     * 
     * @Body ServiceObject
     * @return ResponseEntity
     */

    @ApiOperation(value = "Add a new service if not available", response = ServiceObjectCollection.class)
    @PutMapping(value = "/services", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addService(@Valid @RequestBody ServiceObject serviceObject) {

        ServiceObject existingServiceObject = coverageConfigService.getServiceObject(
                    serviceObject.getServiceName(),
                    serviceObject.getTestType());
        /*
         * If the service already exists , NOT MODIFIED is sent back
         */
        if (null != existingServiceObject) {
            return new ResponseEntity<String>(
                        CustomMessages.RECORD_ALREADY_EXISTS,
                        HttpStatus.NOT_MODIFIED);
        }

        ServiceObjectCollection serviceObjectCollection = coverageConfigService.addServiceObject(
                    serviceObject);
        return new ResponseEntity<ServiceObjectCollection>(
                    serviceObjectCollection, HttpStatus.OK);

    }

    /**
     * Put mapping to update a specific service configuration object
     * 
     * @param String serviceName
     * @requestBody ServiceObject
     * @return ResponseEntity
     */
    @ApiOperation(value = "Updates a specific service definition", response = ServiceObjectCollection.class)
    @PutMapping(value = "/services/{serviceName}/testType/{testType}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateService(@PathVariable String serviceName,
                                        @PathVariable TestType testType,
                                        @Valid @RequestBody ServiceObject serviceObject) {

        ServiceObjectCollection serviceObjectCollection = coverageConfigService.updateServiceObject(
                    serviceName, testType, serviceObject);
        if (null == serviceObjectCollection) {
            return new ResponseEntity<ServiceObjectCollection>(
                        serviceObjectCollection, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<ServiceObjectCollection>(
                    serviceObjectCollection, HttpStatus.OK);

    }

    /**
     * Delete mapping to delete a specific service configuration object
     * 
     * @param serviceName
     * @param testType
     * @return
     */
    @ApiOperation(value = "Deletes a specific service definition", response = ServiceObject.class)
    @DeleteMapping(value = "/services/{serviceName}/testType/{testType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteService(@PathVariable String serviceName,
                                        @PathVariable TestType testType) {

        ServiceObject serviceObject = coverageConfigService.deleteServiceObject(
                    serviceName, testType);
        if (null == serviceObject) {
            return new ResponseEntity<ServiceObject>(serviceObject,
                        HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<ServiceObject>(serviceObject, HttpStatus.OK);

    }

}
