package com.apps.wedding.model;

import java.io.Serializable;

public class FilterRateModel implements Serializable {
    private String title;
    private boolean selected;

    public FilterRateModel(String title) {
        this.title = title;
        this.selected = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
