package com.e_co.wedding.model;

import java.io.Serializable;
import java.util.List;

public class RequestServiceModel implements Serializable {
    private WeddingHallModel weddingHallModel;
    private List<String> ids;

    public WeddingHallModel getWeddingHallModel() {
        return weddingHallModel;
    }

    public void setWeddingHallModel(WeddingHallModel weddingHallModel) {
        this.weddingHallModel = weddingHallModel;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
