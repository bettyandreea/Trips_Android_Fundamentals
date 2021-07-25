package ro.scoalainformala.trips.Trip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import ro.scoalainformala.trips.OpenWeatherService;
import ro.scoalainformala.trips.RetrofitClient;
import ro.scoalainformala.trips.TripsCallback;

public class TripViewModel extends AndroidViewModel {

    private TripRepository tripRepository;
    private LiveData<List<Trip>> trips;

    public TripViewModel(@NonNull Application application){
        super(application);

        TripDao tripDao = TripDatabase.getInstance(application)
                .getTripDao();

        tripRepository = new TripRepository(tripDao);

        trips = tripRepository.getTrips();
    }

    public LiveData<List<Trip>> getTrips() {
        return trips;
    }

    public void insert(Trip trip) {
        tripRepository.insert(trip);
    }

    public void update(Trip trip){
        tripRepository.update(trip);
    }

    public void getWeather(TripsCallback callback){
        OpenWeatherService service = RetrofitClient.getRetrofitInstance().create(OpenWeatherService.class);
        service.getWeather(RetrofitClient.API_KEY).enqueue(new Callback<TripViewModel>() {
            @Override
            public void onResponse(Call<TripViewModel> call, Response<TripViewModel> response) {
                callback.onSuccess(response.body().getTrips());
            }

            @Override
            public void onFailure(Call<TripViewModel> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
