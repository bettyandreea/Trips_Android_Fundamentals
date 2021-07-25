package ro.scoalainformala.trips;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherService {

    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String APP_ID);

    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeatherCity(@Query("q") String city, @Query("appid") String APP_ID);
}
