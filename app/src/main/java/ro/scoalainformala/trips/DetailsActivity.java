package ro.scoalainformala.trips;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class
DetailsActivity extends AppCompatActivity {

    // Location permission
    @SuppressLint("MissingPermission")
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted) {
                    // Permission is granted. Continue the action or workflow in your app.
                    displayCurrentUserLocation();
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their decision
                    handlePermissionDenied();
                }
            });
    private FusedLocationProviderClient fusedLocationClient;

    public static final String EXTRA_IMAGE_D = "ro.scoalainformala.trips.EXTRA_IMAGE_D";
    public static final String EXTRA_TITLE_D = "ro.scoalainformala.trips.EXTRA_TITLE_D";
    public static final String EXTRA_DESTINATION_D = "ro.scoalainformala.trips.EXTRA_DESTINATION_D";
    public static final String EXTRA_PRICE_D = "ro.scoalainformala.trips.EXTRA_PRICE_D";
    public static final String EXTRA_RATING_D = "ro.scoalainformala.trips.EXTRA_RATING_D";
    public static final String EXTRA_TYPE_D = "ro.scoalainformala.trips.EXTRA_TYPE_D";
    public static final String EXTRA_START_DATE_D = "ro.scoalainformala.trips.EXTRA_START_DATE_D";
    public static final String EXTRA_END_DATE_D = "ro.scoalainformala.trips.EXTRA_END_DATE_D";
    public static final String EXTRA_IS_FAVOURITE_D = "ro.scoalainformala.trips.EXTRA_IS_FAVOURITE_D";

    final String APP_ID = "95742b5d52241bd561e65e6b5ec02605";
    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;
    String Location_Provider = LocationManager.GPS_PROVIDER;
    final String WEATHER_URL = "https://api.openweathermap.org/";
    private String lat;
    private String lon;
    private String city;

    private TextView tripTitle;
    private TextView tripDestination;
    //private RatingBar tripRating;
    private TextView tripPrice;
    private TextView tripType;
    private TextView tripStartDate;
    private TextView tripEndDate;
    private TextView weather;
    private TextView temperature;

    LocationManager mLocationManager;
    LocationListener mLocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tripTitle = findViewById(R.id.details_trip_name);
        tripDestination = findViewById(R.id.details_trip_destination);
        //tripRating = findViewById(R.id.details_trip_rating);
        tripPrice = findViewById(R.id.details_trip_price);
        tripType = findViewById(R.id.details_trip_type);
        tripStartDate = findViewById(R.id.details_start_date);
        tripEndDate = findViewById(R.id.details_end_date);
        weather = findViewById(R.id.details_weather_state);
        temperature = findViewById(R.id.details_weather_temperature);

        Intent intent = getIntent();

        tripTitle.setText(intent.getStringExtra(EXTRA_TITLE_D));
        tripDestination.setText(intent.getStringExtra(EXTRA_DESTINATION_D));
        tripPrice.setText(intent.getStringExtra(EXTRA_PRICE_D));
        //tripRating.setRating(Float.parseFloat(intent.getStringExtra(EXTRA_RATING_D)));
        tripType.setText(intent.getStringExtra(EXTRA_TYPE_D));
        tripStartDate.setText(intent.getStringExtra(EXTRA_START_DATE_D));
        tripEndDate.setText(intent.getStringExtra(EXTRA_END_DATE_D));

        city = tripDestination.getText().toString();

//        if(city != null){
//            getWeatherForNewCity(city);
//        } else{
//            getWeatherForCurrentLocation();
//        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        } else if (city != null){
            getCityData();
        } else {
            getCurrentData();
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    private void displayCurrentUserLocation(){
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            // Got last known location. In some rare situation this can be null.
            if(location != null){
                // Logic to handle location object
                lat = String.valueOf(location.getLatitude());
                lon = String.valueOf(location.getLongitude());

//                RequestParams params = new RequestParams();
//                params.put("lat", latitude);
//                params.put("lon", longitude);
//                params.put("appid", APP_ID);
//                doNetworking(params);
            } else{
                Toast.makeText(this, "Unknown location", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handlePermissionDenied(){
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);

        Snackbar.make(coordinatorLayout, R.string.no_location_error, Snackbar.LENGTH_LONG).show();
    }

    private void getCityData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OpenWeatherService service = retrofit.create(OpenWeatherService.class);
        Call<WeatherResponse> call = service.getWeatherCity(city, APP_ID);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;

                    String stringBuilder = "Country: " +
                            weatherResponse.sys.country +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main.temp +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main.temp_min +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main.temp_max +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main.pressure;

                    weather.setText(stringBuilder);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                weather.setText(t.getMessage());
            }
        });
    }

    private void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OpenWeatherService service = retrofit.create(OpenWeatherService.class);
        Call<WeatherResponse> call = service.getWeather(lat, lon, APP_ID);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;

                    String stringBuilder = "Country: " +
                            weatherResponse.sys.country +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main.temp +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main.temp_min +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main.temp_max +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main.pressure;

                    weather.setText(stringBuilder);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                weather.setText(t.getMessage());
            }
        });
    }

//    @Override
//    protected void onResume(){
//        super.onResume();
//        Intent intent = getIntent();
//        String city = tripDestination.getText().toString();
//
//        if(city != null){
//            getWeatherForNewCity(city);
//        } else{
//            getWeatherForCurrentLocation();
//        }
//    }

//    private void getWeatherForNewCity(String city){
//        RequestParams params = new RequestParams();
//        params.put("q", city);
//        params.put("appid", APP_ID);
//        doNetworking(params);
//    }

//    private void getWeatherForCurrentLocation(){
//        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        mLocationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(@NonNull Location location) {
//
//                String latitude = String.valueOf(location.getLatitude());
//                String longitude = String.valueOf(location.getLongitude());
//
//                RequestParams params = new RequestParams();
//                params.put("lat", latitude);
//                params.put("lon", longitude);
//                params.put("appid", APP_ID);
//                doNetworking(params);
//            }
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras){
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//                //not able to get location
//            }
//        };
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
//            return;
//        }
//        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListener);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//
//        if(requestCode==REQUEST_CODE)
//        {
//            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
//            {
//                Toast.makeText(DetailsActivity.this,"Location got successfully",Toast.LENGTH_SHORT).show();
//                getWeatherForCurrentLocation();
//            }
//            else
//            {
//                //user denied the permission
//            }
//        }
//    }

//    private  void doNetworking(RequestParams params)
//    {
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                Toast.makeText(DetailsActivity.this,"Data got Success",Toast.LENGTH_SHORT).show();
//
//                weatherData weatherD = weatherData.fromJson(response);
//                updateUI(weatherD);
//
//
//                // super.onSuccess(statusCode, headers, response);
//            }
//
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                //super.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//        });
//    }
//
//    private void updateUI(weatherData weather){
//        temperature.setText(weather.getmTemperature());
//        weatherState.setText(weather.getmWeatherType());
//    }
}
