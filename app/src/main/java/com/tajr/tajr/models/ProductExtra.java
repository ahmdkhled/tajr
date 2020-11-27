package com.tajr.tajr.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductExtra {

    private String name;
    private String html;
    private String type;
    private String placeholder;
    private String is_list;
    private ArrayList<String> list;
    private String required;
    private String value;


    public ProductExtra(String html) {
        this.html = html;
    }

    public String getName() {
        return name;
    }

    public String getHtml() {
        return html;
    }

    public String getType() {
        return type;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String Is_list() {
        return is_list;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public String getRequired() {
        return required;
    }

    public String getValue() {
        return value;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if ( !(obj instanceof ProductExtra) ) return false;
        ProductExtra productExtra= (ProductExtra) obj;
        return html.equals(productExtra.html);
    }

    @NonNull
    @Override
    public String toString() {
        return name+" >>> "+value;
    }
}
