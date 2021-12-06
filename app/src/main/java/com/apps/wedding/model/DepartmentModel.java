package com.apps.wedding.model;

import java.io.Serializable;

public class DepartmentModel implements Serializable {
    private String id;
    private String icon;
    private String title;
    private String is_shown;
    private String created_at;
    private String updated_at;
    private boolean isSelected;

    public String getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getIs_shown() {
        return is_shown;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
