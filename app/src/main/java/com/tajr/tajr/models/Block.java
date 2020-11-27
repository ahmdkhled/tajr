package com.tajr.tajr.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Block {

    @SerializedName("default")
    private ArrayList<BotBlock> Default;
    private ArrayList<BotBlock> normal;

    public Block(ArrayList<BotBlock> aDefault, ArrayList<BotBlock> normal) {
        Default = aDefault;
        this.normal = normal;
    }

    public ArrayList<BotBlock> getDefault() {
        return Default;
    }

    public ArrayList<BotBlock> getNormal() {
        return normal;
    }
}
