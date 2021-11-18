package com.apps.wedding.model;

import java.io.Serializable;
import java.util.List;

public class OrderDataModel extends StatusResponse implements Serializable {
    private List<com.apps.wedding.model.OrderModel> data;

    public List<com.apps.wedding.model.OrderModel> getData() {
        return data;
    }
}
