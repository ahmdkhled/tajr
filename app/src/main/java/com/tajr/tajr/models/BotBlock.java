package com.tajr.tajr.models;

public class BotBlock {

    private String id;
    private String token;
    private String name;


    public BotBlock(String id, String token, String name) {
        this.id = id;
        this.token = token;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }
}
