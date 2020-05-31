package com.sap.exercise.co2emissioncalculator.repository;

import com.sap.exercise.co2emissioncalculator.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpenRouteServiceRepositoryTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenRouteServiceRepository subject;

    private static final String GET_COORDINATE_URL = "https://api.openrouteservice.org/geocode/search?api_key=%s&text=%s&layers=locality&size=1";
    private static final String CITY = "city";
    private static final String API_KEY = "api-key-open-route-service";

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(subject, "ORS_TOKEN", API_KEY);
    }

    @Test
    public void testGetCoordinates() {
        List<Double> coordinates = Arrays.asList(1.12323, 12.4343);
        GeoCodeResponse geoCodeResponse = getMockGeoCodeResponse(coordinates);
        when(restTemplate.getForObject(String.format(GET_COORDINATE_URL, API_KEY, CITY), GeoCodeResponse.class)).thenReturn(geoCodeResponse);
        List<Double> resultCoordinates = subject.getCoordinates(CITY);
        assertTrue(resultCoordinates.contains(coordinates.get(0)));
        assertTrue(resultCoordinates.contains(coordinates.get(1)));
    }

    @Test
    public void testInvalidResult() {
        when(restTemplate.getForObject(String.format(GET_COORDINATE_URL, API_KEY, CITY), GeoCodeResponse.class)).thenReturn(null);
        List<Double> resultCoordinates = subject.getCoordinates(CITY);
        assertTrue(resultCoordinates.isEmpty());

        List<Double> coordinates = Arrays.asList(1.12323, 12.4343);
        GeoCodeResponse geoCodeResponse = getMockGeoCodeResponse(coordinates);
        geoCodeResponse.setFeatures(null);
        resultCoordinates = subject.getCoordinates(CITY);
        assertTrue(resultCoordinates.isEmpty());

        geoCodeResponse = getMockGeoCodeResponse(coordinates);
        geoCodeResponse.setFeatures(Collections.emptyList());
        resultCoordinates = subject.getCoordinates(CITY);
        assertTrue(resultCoordinates.isEmpty());

        geoCodeResponse = getMockGeoCodeResponse(coordinates);
        geoCodeResponse.getFeatures().get(0).setGeometry(null);
        resultCoordinates = subject.getCoordinates(CITY);
        assertTrue(resultCoordinates.isEmpty());

        geoCodeResponse = getMockGeoCodeResponse(coordinates);
        geoCodeResponse.getFeatures().get(0).getGeometry().setCoordinates(null);
        resultCoordinates = subject.getCoordinates(CITY);
        assertTrue(resultCoordinates.isEmpty());

        geoCodeResponse = getMockGeoCodeResponse(coordinates);
        geoCodeResponse.getFeatures().get(0).getGeometry().setCoordinates(Collections.emptyList());
        resultCoordinates = subject.getCoordinates(CITY);
        assertTrue(resultCoordinates.isEmpty());
    }

    @Test
    public void testCalculatingDistance() {
        List<Double> startCoordinates = Arrays.asList(10.213313, 2.323232);
        List<Double> endCoordinates = Arrays.asList(-20.223, 61.31212);
        double distance = 12.78;
        DistanceMatrixResponse mockDistanceResponse = getMockDistanceResponse(distance);
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), any())).thenReturn(mockDistanceResponse);
        Optional<Double> resultDistanceOptional = subject.getDistance(startCoordinates, endCoordinates);
        assertTrue(resultDistanceOptional.isPresent());
        assertEquals(resultDistanceOptional.get(), distance);
    }


    @Test
    public void testInvalidResultDistance() {
        List<Double> startCoordinates = Arrays.asList(10.213313, 2.323232);
        List<Double> endCoordinates = Arrays.asList(-20.223, 61.31212);
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), any())).thenReturn(null);
        Optional<Double> resultDistanceOptional = subject.getDistance(startCoordinates, endCoordinates);
        assertFalse(resultDistanceOptional.isPresent());

        double distance = 12.78;
        DistanceMatrixResponse mockDistanceResponse = getMockDistanceResponse(distance);
        mockDistanceResponse.setDistances(null);
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), any())).thenReturn(mockDistanceResponse);
        resultDistanceOptional = subject.getDistance(startCoordinates, endCoordinates);
        assertFalse(resultDistanceOptional.isPresent());

        mockDistanceResponse = getMockDistanceResponse(distance);
        mockDistanceResponse.setDistances(Collections.emptyList());
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), any())).thenReturn(mockDistanceResponse);
        resultDistanceOptional = subject.getDistance(startCoordinates, endCoordinates);
        assertFalse(resultDistanceOptional.isPresent());
    }

    @Test
    public void testExceptionInOpenRouteService() {
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), any())).thenThrow(RestClientException.class);
        List<Double> startCoordinates = Arrays.asList(10.213313, 2.323232);
        List<Double> endCoordinates = Arrays.asList(-20.223, 61.31212);
        assertThrows(RestClientException.class, () -> subject.getDistance(startCoordinates, endCoordinates));
    }


    private GeoCodeResponse getMockGeoCodeResponse(List<Double> coordinates) {
        GeoCodeResponse geoCodeResponse = new GeoCodeResponse();
        List<Feature> features = new ArrayList<>();
        Feature feature = new Feature();
        Geometry geometry = new Geometry();

        geometry.setCoordinates(coordinates);
        feature.setGeometry(geometry);
        features.add(feature);
        geoCodeResponse.setFeatures(features);
        return geoCodeResponse;
    }

    private DistanceMatrixResponse getMockDistanceResponse(double distance) {
        DistanceMatrixResponse response = new DistanceMatrixResponse();
        List<Double> result = Arrays.asList(0.0, distance);
        response.setDistances(Collections.singletonList(result));
        return response;
    }

}
