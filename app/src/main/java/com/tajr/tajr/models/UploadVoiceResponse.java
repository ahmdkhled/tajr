package com.tajr.tajr.models;

import com.google.gson.annotations.SerializedName;

public class UploadVoiceResponse {

    @SerializedName("code")
    private String code;

    @SerializedName("info")
    private String info;


    @SerializedName("response")
    private String response;


    public UploadVoiceResponse() {
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public class Data {

        @SerializedName("error")
        private String error;

        public Data() {
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    @Override
    public String toString() {
        return "UploadVoiceResponse{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
