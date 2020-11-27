package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardsResponse {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("data")
    @Expose
    private Data data;

    public CardsResponse() {
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CardsResponse{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", response='" + response + '\'' +
                ", data=" + data +
                '}';
    }

    public class Data{

        @SerializedName("etisalat")
        @Expose
        private List<String> etisalat;

        @SerializedName("orange")
        @Expose
        private List<String> orange;

        @SerializedName("vodafone")
        @Expose
        private List<String> vodafone;

        @SerializedName("we")
        @Expose
        private List<String> we;

        public Data() {
        }

        public List<String> getEtisalat() {
            return etisalat;
        }

        public void setEtisalat(List<String> etisalat) {
            this.etisalat = etisalat;
        }

        public List<String> getOrange() {
            return orange;
        }

        public void setOrange(List<String> orange) {
            this.orange = orange;
        }

        public List<String> getVodafone() {
            return vodafone;
        }

        public void setVodafone(List<String> vodafone) {
            this.vodafone = vodafone;
        }

        public List<String> getWe() {
            return we;
        }

        public void setWe(List<String> we) {
            this.we = we;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "etisalat=" + etisalat +
                    ", orange=" + orange +
                    ", vodafone=" + vodafone +
                    ", we=" + we +
                    '}';
        }
    }
}
