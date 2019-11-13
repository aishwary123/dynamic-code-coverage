package com.dynamic.codecoverage.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ServiceObjectCollection {

    private List<ServiceObject> serviceObjectList;

    @JsonCreator
    public ServiceObjectCollection() {
        serviceObjectList = new ArrayList<>();
    }

    public List<ServiceObject> getServiceObjectList() {
        return serviceObjectList;
    }

    public void setServiceObjectList(List<ServiceObject> serviceObjectList) {
        this.serviceObjectList = serviceObjectList;
    }

}
