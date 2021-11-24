package com.apps.wedding.model;

import java.io.Serializable;

public class FilterRangeModel implements Serializable {
    private float fromValue=0.0f;
    private float toValue=100000.0f;
    private String rate="1";

    public FilterRangeModel(float fromValue, float toValue) {
        this.fromValue = fromValue;
        this.toValue = toValue;
    }

    public float getFromValue() {
        return fromValue;
    }

    public void setFromValue(float fromValue) {
        this.fromValue = fromValue;
    }

    public float getToValue() {
        return toValue;
    }

    public void setToValue(float toValue) {
        this.toValue = toValue;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
