package com.mycode.topviewproject.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mycode.topviewproject.entities.CurrentWeather;
import com.mycode.topviewproject.entities.DailyWeatherData;
import com.mycode.topviewproject.entities.Weather;
import com.mycode.topviewproject.persistence.Repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class MainViewModel extends ViewModel {

    private Repository repository;
    private LiveData<CurrentWeather> allCurrentData;
    private LiveData<List<DailyWeatherData>> allDailyData;

    @Inject
    public MainViewModel(Repository repository) {
        this.repository = repository;
        allCurrentData = this.repository.getAllCurrentData();
        allDailyData = this.repository.getAllDailyData();
    }

    public LiveData<Weather> currentAPICall(String key, Double latitude, Double longitude){
        return repository.currentAPICall(key, latitude,longitude);
    }

    public LiveData<Weather> futureAPICall(String key, Double latitude, Double longitude, String time){
        return repository.futureAPICall(key,latitude,longitude,time);
    }

    public Flowable<Resource<Integer>> addCurrentWeather(CurrentWeather currentWeather){
        return repository.insertCurrentData(currentWeather);
    }

    public Flowable<Resource<Integer>> deleteCurrentWeather(){
        return repository.deleteCurrentData();

    }

    public LiveData<Integer> getcount(){
        return repository.getcount();
    }

    public LiveData<CurrentWeather> getAllCurrentData(){
        return allCurrentData;
    }


    public Flowable<Resource<Integer>> addDailyWeather(DailyWeatherData dailyWeatherData){
        return  repository.addDailyData(dailyWeatherData);
    }

    public Flowable<Resource<Integer>> deleteDailyWeather(){
        return repository.deleteDailyData();
    }


    public LiveData<List<DailyWeatherData>> getAllDailyData(){
        return allDailyData;
    }

}
