package com.tajr.tajr.models;

public class Pages {

    private String current;
    private String of;

    public String getCurrent() {
        return current;
    }

    public String getOf() {
        return of;
    }

    public boolean isLastPage(){
        return current.equals(of);
    }

    public boolean exceedLimit(int page){
        return page>Integer.valueOf(of);
    }
}
