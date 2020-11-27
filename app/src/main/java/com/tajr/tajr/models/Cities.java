package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cities {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("cities_count")
    @Expose
    private int shipping_cost;

    @SerializedName("cities")
    @Expose
    private List<City> cities;


    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public int getShipping_cost() {
        return shipping_cost;
    }

    public List<City> getCities() {
        return cities;
    }

    public Cities() {
    }

    @Override
    public String toString() {
        return "Cities{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", shipping_cost=" + shipping_cost +
                ", cities=" + cities +
                '}';
    }

    public class City{

        @SerializedName("city_id")
        @Expose
        private String city_id;

        @SerializedName("city_name")
        @Expose
        private String city_name;

        @SerializedName("shipping_cost")
        @Expose
        private String shipping_cost;

        public City() {
        }

        public String getCity_id() {
            return city_id;
        }

        public String getCity_name() {
            return city_name;
        }

        public String getShipping_cost() {
            return shipping_cost;
        }

        @Override
        public String toString() {
            return "City{" +
                    "city_id='" + city_id + '\'' +
                    ", city_name='" + city_name + '\'' +
                    ", shipping_cost='" + shipping_cost + '\'' +
                    '}';
        }
    }
}
