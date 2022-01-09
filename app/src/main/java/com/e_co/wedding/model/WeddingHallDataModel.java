package com.e_co.wedding.model;

import java.io.Serializable;
import java.util.List;

public class WeddingHallDataModel extends StatusResponse implements Serializable {
    private List<WeddingHallModel> data;

    public List<WeddingHallModel> getData() {
        return data;
    }

}
