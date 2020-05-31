package com.sap.exercise.co2emissioncalculator.repository;

import com.sap.exercise.co2emissioncalculator.model.*;
import com.sap.exercise.co2emissioncalculator.util.EmissionCalculatorUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class OpenRouteServiceRepository {

    private static String ORS_TOKEN = System.getenv("ORS_TOKEN");
    private RestTemplate restTemplate;
    private String geoCodeUrl;
    private String matrixUrl;

    public OpenRouteServiceRepository() {
        restTemplate = new RestTemplate();
        geoCodeUrl = EmissionCalculatorUtil.getOpenRouteServiceProperty("geocodeUrl");
        matrixUrl = EmissionCalculatorUtil.getOpenRouteServiceProperty("matrixUrl");
    }

    public List<Double> getCoordinates(String city) {
        String geoCodeEndPoint = String.format(geoCodeUrl, ORS_TOKEN, city);
        GeoCodeResponse geoCodeResponse = restTemplate.getForObject(geoCodeEndPoint, GeoCodeResponse.class);
        if (Objects.nonNull(geoCodeResponse)) {
            List<Feature> features = geoCodeResponse.getFeatures();
            if (Objects.nonNull(features) && !features.isEmpty()) {
                Geometry geometry = features.get(0).getGeometry();
                if (Objects.nonNull(geometry)) {
                    List<Double> coordinates = geometry.getCoordinates();
                    if (Objects.nonNull(coordinates) && !coordinates.isEmpty()) return coordinates;
                }
            }
        }
        return Collections.emptyList();
    }

    public Optional<Double> getDistance(List<Double> startCoordinates, List<Double> endCoordinates) {
        DistanceMatrixRequest matrixRequest = new DistanceMatrixRequest(startCoordinates, endCoordinates, Collections.singletonList("distance"), "km");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", ORS_TOKEN);
        HttpEntity<DistanceMatrixRequest> entity = new HttpEntity<>(matrixRequest, headers);
        DistanceMatrixResponse response = restTemplate.postForObject(matrixUrl, entity, DistanceMatrixResponse.class);
        if (Objects.nonNull(response)) {
            List<List<Double>> distances = response.getDistances();
            if (Objects.nonNull(distances) && distances.size() > 0) {
                List<Double> sourceDistance = distances.get(0);
                if (sourceDistance.size() > 1) return Optional.of(sourceDistance.get(1));
            }
        }
        return Optional.empty();
    }
}
