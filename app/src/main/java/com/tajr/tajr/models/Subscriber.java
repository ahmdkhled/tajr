package com.tajr.tajr.models;

public class Subscriber {

    private String id;
    private String name;
    private String img;
    private String type;
    private String img_base;
    private String page;
    private String psid;

    public Subscriber(String id, String name, String img, String page, String psid) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.page = page;
        this.psid = psid;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public String getType() {
        return type;
    }

    public String getImg_base() {
        return img_base;
    }

    public String getPage() {
        return page;
    }

    public String getPsid() {
        return psid;
    }
}
