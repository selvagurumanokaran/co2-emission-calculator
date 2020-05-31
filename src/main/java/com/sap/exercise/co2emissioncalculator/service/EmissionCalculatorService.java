package com.sap.exercise.co2emissioncalculator.service;

import com.sap.exercise.co2emissioncalculator.repository.OpenRouteServiceRepository;
import com.sap.exercise.co2emissioncalculator.util.EmissionCalculatorUtil;

import java.util.*;

public class EmissionCalculatorService {


    private OpenRouteServiceRepository repository;

    public EmissionCalculatorService() {
        repository = new OpenRouteServiceRepository();
    }

    public String calculateCo2Emission(String start, String end, String transportationMethod) {
        Optional<Integer> emissionValueOptional = EmissionCalculatorUtil.getEmissionValue(transportationMethod);
        if (!emissionValueOptional.isPresent()) return "Invalid transportation method.";
        List<Double> startCoordinates = repository.getCoordinates(start);
        if (startCoordinates.isEmpty()) return "Invalid start city.";
        List<Double> endCoordinates = repository.getCoordinates(end);
        if (endCoordinates.isEmpty()) return "Invalid end city.";
        Optional<Double> distanceOptional = repository.getDistance(startCoordinates, endCoordinates);
        return distanceOptional.map(distance -> {
            if (distance == 0.0) return "Cities are not connected.";
            double emissionValue = (distance * emissionValueOptional.get()) / 1000;
            return "Your trip caused " + (Math.round(emissionValue * 10.0) / 10.0) + "kg of CO2-equivalent.";
        }).orElse("Something went wrong. Could not determine the distance and co2 emission between given cities.");
    }
}
