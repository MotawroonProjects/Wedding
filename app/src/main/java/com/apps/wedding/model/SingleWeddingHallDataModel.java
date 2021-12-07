package com.apps.wedding.model;

import java.io.Serializable;
import java.util.List;

public class SingleWeddingHallDataModel extends StatusResponse implements Serializable {
    private WeddingHallModel data;

    public WeddingHallModel getData() {
        return data;
    }

}
