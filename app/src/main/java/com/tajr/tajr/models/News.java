package com.tajr.tajr.models;

public class News {
    private String image;
    private String name;
    private String message;

    public News() {
    }

    public News(String image, String name, String message) {
        this.image = image;
        this.name = name;
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
