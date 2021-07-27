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

        Intent intent = getIntent();

        tripTitle.setText(intent.getStringExtra(EXTRA_TITLE_D));
        tripDestination.setText(intent.getStringExtra(EXTRA_DESTINATION_D));
        tripPrice.setText(intent.getStringExtra(EXTRA_PRICE_D));
        //tripRating.setRating(Float.parseFloat(intent.getStringExtra(EXTRA_RATING_D)));
        tripType.setText(intent.getStringExtra(EXTRA_TYPE_D));
        tripStartDate.setText(intent.getStringExtra(EXTRA_START_DATE_D));
        tripEndDate.setText(intent.getStringExtra(EXTRA_END_DATE_D));

        city = tripDestination.getText().toString();

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
                            (weatherResponse.main.temp - 273.15) +
                            "\n" +
                            "Temperature(Min): " +
                            (weatherResponse.main.temp_min - 273.15) +
                            "\n" +
                            "Temperature(Max): " +
                            (weatherResponse.main.temp_max - 273.15) +
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
}
