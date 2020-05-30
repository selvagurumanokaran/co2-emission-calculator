package com.sap.exercise.co2emissioncalculator.service;

import com.sap.exercise.co2emissioncalculator.repository.OpenRouteServiceRepository;
import com.sap.exercise.co2emissioncalculator.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmissionCalculatorService {

    @Autowired
    private OpenRouteServiceRepository repository;

    public String calculateCo2Emission(String start, String end, String transportationMethod) {
        Optional<Integer> emissionValueOptional = PropertiesUtil.getEmissionValue(transportationMethod);
        if (!emissionValueOptional.isPresent()) return "Invalid transportation method.";
        List<Double> startCoordinates = repository.getCoordinates(start);
        if (startCoordinates.isEmpty()) return "Invalid start city.";
        List<Double> endCoordinates = repository.getCoordinates(end);
        if (endCoordinates.isEmpty()) return "Invalid end city.";
        Optional<Double> distanceOptional = repository.getDistance(startCoordinates, endCoordinates);
        return distanceOptional.map(aDouble -> (aDouble * emissionValueOptional.get()) + "g")
                .orElse("Something went wrong. Could not determine the distance and co2 emission between given cities.");
    }
}
