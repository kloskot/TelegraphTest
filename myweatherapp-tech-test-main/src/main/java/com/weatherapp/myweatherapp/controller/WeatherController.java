package com.weatherapp.myweatherapp.controller;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WeatherController {

  @Autowired
  WeatherService weatherService;
  //Used for gathering raw data from the API
  @GetMapping("/forecast/{city}")
  public ResponseEntity<CityInfo> forecastByCity(@PathVariable("city") String city) {
    try{
    CityInfo ci = weatherService.forecastByCity(city);

    return ResponseEntity.ok(ci);
    //Throws exception mainly for invalid entry
    }catch(Exception e){
      CityInfo error = new CityInfo();
      error.setAddress(city + " is Invalid");
      return ResponseEntity.ok(error);
    }
  }

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

// Task: given two city names, compare the length of the daylight hours and return the city with the longest day

  //Deserializes CityInfo and calculates a value of daylight hours for a given city
  public Double getSunTimings(String city){
    //Exception handling implemented here, will return negative value which is separably processed at endpoint
    try{
      CityInfo ci = weatherService.forecastByCity(city);

      String sunrise = ci.getCurrentConditions().getSunrise();
      String sunset = ci.getCurrentConditions().getSunset();
      //Used for manually checking in runtime
      System.out.println(sunrise);
      System.out.println(sunset);

      LocalTime sunriseTime = LocalTime.parse(sunrise);
      LocalTime sunsetTime = LocalTime.parse(sunset);

      Duration dayLightDuration = Duration.between(sunriseTime, sunsetTime);

      //Formula to convert time difference from format hh:mm:ss to single figure
      double totalHours = dayLightDuration.toHours() + dayLightDuration.toMinutes() % 60 / 60.0 + dayLightDuration.getSeconds() % 60 / 3600.0;

      System.out.println(city + ": " + totalHours);
      return totalHours;
    }
    catch (Exception e){
      System.out.println("Error parsing");
      return -1.0;
    }
  }

  //Endpoint for comparing daylight hours between two cities
  @GetMapping("/daylightComparison/{location1}/{location2}")
  public ResponseEntity<String> findDifference(
    @PathVariable("location1") String location1,
    @PathVariable("location2") String location2)
    {
      Double daytime1 = getSunTimings(location1);
      Double daytime2 = getSunTimings(location2);
      //Exception handling result from getSunTimes() 
      if(daytime1 == -1.0 || daytime2 == -1.0){
        return ResponseEntity.ok("Entry error");
      }
      if(daytime1 > daytime2){
        return ResponseEntity.ok(location1 + " has longer days");
      }
      else if(daytime2 > daytime1){
        return ResponseEntity.ok(location2 + " has longer days");
      }
      else{
        return ResponseEntity.ok("Both have the same amount of daylight");
      }

    }

  //-----------------------------------------------------------------------------------------------------------------------------------------------------------
  // Task: given two city names, check which city its currently raining in
  public Boolean isRaining(String city){
    
    CityInfo ci = weatherService.forecastByCity(city);
    String weather_condition = ci.getCurrentConditions().getConditions();

    //Checks for the mention of word "rain" in the description which is the indicator that it is raining
    if(weather_condition.toLowerCase().contains("rain")){
      return true;
    }
    else{
      return false;
    }
  }

  //Endpoint for checking which city it is raining in
  @GetMapping("/isRaining/{city1}/{city2}")
  public ResponseEntity<String> rainStatus(@PathVariable("city1") String city1,
                                           @PathVariable("city2") String city2){
    //Exception handling is implemented in class (boolean can only have two set values so this allows it to be distinguished from them)
    try{                                        
    boolean status1 = isRaining(city1);
    boolean status2 = isRaining(city2);
    if(status1 && status2){
      return ResponseEntity.ok(("It is raining in both " + city1 + " and " + city2));
    }else if(status1){
      return ResponseEntity.ok("It is raining in " + city1);
    }else if(status2){
      return ResponseEntity.ok("It is raining " + city2);
    }else{
      return ResponseEntity.ok("It is not raining in either");
    }
  }catch(Exception e){
    return ResponseEntity.ok("Entry Error");
  }
  }
}
