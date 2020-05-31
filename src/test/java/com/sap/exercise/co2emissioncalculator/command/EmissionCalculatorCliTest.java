package com.sap.exercise.co2emissioncalculator.command;

import com.sap.exercise.co2emissioncalculator.service.EmissionCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmissionCalculatorCliTest {

    @Mock
    private EmissionCalculatorService service;

    @InjectMocks
    private EmissionCalculatorCli subject;

    private ArgumentCaptor<String> argumentCaptor;

    @BeforeEach
    public void setup() {
        argumentCaptor = ArgumentCaptor.forClass(String.class);
    }

    @Test
    public void testCalculateCommand() {
        String startCity = "Berlin";
        String endCity = "Hamburg";
        String transportationMethod = "train";
        subject.run(new String[]{"--start", startCity, "--end", endCity, "--transportation-method", transportationMethod});
        verify(service).calculateCo2Emission(argumentCaptor.capture(), argumentCaptor.capture(), argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().size(), 3);
        assertEquals(argumentCaptor.getAllValues().get(0), startCity);
        assertEquals(argumentCaptor.getAllValues().get(1), endCity);
        assertEquals(argumentCaptor.getAllValues().get(2), transportationMethod);
    }

    @Test
    public void testArgumentsWithEqualSymbol() {
        String startCity = "Los Angeles";
        String endCity = "New York";
        String transportationMethod = "large-electric-car";
        subject.run(new String[]{"--end", endCity, "--transportation-method", transportationMethod, "--start", startCity});
        verify(service).calculateCo2Emission(argumentCaptor.capture(), argumentCaptor.capture(), argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().size(), 3);
        assertEquals(argumentCaptor.getAllValues().get(0), startCity);
        assertEquals(argumentCaptor.getAllValues().get(1), endCity);
        assertEquals(argumentCaptor.getAllValues().get(2), transportationMethod);
    }

}
