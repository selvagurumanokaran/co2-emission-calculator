package com.sap.exercise.co2emissioncalculator.service;

import com.sap.exercise.co2emissioncalculator.repository.OpenRouteServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmissionCalculatorServiceTest {

    @Mock
    private OpenRouteServiceRepository repository;

    @InjectMocks
    private EmissionCalculatorService subject;

    private static final String START = "Berlin";
    private static final String END = "Hamburg";
    private static final String TRANSPORTATION_METHOD = "train";

    @Test
    public void testCalculatingEmission() {
        List<Double> startCoordinates = Arrays.asList(10.213313, 2.323232);
        when(repository.getCoordinates(START)).thenReturn(startCoordinates);
        List<Double> endCoordinates = Arrays.asList(-20.223, 61.31212);
        when(repository.getCoordinates(END)).thenReturn(endCoordinates);
        when(repository.getDistance(startCoordinates, endCoordinates)).thenReturn(Optional.of(124.0));
        String output = subject.calculateCo2Emission(START, END, TRANSPORTATION_METHOD);
        assertEquals(output, "Your trip caused 0.7kg of CO2-equivalent.");
    }

    @Test
    public void testInvalidTransportation() {
        String output = subject.calculateCo2Emission(START, END, "auto");
        assertEquals(output, "Invalid transportation method.");
    }

    @Test
    public void testInvalidStartCity() {
        when(repository.getCoordinates(START)).thenReturn(Collections.emptyList());
        String output = subject.calculateCo2Emission(START, END, TRANSPORTATION_METHOD);
        assertEquals(output, "Invalid start city.");
    }

    @Test
    public void testInvalidEndCity() {
        List<Double> startCoordinates = Arrays.asList(10.213313, 2.323232);
        when(repository.getCoordinates(START)).thenReturn(startCoordinates);
        when(repository.getCoordinates(END)).thenReturn(Collections.emptyList());
        String output = subject.calculateCo2Emission(START, END, TRANSPORTATION_METHOD);
        assertEquals(output, "Invalid end city.");
    }

    @Test
    public void testErrorCalculatingDistance() {
        List<Double> startCoordinates = Arrays.asList(10.213313, 2.323232);
        when(repository.getCoordinates(START)).thenReturn(startCoordinates);
        List<Double> endCoordinates = Arrays.asList(-20.223, 61.31212);
        when(repository.getCoordinates(END)).thenReturn(endCoordinates);
        when(repository.getDistance(startCoordinates, endCoordinates)).thenThrow(new NullPointerException());
        assertThrows(NullPointerException.class, () -> subject.calculateCo2Emission(START, END, TRANSPORTATION_METHOD));
    }

    @Test
    public void testNoDistanceCalculated() {
        List<Double> startCoordinates = Arrays.asList(10.213313, 2.323232);
        when(repository.getCoordinates(START)).thenReturn(startCoordinates);
        List<Double> endCoordinates = Arrays.asList(-20.223, 61.31212);
        when(repository.getCoordinates(END)).thenReturn(endCoordinates);
        when(repository.getDistance(startCoordinates, endCoordinates)).thenReturn(Optional.empty());
        String output = subject.calculateCo2Emission(START, END, TRANSPORTATION_METHOD);
        assertEquals(output, "Something went wrong. Could not determine the distance and co2 emission between given cities.");
    }

    @Test
    public void testCityNotConnected() {
        List<Double> startCoordinates = Arrays.asList(10.213313, 2.323232);
        when(repository.getCoordinates(START)).thenReturn(startCoordinates);
        List<Double> endCoordinates = Arrays.asList(-20.223, 61.31212);
        when(repository.getCoordinates(END)).thenReturn(endCoordinates);
        when(repository.getDistance(startCoordinates, endCoordinates)).thenReturn(Optional.of(0.0));
        String output = subject.calculateCo2Emission(START, END, TRANSPORTATION_METHOD);
        assertEquals("Cities are not connected.", output);
    }
}
