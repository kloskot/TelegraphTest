package com.weatherapp.myweatherapp.WeatherEndpointTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.weatherapp.myweatherapp.controller.WeatherController;
import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;

//ENDPOINT TESTS 
class WeatherEndpointTest {
    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    //Initialise mock fields
    @BeforeEach
    void load(){
        MockitoAnnotations.openMocks(this);
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------
//Sets of information used for various testing
    private CityInfo mockCityInfo() {
        CityInfo cityInfo1 = new CityInfo();
        CityInfo.CurrentConditions conditions1 = new CityInfo.CurrentConditions();
        conditions1.setConditions("Rain, Overcast");
        conditions1.setSunrise("06:14:21");
        conditions1.setSunset("19:42:13");
        cityInfo1.setCurrentConditions(conditions1);
        return cityInfo1;
    }

    private CityInfo mockCityInfo2(){
        CityInfo cityInfo2 = new CityInfo();
        CityInfo.CurrentConditions conditions2 = new CityInfo.CurrentConditions();
        conditions2.setConditions("Overcast");
        conditions2.setSunrise("06:13:54");
        conditions2.setSunset("18:51:13");
        cityInfo2.setCurrentConditions(conditions2);
        return cityInfo2;
    }

    private CityInfo mockBoundaryInfo(){
        CityInfo boundaryInfo = new CityInfo(); //For boundary tests when used with cityInfo1
        CityInfo.CurrentConditions boundaryConditions = new CityInfo.CurrentConditions();
        boundaryConditions.setConditions("Storm, Heavy Rain");
        boundaryConditions.setSunrise("07:18:24");
        boundaryConditions.setSunset("20:46:16");
        boundaryInfo.setCurrentConditions(boundaryConditions);
        return boundaryInfo;
    }

    private CityInfo mockErroneousInfo(){
        CityInfo erroneousInfo = new CityInfo();
        CityInfo.CurrentConditions conditions = new CityInfo.CurrentConditions();
        conditions.setConditions(null);
        conditions.setSunrise("Sometime in the morning");
        conditions.setSunset("When the sun sets");
        erroneousInfo.setCurrentConditions(conditions);
        return erroneousInfo;
    }
//------------------------------------------------------------------------------------------------------------------------------
// TESTS FOR "/daylightComparison/{location1}/{location2}" endpoint

    //Tests valid inputs 
    @Test
    void testFindDifference_City1(){
        when(weatherService.forecastByCity("London")).thenReturn(mockCityInfo());
        when(weatherService.forecastByCity("Warsaw")).thenReturn(mockCityInfo2());

        ResponseEntity<String> response = weatherController.findDifference("London", "Warsaw");

        assertEquals("London has longer days", response.getBody());
    }

    //Ideally should have implemented a separate valid test here that returns the correct response when 'parameter 2' has longer days

    //Tests for when inputs have different inputs (sunrise and sunset times) but same results (amount of daylight hours)
    @Test
    void testFindDifference_Boundary(){
        when(weatherService.forecastByCity("Kampala")).thenReturn(mockCityInfo());
        when(weatherService.forecastByCity("Lagos")).thenReturn(mockBoundaryInfo());

        ResponseEntity<String> response = weatherController.findDifference("Kampala", "Lagos");

        assertEquals("Both have the same amount of daylight", response.getBody());
    }

    //Test for when erroneous data is parse through
    @Test
    void testFindDifference_Error(){
        when(weatherService.forecastByCity("Kingston")).thenReturn(mockCityInfo());
        when(weatherService.forecastByCity("Mordor")).thenReturn(mockErroneousInfo());

        ResponseEntity<String> response = weatherController.findDifference("Kingston", "Mordor");

        assertEquals("Entry error", response.getBody());
    }
 //---------------------------------------------------------------------------------------------------------------------------------
//TESTS FOR "/isRaining/{city1}/{city2}" endpoint 
    //Tests valid inputs
    @Test
    void testRainStatus_City1(){
        when(weatherService.forecastByCity("Lima")).thenReturn(mockCityInfo());
        when(weatherService.forecastByCity("Rio")).thenReturn(mockCityInfo2());

        ResponseEntity<String> response = weatherController.rainStatus("Lima", "Rio");

        assertEquals("It is raining in Lima", response.getBody());
    }

    //Test when both cities have rain
    @Test
    void testRainStatus_Boundary(){
        when(weatherService.forecastByCity("Bern")).thenReturn(mockBoundaryInfo());
        when(weatherService.forecastByCity("Berlin")).thenReturn(mockCityInfo());

        ResponseEntity<String> response = weatherController.rainStatus("Bern", "Berlin");

        assertEquals("It is raining in both Bern and Berlin", response.getBody());
    }

    //Test for when an input is invalid 
    @Test
    void testRainStatus_Erroneous(){
        when(weatherService.forecastByCity("Diamond City")).thenReturn(mockErroneousInfo());
        when(weatherService.forecastByCity("Dubai")).thenReturn(mockCityInfo());

        ResponseEntity<String> response = weatherController.rainStatus("Diamond City", "Dubai");

        assertEquals("Entry Error", response.getBody());
    }
}
