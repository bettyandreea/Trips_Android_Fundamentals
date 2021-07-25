package ro.scoalainformala.trips;

import androidx.lifecycle.LiveData;

import java.util.List;

import ro.scoalainformala.trips.Trip.Trip;

public interface TripsCallback {
    void onSuccess(LiveData<List<Trip>> trips);
    void onFailure(Throwable throwable);
}
