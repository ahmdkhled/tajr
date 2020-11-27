package com.tajr.tajr.models;

public class CurrentOrderData {

    public static CurrentOrderData currentOrderData;
    private String callTime = "NotYet";
    private CurrentOrderResponse currentOrderResponse;
    private SingleOrderProductsResponse singleOrderProductsResponse;
    private SingleOrderProductsResponse singleMissedOrderProductsResponse;
    private CurrentOrderResponse missedCallOrderResponse;
    public static CurrentOrderData getInstance(){
        if (currentOrderData!=null){
            return currentOrderData;
        }else {
            currentOrderData = new CurrentOrderData();
            return currentOrderData;
        }
    }

    public SingleOrderProductsResponse getSingleMissedOrderProductsResponse() {
        return singleMissedOrderProductsResponse;
    }

    public void setSingleMissedOrderProductsResponse(SingleOrderProductsResponse singleMissedOrderProductsResponse) {
        this.singleMissedOrderProductsResponse = singleMissedOrderProductsResponse;
    }

    public CurrentOrderResponse getMissedCallOrderResponse() {
        return missedCallOrderResponse;
    }

    public void setMissedCallOrderResponse(CurrentOrderResponse missedCallOrderResponse) {
        this.missedCallOrderResponse = missedCallOrderResponse;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public CurrentOrderResponse getCurrentOrderResponse() {
        return currentOrderResponse;
    }

    public void setCurrentOrderResponse(CurrentOrderResponse currentOrderResponse) {
        this.currentOrderResponse = currentOrderResponse;
    }

    public SingleOrderProductsResponse getSingleOrderProductsResponse() {
        return singleOrderProductsResponse;
    }

    public void setSingleOrderProductsResponse(SingleOrderProductsResponse singleOrderProductsResponse) {
        this.singleOrderProductsResponse = singleOrderProductsResponse;
    }
}
