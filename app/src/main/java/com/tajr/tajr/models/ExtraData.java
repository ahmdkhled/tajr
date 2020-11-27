package com.tajr.tajr.models;

import androidx.annotation.NonNull;

public class ExtraData {

    private String name;
    private String type;
    private String request_name;
    private String details;
    private String required;


    public ExtraData(String name, String type, String request_name, String details, String required) {
        this.name = name;
        this.type = type;
        this.request_name = request_name;
        this.details = details;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRequest_name() {
        return request_name;
    }

    public String getDetails() {
        return details;
    }

    public String getRequired() {
        return required;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "name "+name+
                "type "+type+
                "request_name "+request_name+
                "details "+details
                ;
    }
}
