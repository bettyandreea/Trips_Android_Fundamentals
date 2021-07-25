package ro.scoalainformala.trips;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ro.scoalainformala.trips.Trip.TripViewModel;

public interface OpenWeatherService {

    @GET("data/2.5/weather?q={city name}&appid={API key}")
    Call<TripViewModel> getWeather(@Query("api_key") String apiKey);
}
