package com.weatherapp.myweatherapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CityInfo {

  @JsonProperty("address")
  String address;

  @JsonProperty("description")
  String description;

  @JsonProperty("currentConditions")
  CurrentConditions currentConditions;

  @JsonProperty("days")
  List<Days> days;

  //Not used in implementation of tasks (Used in exception handling in forecast endpoint)
  public void setAddress(String address){
    this.address = address;
  }

  //Getter and Setter methods for CurrentConditions 
  public CurrentConditions getCurrentConditions() {
    return currentConditions;
  }

  public void setCurrentConditions(CurrentConditions currentConditions){
    this.currentConditions = currentConditions;
  }

  public static class CurrentConditions {
    @JsonProperty("temp")
    String currentTemperature;

    @JsonProperty("sunrise")
    public
    String sunrise;

    @JsonProperty("sunset")
    public
    String sunset;

    @JsonProperty("feelslike")
    String feelslike;

    @JsonProperty("humidity")
    String humidity;

    @JsonProperty("conditions")
    String conditions;

    //Getter and Setter methods for conditions field
    public String getConditions(){
      return conditions;
    }

    public void setConditions(String conditions){
      this.conditions = conditions;
    }

    //Getter and Setter for sunrise field
    public String getSunrise() {
      return sunrise;
    }

    public void setSunrise(String sunrise){
      this.sunrise = sunrise;
    }

    //Getter and Setter for sunset field
    public String getSunset() {
      return sunset;
    }

    public void setSunset(String sunset){
      this.sunset = sunset;
    }

    //Setters should be removed as they are used for testing if this were a product and be deployed for security purposes 
  }

  static class Days {

    @JsonProperty("datetime")
    String date;

    @JsonProperty("temp")
    String currentTemperature;

    @JsonProperty("tempmax")
    String maxTemperature;

    @JsonProperty("tempmin")
    String minTemperature;

    @JsonProperty("conditions")
    String conditions;

    @JsonProperty("description")
    String description;

  }

}
