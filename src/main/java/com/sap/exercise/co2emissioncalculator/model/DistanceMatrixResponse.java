package com.sap.exercise.co2emissioncalculator.model;

import java.util.List;

public class DistanceMatrixResponse {
    private List<List<Double>> distances;

    public List<List<Double>> getDistances() {
        return distances;
    }

    public void setDistances(List<List<Double>> distances) {
        this.distances = distances;
    }
}
