package com.tajr.tajr.models;

public class BotBlocksResponse {

    private String code;
    private String info;
    private String response;
    private String data;
    private Block blocks;

    public BotBlocksResponse(String code, String info, String response, String data, Block blocks) {
        this.code = code;
        this.info = info;
        this.response = response;
        this.data = data;
        this.blocks = blocks;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getResponse() {
        return response;
    }

    public String getData() {
        return data;
    }

    public Block getBlocks() {
        return blocks;
    }
}
