package com.sap.exercise.co2emissioncalculator.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public class EmissionCalculatorUtil {

    private static Properties emissionProperties = new Properties();
    private static Properties openRouteServiceProperties = new Properties();

    public static Properties getEmissionValues() {
        if (emissionProperties.isEmpty()) {
            try (InputStream emissionPropertiesPath = EmissionCalculatorUtil.class.getClassLoader().getResourceAsStream("co2emission.properties")) {
                emissionProperties.load(emissionPropertiesPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return emissionProperties;
    }

    public static Optional<Integer> getEmissionValue(String transportationMethod) {
        String value = EmissionCalculatorUtil.getEmissionValues().getProperty(transportationMethod);
        if (Objects.isNull(value)) return Optional.empty();
        return Optional.of(Integer.parseInt(value));
    }

    public static Properties getOpenRouteServiceProperties() {
        if (openRouteServiceProperties.isEmpty()) {
            try (InputStream openrouterervicePropertiesPath = EmissionCalculatorUtil.class.getClassLoader().getResourceAsStream("openrouterervice.properties")) {
                openRouteServiceProperties.load(openrouterervicePropertiesPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return openRouteServiceProperties;
    }

    public static String getOpenRouteServiceProperty(String key) {
        return EmissionCalculatorUtil.getOpenRouteServiceProperties().getProperty(key);
    }

}
