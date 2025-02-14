package com.weatherapp.myweatherapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.weatherapp.myweatherapp.controller.WeatherController;
import com.weatherapp.myweatherapp.model.CityInfo;

class WeatherServiceTest {
  @Mock
  private WeatherService weatherService;

  @InjectMocks
  private WeatherController weatherController;

  private CityInfo cityInfo1;

//TESTS FOR FUNCTIONS IMPLEMENTED

//Could be implemented as its own class however this allows for reusability as well, just sometimes not used 
//Default data with valid entries
  @BeforeEach
  void load(){
    MockitoAnnotations.openMocks(this);

    cityInfo1 = new CityInfo();
    CityInfo.CurrentConditions conditions1 = new CityInfo.CurrentConditions();
    conditions1.setConditions("Rain, Overcast");
    conditions1.setSunrise("06:14:21");
    conditions1.setSunset("19:42:13");
    cityInfo1.setCurrentConditions(conditions1);
  }

//--------------------------------------------------------------------------------------------

//TESTS FOR getSunTimings()

//Tests if it works with valid entries 
  @Test
  void testGetSunTimings_Valid(){
    when(weatherService.forecastByCity("London")).thenReturn(cityInfo1);

    Double daylightHours = weatherController.getSunTimings("London");
    double expectedHours = 13.4644; //Manually calculated

    assertEquals(daylightHours, expectedHours, 0.01);
  }

//Tests if it behaves correctly with null entries
  @Test
  void testGetSunTimings_Null(){
    when(weatherService.forecastByCity("NullCity")).thenReturn(null);

    Double result = weatherController.getSunTimings("NullCity");

    assertEquals(-1.0, result);
  }

//Tests if it behaves correctly with invalid format used (not hh:mm:ss)
  @Test
  void testGetSunTimings_InvalidFormat(){
    CityInfo invalidInfo = new CityInfo();
    CityInfo.CurrentConditions invalidConditions = new CityInfo.CurrentConditions();
    invalidConditions.setSunrise("06:13:54");
    invalidConditions.setSunset("invalid"); //invalid entry
    cityInfo1.setCurrentConditions(invalidConditions);

    when(weatherService.forecastByCity("Invalid City")).thenReturn(invalidInfo);

    Double daylightHours = weatherController.getSunTimings("Invalid City");

    assertEquals(-1.0, daylightHours);
  }

//---------------------------------------------------------------------------------------------------------

//TESTS FOR isRaining()

//Tests if it works with valid entries 
@Test
 void testIsRaining_Valid(){
  when(weatherService.forecastByCity("City")).thenReturn(cityInfo1);

  boolean isRaining = weatherController.isRaining("City");

  assertEquals(isRaining, true);
 }

 //Tests if it behaves correctly with null entries
@Test
 void testIsRaining_Null(){
  when(weatherService.forecastByCity("City")).thenReturn(null);

  assertThrows(Exception.class, () -> {
    weatherController.isRaining("City");
  });
 }

 //Tests when entry is not words
@Test
 void testIsRaining_Invalid(){

  CityInfo cityInfoError = new CityInfo();
  CityInfo.CurrentConditions conditions1 = new CityInfo.CurrentConditions();
  conditions1.setConditions("1231"); //Invalid entry (Technically it is valid as it is a string however it does'nt contain any words)
  conditions1.setSunrise("06:14:21");
  conditions1.setSunset("19:42:13");
  cityInfoError.setCurrentConditions(conditions1);

  when(weatherService.forecastByCity("City")).thenReturn(cityInfoError);

  boolean isRaining = weatherController.isRaining("City");

  assertEquals(false, isRaining);
  //Ideally should throw some kind of error
  //As improvement you could implement a scanner which checks if input contains valid words before parsing through isRaining function()
 }
}