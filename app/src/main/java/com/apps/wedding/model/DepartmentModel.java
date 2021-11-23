package com.apps.wedding.model;

import java.io.Serializable;

public class DepartmentModel implements Serializable {
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
