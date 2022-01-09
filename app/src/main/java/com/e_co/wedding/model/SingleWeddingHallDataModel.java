package com.e_co.wedding.model;

import java.io.Serializable;

public class SingleWeddingHallDataModel extends StatusResponse implements Serializable {
    private WeddingHallModel data;

    public WeddingHallModel getData() {
        return data;
    }

}
