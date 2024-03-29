package com.mycode.topviewproject.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mycode.topviewproject.entities.CurrentWeather;
import com.mycode.topviewproject.entities.DailyWeatherData;

import java.util.List;

import io.reactivex.Single;

//implements database calls in order to query and insert specific data
@Dao
public interface WeatherDAO{

    @Query("SELECT * FROM currentWeatherTable")
    LiveData<CurrentWeather> getAllCurrentData();

    @Query("SELECT COUNT(humidity) FROM currentWeatherTable")
    LiveData<Integer> getRowCount();

    @Insert
    Single<Long> addCurrentData(CurrentWeather weather);

    @Query("DELETE FROM currentWeatherTable")
    Single<Integer> removeAllCurrentData();

    @Query("SELECT * FROM dailyWeatherTable")
    LiveData<List<DailyWeatherData>> getAllDailyData();

    @Insert
    Single<Long> addDailyData(DailyWeatherData weather);

    @Query("DELETE FROM dailyWeatherTable")
    Single<Integer> removeAllDailyData();

}
