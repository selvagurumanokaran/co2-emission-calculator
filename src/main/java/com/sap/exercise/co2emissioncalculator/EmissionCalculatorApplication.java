package com.sap.exercise.co2emissioncalculator;

import com.sap.exercise.co2emissioncalculator.command.EmissionCalculatorCli;

public class EmissionCalculatorApplication {
    public static void main(String[] args) {
        EmissionCalculatorCli cli = new EmissionCalculatorCli();
        cli.run(args);
    }
}
