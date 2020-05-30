package com.sap.exercise.co2emissioncalculator.repository;

import com.sap.exercise.co2emissioncalculator.model.DistanceMatrixRequest;
import com.sap.exercise.co2emissioncalculator.model.DistanceMatrixResponse;
import com.sap.exercise.co2emissioncalculator.model.Feature;
import com.sap.exercise.co2emissioncalculator.model.GeoCodeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Repository
public class OpenRouteServiceRepository {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String ORS_TOKEN = System.getenv("ORS_TOKEN");
    @Value("${openserviceroute.api.geocodeUrl}")
    private String geoCodeUrl;
    @Value("${openserviceroute.api.matrixUrl}")
    private String matrixUrl;

    public List<Double> getCoordinates(String city) {
        String geoCodeEndPoint = String.format(geoCodeUrl, ORS_TOKEN, city);
        GeoCodeResponse geoCodeResponse = restTemplate.getForObject(geoCodeEndPoint, GeoCodeResponse.class);
        if (Objects.nonNull(geoCodeResponse)) {
            List<Feature> features = geoCodeResponse.getFeatures();
            if (!features.isEmpty()) {
                return features.get(0).getGeometry().getCoordinates();
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
            if (distances.size() > 0) {
                List<Double> sourceDistance = distances.get(0);
                if (sourceDistance.size() > 1) return Optional.of(sourceDistance.get(1));
            }
        }
        return Optional.empty();
    }
}
