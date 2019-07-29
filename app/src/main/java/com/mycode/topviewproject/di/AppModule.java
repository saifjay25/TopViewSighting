package com.mycode.topviewproject.di;

import android.app.Application;

import androidx.room.Room;

import com.mycode.topviewproject.network.WeatherAPI;
import com.mycode.topviewproject.persistence.Repository;
import com.mycode.topviewproject.persistence.WeatherDAO;
import com.mycode.topviewproject.persistence.WeatherDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//repository is going to go be provided or any dao for room or APIs by applying dependency injection to other classes
@Module
public class AppModule {

    @Singleton
    @Provides
    static WeatherDatabase provideWeatherDatabase(Application application){
        return Room.databaseBuilder(application, WeatherDatabase.class,"database").build();

    }

    @Singleton
    @Provides
    static WeatherDAO provideWeatherDao (WeatherDatabase weatherDatabase){
        return weatherDatabase.weatherDAO();
    }

    //able to access the weather dao through provideWeatherdao
    @Singleton
    @Provides
    static Repository provideRepository(WeatherDAO weatherDAO, WeatherAPI weatherAPI){
        return new Repository(weatherDAO,weatherAPI);
    }

    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance(){
        HttpLoggingInterceptor intercept = new HttpLoggingInterceptor();
        intercept.setLevel((HttpLoggingInterceptor.Level.BODY)) ;
        OkHttpClient client  = new OkHttpClient.Builder().addInterceptor(intercept).build();
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://api.darksky.net/forecast/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retro;
    }

    @Singleton
    @Provides
    static WeatherAPI provideWeatherApi(Retrofit retrofit){
        return retrofit.create(WeatherAPI.class);
    }
}
