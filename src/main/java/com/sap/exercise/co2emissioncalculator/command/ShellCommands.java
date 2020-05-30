package com.sap.exercise.co2emissioncalculator.command;

import com.sap.exercise.co2emissioncalculator.service.EmissionCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ShellCommands {

    @Autowired
    private EmissionCalculatorService emissionCalculatorService;

    @ShellMethod(value = " returns the amount of CO2-equivalent that will be caused when traveling between two cities using a given transportation method."
            , key = "co2-calculator")
    public String calculateCo2Emission(String start, String end, @ShellOption("--transportation-method") String transportationMethod) {
        String separator = "=";
        String[] startParams = start.split(separator);
        if (startParams.length > 1) {
            if (!"--start".equals(startParams[0])) return "Invalid argument " + startParams[0];
            else start = startParams[1];
        }
        String[] endParams = end.split(separator);
        if (endParams.length > 1) {
            if (!"--end".equals(endParams[0])) return "Invalid argument " + endParams[0];
            else end = endParams[1];
        }
        String[] transportParams = transportationMethod.split(separator);
        if (transportParams.length > 1) {
            if (!"--transportation-method".equals(transportParams[0])) return "Invalid argument " + transportParams[0];
            else transportationMethod = transportParams[1];
        }
        return emissionCalculatorService.calculateCo2Emission(start, end, transportationMethod);
    }
}
