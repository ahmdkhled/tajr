package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateOrdreDataRespnse {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("response")
    @Expose
    private String response;

    public UpdateOrdreDataRespnse() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
