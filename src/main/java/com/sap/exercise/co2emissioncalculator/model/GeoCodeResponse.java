package com.sap.exercise.co2emissioncalculator.model;

import java.util.List;

public class GeoCodeResponse {

    private List<Feature> features;

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
