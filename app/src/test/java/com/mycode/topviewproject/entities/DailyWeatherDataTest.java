package com.mycode.topviewproject.entities;

import com.mycode.topviewproject.UtilTest;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DailyWeatherDataTest {

    @Test
    void isDailyWeatherNotEqual_differentIds_returnTrue() throws Exception{
        DailyWeatherData dailyWeatherData1 = new DailyWeatherData(UtilTest.dailyWeatherData1);
        DailyWeatherData dailyWeatherData2 = new DailyWeatherData(UtilTest.dailyWeatherData2);
        Assert.assertNotEquals(dailyWeatherData1, dailyWeatherData2);

    }
}
