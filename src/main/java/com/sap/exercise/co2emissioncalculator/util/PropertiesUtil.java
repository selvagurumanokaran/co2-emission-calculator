package com.sap.exercise.co2emissioncalculator.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public class PropertiesUtil {

    private static Properties emissionProperties = new Properties();

    public static Properties getEmissionValues() {
        if (emissionProperties.isEmpty()) {
            try (InputStream emissionPropertiesPath = PropertiesUtil.class.getClassLoader().getResourceAsStream("co2emission.properties")) {
                emissionProperties.load(emissionPropertiesPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return emissionProperties;
    }

    public static Optional<Integer> getEmissionValue(String transportationMethod) {
        String value = PropertiesUtil.getEmissionValues().getProperty(transportationMethod);
        if (Objects.isNull(value)) return Optional.empty();
        return Optional.of(Integer.parseInt(value));
    }
}
