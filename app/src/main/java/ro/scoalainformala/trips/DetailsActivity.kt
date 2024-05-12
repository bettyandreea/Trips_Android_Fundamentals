package ro.scoalainformala.trips

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsActivity : AppCompatActivity() {
    // Location permission
    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult<String, Boolean>(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your app.
                displayCurrentUserLocation()
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their decision
                handlePermissionDenied()
            }
        }
    private var fusedLocationClient: FusedLocationProviderClient? = null

    val APP_ID: String = "95742b5d52241bd561e65e6b5ec02605"
    val WEATHER_URL: String = "https://api.openweathermap.org/"
    private var lat: String? = null
    private var lon: String? = null
    private var city: String? = null

    private var tripTitle: TextView? = null
    private var tripDestination: TextView? = null

    //private RatingBar tripRating;
    private var tripPrice: TextView? = null
    private var tripType: TextView? = null
    private var tripStartDate: TextView? = null
    private var tripEndDate: TextView? = null
    private var weather: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        tripTitle = findViewById(R.id.details_trip_name)
        tripDestination = findViewById(R.id.details_trip_destination)
        //tripRating = findViewById(R.id.details_trip_rating);
        tripPrice = findViewById(R.id.details_trip_price)
        tripType = findViewById(R.id.details_trip_type)
        tripStartDate = findViewById(R.id.details_start_date)
        tripEndDate = findViewById(R.id.details_end_date)
        weather = findViewById(R.id.details_weather_state)

        val intent = intent

        with(tripTitle) { this!!.text = intent.getStringExtra(EXTRA_TITLE_D) }
        with(tripDestination) { this!!.text = intent.getStringExtra(EXTRA_DESTINATION_D) }
        with(tripPrice) { this!!.text = intent.getStringExtra(EXTRA_PRICE_D) }
       //with(tripRating) { this!!.rating = intent.getStringExtra(EXTRA_RATING_D) }
        with(tripType) { this!!.text = intent.getStringExtra(EXTRA_TYPE_D) }
        with(tripStartDate) { this!!.text = intent.getStringExtra(EXTRA_START_DATE_D) }
        with(tripEndDate) { this!!.text = intent.getStringExtra(EXTRA_END_DATE_D) }


        // city name to get weather in
        val dest = tripDestination;
        if(dest != null)
            city = dest.getText().toString()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else if (city != null) {
            cityData
        } else {
            currentData
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    private fun displayCurrentUserLocation() {
        fusedLocationClient!!.lastLocation.addOnSuccessListener(this) { location: Location? ->
            // Got last known location. In some rare situation this can be null.
            if (location != null) {
                // Logic to handle location object
                lat = location.latitude.toString()
                lon = location.longitude.toString()
            } else {
                Toast.makeText(this, "Unknown location", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handlePermissionDenied() {
        val coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinator)

        Snackbar.make(coordinatorLayout, R.string.no_location_error, Snackbar.LENGTH_LONG).show()
    }

    private val cityData: Unit
        // get weather in given city
        get() {
            val retrofit = Retrofit.Builder()
                .baseUrl(WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(OpenWeatherService::class.java)
            val call = service.getWeatherCity(city, APP_ID)
            call.enqueue(object : Callback<WeatherResponse?> {
                override fun onResponse(
                    call: Call<WeatherResponse?>,
                    response: Response<WeatherResponse?>
                ) {
                    if (response.code() == 200) {
                        val weatherResponse = response.body()!!
                        val stringBuilder = """
                        Country: ${weatherResponse.sys.country}
                        Temperature: ${Math.round(weatherResponse.main.temp - 273.15)}°C
                        Temperature(Min): ${Math.round(weatherResponse.main.temp_min - 273.15)}°C
                        Temperature(Max): ${Math.round(weatherResponse.main.temp_max - 273.15)}°C
                        Humidity: ${Math.round(weatherResponse.main.humidity)}
                        Pressure: ${Math.round(weatherResponse.main.pressure)}
                        """.trimIndent()

                        weather!!.text = stringBuilder
                    }
                }

                override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                    weather!!.text = t.message
                }
            })
        }

    private val currentData: Unit
        // get weather in current location
        get() {
            val retrofit = Retrofit.Builder()
                .baseUrl(WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(OpenWeatherService::class.java)
            val call = service.getWeather(lat, lon, APP_ID)
            call.enqueue(object : Callback<WeatherResponse?> {
                override fun onResponse(
                    call: Call<WeatherResponse?>,
                    response: Response<WeatherResponse?>
                ) {
                    if (response.code() == 200) {
                        val weatherResponse = response.body()!!
                        val stringBuilder = """
                        Country: ${weatherResponse.sys.country}
                        Temperature: ${weatherResponse.main.temp}
                        Temperature(Min): ${weatherResponse.main.temp_min}
                        Temperature(Max): ${weatherResponse.main.temp_max}
                        Humidity: ${weatherResponse.main.humidity}
                        Pressure: ${weatherResponse.main.pressure}
                        """.trimIndent()

                        weather!!.text = stringBuilder
                    }
                }

                override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                    weather!!.text = t.message
                }
            })
        }

    companion object {
        const val EXTRA_IMAGE_D: String = "ro.scoalainformala.trips.EXTRA_IMAGE_D"
        const val EXTRA_TITLE_D: String = "ro.scoalainformala.trips.EXTRA_TITLE_D"
        const val EXTRA_DESTINATION_D: String = "ro.scoalainformala.trips.EXTRA_DESTINATION_D"
        const val EXTRA_PRICE_D: String = "ro.scoalainformala.trips.EXTRA_PRICE_D"
        const val EXTRA_RATING_D: String = "ro.scoalainformala.trips.EXTRA_RATING_D"
        const val EXTRA_TYPE_D: String = "ro.scoalainformala.trips.EXTRA_TYPE_D"
        const val EXTRA_START_DATE_D: String = "ro.scoalainformala.trips.EXTRA_START_DATE_D"
        const val EXTRA_END_DATE_D: String = "ro.scoalainformala.trips.EXTRA_END_DATE_D"
        const val EXTRA_IS_FAVOURITE_D: String = "ro.scoalainformala.trips.EXTRA_IS_FAVOURITE_D"
    }
}
