package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notes {

    @SerializedName("notes")
    @Expose
    private String notes;

    public Notes() {
    }

    public String getNotes() {
        return notes;
    }
}
