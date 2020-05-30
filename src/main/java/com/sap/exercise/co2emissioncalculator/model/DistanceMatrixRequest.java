package com.sap.exercise.co2emissioncalculator.model;

import java.util.ArrayList;
import java.util.List;

public class DistanceMatrixRequest {
    private List<List<Double>> locations;
    private List<String> metrics;
    private String units;

    public DistanceMatrixRequest(List<Double> startCoordinates, List<Double> endCoordinates, List<String> metrics, String units) {
        locations = new ArrayList<>();
        this.locations.add(startCoordinates);
        this.locations.add(endCoordinates);
        this.metrics = metrics;
        this.units = units;
    }

    public List<List<Double>> getLocations() {
        return locations;
    }

    public void setLocations(List<List<Double>> locations) {
        this.locations = locations;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
