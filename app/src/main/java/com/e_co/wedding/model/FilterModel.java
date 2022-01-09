package com.e_co.wedding.model;

import java.io.Serializable;

public class FilterModel implements Serializable {
    private String category_id;
    private String fromRange;
    private String toRange;
    private String rate;

    public FilterModel(String category_id, String fromRange, String toRange, String rate) {
        this.category_id = category_id;
        this.fromRange = fromRange;
        this.toRange = toRange;
        this.rate = rate;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getFromRange() {
        return fromRange;
    }

    public void setFromRange(String fromRange) {
        this.fromRange = fromRange;
    }

    public String getToRange() {
        return toRange;
    }

    public void setToRange(String toRange) {
        this.toRange = toRange;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
