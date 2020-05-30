package com.sap.exercise.co2emissioncalculator;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Map;

@SpringBootApplication
public class Co2EmissionCalculatorApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Co2EmissionCalculatorApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

}
