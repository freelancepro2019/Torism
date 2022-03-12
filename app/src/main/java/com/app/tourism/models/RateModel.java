package com.app.tourism.models;

import java.io.Serializable;

public class RateModel implements Serializable {
    private String rate_id;
    private String guide_id;
    private String order_id;
    private int rate;

    public RateModel() {
    }

    public RateModel(String rate_id, String guide_id, String order_id, int rate) {
        this.rate_id = rate_id;
        this.guide_id = guide_id;
        this.order_id = order_id;
        this.rate = rate;
    }

    public String getRate_id() {
        return rate_id;
    }

    public void setRate_id(String rate_id) {
        this.rate_id = rate_id;
    }

    public String getGuide_id() {
        return guide_id;
    }

    public void setGuide_id(String guide_id) {
        this.guide_id = guide_id;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
