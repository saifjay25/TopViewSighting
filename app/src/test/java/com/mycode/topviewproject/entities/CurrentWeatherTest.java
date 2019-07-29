package com.mycode.topviewproject.entities;

import com.mycode.topviewproject.UtilTest;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrentWeatherTest {

    @Test
    void isCurrentWeatherNotEqual_differentIds_returnTrue() throws Exception{
        CurrentWeather currentWeather1 = new CurrentWeather(UtilTest.currentWeather1);
        CurrentWeather currentWeather2 = new CurrentWeather(UtilTest.currentWeather2);
        Assert.assertNotEquals(currentWeather1, currentWeather2);
    }

}