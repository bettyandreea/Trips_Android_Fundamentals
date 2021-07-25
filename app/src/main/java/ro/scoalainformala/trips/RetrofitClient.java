package ro.scoalainformala.trips;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit RETROFIT;
    public static String BASE_URL = "https://api.openweathermap.org/";

    public static String API_KEY = "95742b5d52241bd561e65e6b5ec02605";

    public static Retrofit getRetrofitInstance(){
        if(RETROFIT == null){
            RETROFIT = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return RETROFIT;
    }
}
