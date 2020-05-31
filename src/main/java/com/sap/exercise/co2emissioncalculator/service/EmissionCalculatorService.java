package com.sap.exercise.co2emissioncalculator.service;

import com.sap.exercise.co2emissioncalculator.repository.OpenRouteServiceRepository;
import com.sap.exercise.co2emissioncalculator.util.EmissionCalculatorUtil;

import java.util.*;

public class EmissionCalculatorService {


    private OpenRouteServiceRepository repository;

    public EmissionCalculatorService() {
        repository = new OpenRouteServiceRepository();
    }

    public double calculateCo2Emission(String start, String end, String transportationMethod) throws IllegalArgumentException {
        Optional<Integer> emissionValueOptional = EmissionCalculatorUtil.getEmissionValue(transportationMethod);
        if (!emissionValueOptional.isPresent())
            throw new IllegalArgumentException("Invalid transportation method.");
        List<Double> startCoordinates = repository.getCoordinates(start);
        if (startCoordinates.isEmpty())
            throw new IllegalArgumentException("Invalid start city.");
        List<Double> endCoordinates = repository.getCoordinates(end);
        if (endCoordinates.isEmpty())
            throw new IllegalArgumentException("Invalid end city.");
        Optional<Double> distanceOptional = repository.getDistance(startCoordinates, endCoordinates);
        if (!distanceOptional.isPresent())
            throw new IllegalArgumentException("Something went wrong. Could not determine the distance and co2 emission between given cities.");
        Double distance = distanceOptional.get();
        if (distance == 0.0) throw new IllegalArgumentException("Cities are not connected.");
        double emissionValueInKg = (distance * emissionValueOptional.get()) / 1000;
        return Math.round(emissionValueInKg * 10.0) / 10.0;
    }
}
