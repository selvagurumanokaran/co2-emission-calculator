package com.sap.exercise.co2emissioncalculator.command;

import com.sap.exercise.co2emissioncalculator.service.EmissionCalculatorService;
import org.apache.commons.cli.*;

public class EmissionCalculatorCli {

    private EmissionCalculatorService emissionCalculatorService;

    public EmissionCalculatorCli() {
        emissionCalculatorService = new EmissionCalculatorService();
    }

    public void run(String[] args) {
        Options options = new Options();
        try {
            Option startOption = new Option("start", "start", true, "Name of origin city.");
            startOption.setRequired(true);
            options.addOption(startOption);
            Option endOption = new Option("end", "end", true, "Name of destination city.");
            endOption.setRequired(true);
            options.addOption(endOption);
            Option transportOption = new Option("t", "transportation-method", true, "The mode of transportation.");
            transportOption.setRequired(true);
            options.addOption(transportOption);
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(options, args);
            String start = String.join(" ", line.getOptionValues("start"));
            String end = String.join(" ", line.getOptionValues("end"));
            String transport = line.getOptionValues("transportation-method")[0];
            System.out.println(emissionCalculatorService.calculateCo2Emission(start, end, transport));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            new HelpFormatter().printHelp("./co2-calculator", options);
        }
    }
}
