package ro.scoalainformala.trips.Trip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

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

    public LiveData<List<Trip>> getFavTrips(){
        return tripRepository.getAllFavouriteTrips();
    }

    public void insert(Trip trip) {
        tripRepository.insert(trip);
    }

    public void update(Trip trip){
        tripRepository.update(trip);
    }
}
