package com.mycode.topviewproject.ui;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.mycode.topviewproject.R;
import com.mycode.topviewproject.databinding.ActivityMainBinding;
import com.mycode.topviewproject.entities.CurrentWeather;
import com.mycode.topviewproject.entities.Weather;
import com.mycode.topviewproject.internet.CheckConnection;
import com.mycode.topviewproject.persistence.Repository;
import com.mycode.topviewproject.viewmodel.ViewModelProviderFactory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.inject.Inject;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends DaggerAppCompatActivity  {

    private Calendar now = Calendar.getInstance();
    private LocationManager locationManager;
    private CurrentWeather current = null;
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> times = new ArrayList<>();
    private ActivityMainBinding binding;
    private DatesAndTimes sevenDates = new DatesAndTimes();
    private final String key = "97357a1cc59a1a427e9ade54f72a10b9";
    private ProgressDialog progressDialog;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private MainViewModel viewModel;
    private ImageView imageView;
    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = ViewModelProviders.of(this, providerFactory).get(MainViewModel.class);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Fetching Location");
        progressDialog.setCancelable(false);
        progressDialog.show();
        imageView = findViewById(R.id.image);
        LinearLayout lr = findViewById(R.id.clicker);
            lr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(current != null) {
                        String summary = current.getSummary();
                        String aptemp = current.getApparentTemp();
                        String humid = current.getHumidity();
                        String precip = current.getPrecipProbability();
                        String wind = current.getWindSpeed();
                        showDialogAnime(R.style.dialogSlide,
                                "Apparent Temperature: " + aptemp + "°" + "\n" + "\n" +
                                        "Precipitation Probability: " + convertPercent(precip) + "\n" + "\n" +
                                        "Sunrise Time: " + summary + "\n" + "\n" +
                                        "Sunset Time: " + convertPercent(humid) + "\n" + "\n" +
                                        "Wind Speed: " + wind + " mph" + "\n" + "\n");
                    }
                }
            });
        for (int i = 0; i < 7; i++) {
            Date date = now.getTime();
            System.out.println(date);
            String day = sevenDates.date(now.getTime().toString());
            String APITime = sevenDates.getWholeDate(date);
            times.add(APITime);
            dates.add(day);
            now.add(Calendar.HOUR, 24);
        }
        CheckConnection checkConnection = new CheckConnection(this);
        if (checkConnection.isOnline()) {
            mDisposable.add(viewModel.deleteCurrentWeather()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe());
            mDisposable.add(viewModel.deleteDailyWeather()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe());
            Repository.noWifi = false;
            locationPermission();
        } else {
            progressDialog.dismiss();
            viewModel.getAllCurrentData().observe(this, new Observer<CurrentWeather>() {
                @Override
                public void onChanged(CurrentWeather currentWeather) {
                    if(!currentWeather.getSummary().isEmpty()) {
                        current = currentWeather;
                        binding.setCurrentWeather(currentWeather);
                        setPic(currentWeather.getSummary(),imageView);
                    }
                }
            });
            Bundle bundle = new Bundle();
            bundle.putString("day", dates.get(1));
            Grid1 data = new Grid1();
            data.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, data).commit();

            Bundle bundle2 = new Bundle();
            bundle2.putString("day", dates.get(2));
            Grid2 data2 = new Grid2();
            data2.setArguments(bundle2);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, data2).commit();

            Bundle bundle3 = new Bundle();
            bundle3.putString("day", dates.get(3));
            Grid3 data3 = new Grid3();
            data3.setArguments(bundle3);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment3, data3).commit();

            Bundle bundle4 = new Bundle();
            bundle4.putString("day", dates.get(4));
            Grid4 data4 = new Grid4();
            data4.setArguments(bundle4);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment4, data4).commit();

            Bundle bundle5 = new Bundle();
            bundle5.putString("day", dates.get(5));
            Grid5 data5 = new Grid5();
            data5.setArguments(bundle5);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment5, data5).commit();

            Bundle bundle6 = new Bundle();
            bundle6.putString("day", dates.get(6));
            Grid6 data6 = new Grid6();
            data6.setArguments(bundle6);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment6, data6).commit();
        }
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (Repository.counter == 0) {
                progressDialog.dismiss();
                viewModel.currentAPICall(key, location.getLatitude(), location.getLongitude())
                        .observe(MainActivity.this, new Observer<Weather>() {
                            @Override
                            public void onChanged(Weather weather) {
                                current = weather.getCurrentWeather();
                                weather.getCurrentWeather().setTime(dates.get(0));
                                binding.setCurrentWeather(weather.getCurrentWeather());
                                setPic(weather.getCurrentWeather().getSummary(),imageView);
                                mDisposable.add(viewModel.addCurrentWeather(weather.getCurrentWeather())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe());
                            }
                        });
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", location.getLatitude());
                bundle.putDouble("longitude", location.getLongitude());
                bundle.putString("key", key);
                bundle.putString("time", times.get(1));
                bundle.putString("day", dates.get(1));
                Grid1 data = new Grid1();
                data.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, data).commit();

                Bundle bundle2 = new Bundle();
                bundle2.putDouble("latitude", location.getLatitude());
                bundle2.putDouble("longitude", location.getLongitude());
                bundle2.putString("key", key);
                bundle2.putString("time", times.get(2));
                bundle2.putString("day", dates.get(2));
                Grid2 data2 = new Grid2();
                data2.setArguments(bundle2);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, data2).commit();

                Bundle bundle3 = new Bundle();
                bundle3.putDouble("latitude", location.getLatitude());
                bundle3.putDouble("longitude", location.getLongitude());
                bundle3.putString("key", key);
                bundle3.putString("time", times.get(3));
                bundle3.putString("day", dates.get(3));
                Grid3 data3 = new Grid3();
                data3.setArguments(bundle3);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment3, data3).commit();

                Bundle bundle4 = new Bundle();
                bundle4.putDouble("latitude", location.getLatitude());
                bundle4.putDouble("longitude", location.getLongitude());
                bundle4.putString("key", key);
                bundle4.putString("time", times.get(4));
                bundle4.putString("day", dates.get(4));
                Grid4 data4 = new Grid4();
                data4.setArguments(bundle4);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment4, data4).commit();

                Bundle bundle5 = new Bundle();
                bundle5.putDouble("latitude", location.getLatitude());
                bundle5.putDouble("longitude", location.getLongitude());
                bundle5.putString("key", key);
                bundle5.putString("time", times.get(5));
                bundle5.putString("day", dates.get(5));
                Grid5 data5 = new Grid5();
                data5.setArguments(bundle5);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment5, data5).commit();

                Bundle bundle6 = new Bundle();
                bundle6.putDouble("latitude", location.getLatitude());
                bundle6.putDouble("longitude", location.getLongitude());
                bundle6.putString("key", key);
                bundle6.putString("time", times.get(6));
                bundle6.putString("day", dates.get(6));
                Grid6 data6 = new Grid6();
                data6.setArguments(bundle6);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment6, data6).commit();
            }
            Repository.counter++;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };

    private void locationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        assert locationManager != null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setPic(String summary, ImageView image){
        if(summary.toLowerCase().contains("rain")){
            image.setImageResource(R.mipmap.rain);
            return;
        }
        if(summary.toLowerCase().contains("partly cloudy")){
            image.setImageResource(R.mipmap.partlycloudy);
            return;
        }
        if(summary.toLowerCase().contains("cloudy")){
            image.setImageResource(R.mipmap.cloudy);
            return;
        }
        if(summary.toLowerCase().contains("snow")){
            image.setImageResource(R.mipmap.snow);
            return;
        }
        if(summary.toLowerCase().contains("clear" ) || summary.toLowerCase().contains("sunny")){
            image.setImageResource(R.mipmap.sunny);
            return;
        }
        if(summary.toLowerCase().contains("wind" )|| summary.toLowerCase().contains("breezy")|| summary.toLowerCase().contains("windy")){
            image.setImageResource(R.mipmap.wind);
            return;
        }
        image.setImageResource(R.mipmap.partlycloudy);
    }


    private void showDialogAnime(int type, String info){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("More Information");
        alertDialog.setMessage(info);
        alertDialog.getWindow().getAttributes().windowAnimations = type;
        alertDialog.show();
    }

    private String convertPercent(String num){
        Double number = Double.parseDouble(num);
        int percentage = (int) ((1 - number) * 100);
        if (number == 0) {
            percentage = 0;
        }
        return percentage+"%";
    }

    @Override
    protected void onStop() {
        super.onStop();
        Repository.counter = 0;
        mDisposable.clear();
    }
}
