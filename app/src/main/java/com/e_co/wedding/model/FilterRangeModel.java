package com.e_co.wedding.model;

import java.io.Serializable;

public class FilterRangeModel implements Serializable {
    private float fromValue;
    private float toValue;
    private float stepValue;

    private float selectedFromValue;
    private float selectedToValue;

    public FilterRangeModel(float fromValue, float toValue, float stepValue) {
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.stepValue = stepValue;
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

    public float getStepValue() {
        return stepValue;
    }

    public void setStepValue(float stepValue) {
        this.stepValue = stepValue;
    }

    public float getSelectedFromValue() {
        return selectedFromValue;
    }

    public void setSelectedFromValue(float selectedFromValue) {
        this.selectedFromValue = selectedFromValue;
    }

    public float getSelectedToValue() {
        return selectedToValue;
    }

    public void setSelectedToValue(float selectedToValue) {
        this.selectedToValue = selectedToValue;
    }
}
