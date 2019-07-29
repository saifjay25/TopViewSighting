package com.mycode.topviewproject.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mycode.topviewproject.R;
import com.mycode.topviewproject.entities.DailyWeatherData;
import com.mycode.topviewproject.entities.Weather;
import com.mycode.topviewproject.persistence.Repository;
import com.mycode.topviewproject.viewmodel.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Grid2 extends DaggerFragment {
    private OnFragmentInteractionListener mListener;
    private String key;
    private Double latitude;
    private Double longitude;
    private String time;
    private String day;
    private MainViewModel viewModel;
    private DatesAndTimes getTime;
    private TextView temp, date;
    private ImageView imageView;
    private static Weather gweather;
    private DailyWeatherData dailyWeather;
    @Inject
    ViewModelProviderFactory providerFactory;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public Grid2(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this,providerFactory).get(MainViewModel.class);
        getTime = new DatesAndTimes();
        //checks to see if there is wifi and has the arguments
        if(getArguments() != null && !Repository.noWifi) {
            key = getArguments().getString("key");
            latitude = getArguments().getDouble("latitude");
            longitude = getArguments().getDouble("longitude");
            time = getArguments().getString("time");
            day = getArguments().getString("day");
            observingWeather();
            //checks for if there is no wifi then it retrieves the cached data
        }else if(Repository.noWifi){
            day = getArguments().getString("day");
            viewModel.getAllDailyData().observe(this, new Observer<List<DailyWeatherData>>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onChanged(List<DailyWeatherData> dailyWeatherData) {
                    if(Repository.noWifi && !dailyWeatherData.isEmpty()) {
                        for(int i=0; i<7; i++) {
                            if(dailyWeatherData.get(i).getTab().equals("Tab2")){
                                dailyWeather = dailyWeatherData.get(i);
                                setPic(dailyWeatherData.get(0).getSummary());
                                break;
                            }
                        }
                        temp.setText("Temp: "+dailyWeather.getTemperatureHigh()+"°");
                        date.setText("Date: "+day);
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid2, container, false);
        LinearLayout rl = view.findViewById(R.id.clicker2);
        temp = view.findViewById(R.id.currentTemperature);
        date = view.findViewById(R.id.time);
        imageView = view.findViewById(R.id.image);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Repository.noWifi && gweather != null) {
                    String precipMaxTime = getTime.getTime(gweather.getDailyWeather().getData().get(0).getPrecipIntensityMaxTime());
                    String sunriseTime = getTime.getTime(gweather.getDailyWeather().getData().get(0).getSunriseTime());
                    String tempHighTime = getTime.getTime(gweather.getDailyWeather().getData().get(0).getTemperatureHighTime());
                    String sunsetTime = getTime.getTime(gweather.getDailyWeather().getData().get(0).getSunsetTime());
                    String tempLowTime = getTime.getTime(gweather.getDailyWeather().getData().get(0).getTemperatureLowTime());
                    showDialogAnime(R.style.dialogSlide,
                            "Precipitation Intensity Maximum Time: " + precipMaxTime + "\n" + "\n" +
                                    "Temperature High Time: " + tempHighTime + "\n" + "\n" +
                                    "Temperature Low Time: " + tempLowTime + "\n" + "\n" +
                                    "Sunrise Time: " + sunriseTime + "\n" + "\n" +
                                    "Sunset Time: " + sunsetTime + "\n" + "\n" +
                                    "Cloud Cover: " + convertPercent(gweather.getDailyWeather().getData().get(0).getCloudCover()) + "\n" + "\n" +
                                    "Pressure: " + gweather.getDailyWeather().getData().get(0).getPressure() + "\n" + "\n" +
                                    "MoonPhase: " + gweather.getDailyWeather().getData().get(0).getMoonPhase() + "\n" + "\n" +
                                    "Visibility: " + gweather.getDailyWeather().getData().get(0).getVisibility()+" miles");
                }else{
                    if(dailyWeather != null) {
                        String precipMaxTime = getTime.getTime(dailyWeather.getPrecipIntensityMaxTime());
                        String sunriseTime = getTime.getTime(dailyWeather.getSunriseTime());
                        String tempHighTime = getTime.getTime(dailyWeather.getTemperatureHighTime());
                        String sunsetTime = getTime.getTime(dailyWeather.getSunsetTime());
                        String tempLowTime = getTime.getTime(dailyWeather.getTemperatureLowTime());
                        showDialogAnime(R.style.dialogSlide,
                                "Precipitation Intensity Maximum Time: " + precipMaxTime + "\n" + "\n" +
                                        "Temperature High Time: " + tempHighTime + "\n" + "\n" +
                                        "Temperature Low Time: " + tempLowTime + "\n" + "\n" +
                                        "Sunrise Time: " + sunriseTime + "\n" + "\n" +
                                        "Sunset Time: " + sunsetTime + "\n" + "\n" +
                                        "Cloud Cover: " + convertPercent(dailyWeather.getCloudCover()) + "\n" + "\n" +
                                        "Pressure: " + dailyWeather.getPressure() + "\n" + "\n" +
                                        "MoonPhase: " + dailyWeather.getMoonPhase() + "\n" + "\n" +
                                        "Visibility: " + dailyWeather.getVisibility() + " miles");
                    }
                }
            }
        });
        return view;
    }
    private void observingWeather() {
        viewModel.futureAPICall(key, latitude, longitude, time).observe(this, new Observer<Weather>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(Weather weather) {
                weather.getDailyWeather().getData().get(0).setTab("Tab2");
                mDisposable.add(viewModel.addDailyWeather(weather.getDailyWeather().getData().get(0))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
                gweather = weather;
                temp.setText("Temp: "+weather.getDailyWeather().getData().get(0).getTemperatureHigh()+"°");
                date.setText("Date: "+day);
                setPic(weather.getDailyWeather().getData().get(0).getSummary());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void showDialogAnime(int type, String info){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("More Information");
        alertDialog.setMessage(info);
        alertDialog.getWindow().getAttributes().windowAnimations = type;
        alertDialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    private String convertPercent(String num){
        Double number = Double.parseDouble(num);
        int percentage = (int) ((1 - number) * 100);
        if (number == 0) {
            percentage = 0;
        }
        return percentage+"%";
    }

    private void setPic(String summary){
        if(summary.toLowerCase().contains("rain")){
            imageView.setImageResource(R.mipmap.rain);
            return;
        }
        if(summary.toLowerCase().contains("partly cloudy")){
            imageView.setImageResource(R.mipmap.partlycloudy);
            return;
        }
        if(summary.toLowerCase().contains("cloudy")){
            imageView.setImageResource(R.mipmap.cloudy);
            return;
        }
        if(summary.toLowerCase().contains("snow")){
            imageView.setImageResource(R.mipmap.snow);
            return;
        }
        if(summary.toLowerCase().contains("clear" ) || summary.toLowerCase().contains("sunny")){
            imageView.setImageResource(R.mipmap.sunny);
            return;
        }
        if(summary.toLowerCase().contains("wind" )|| summary.toLowerCase().contains("breezy")|| summary.toLowerCase().contains("windy")){
            imageView.setImageResource(R.mipmap.wind);
            return;
        }
        imageView.setImageResource(R.mipmap.partlycloudy);
    }
}