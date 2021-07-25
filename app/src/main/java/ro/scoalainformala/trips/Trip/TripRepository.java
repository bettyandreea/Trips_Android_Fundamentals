package ro.scoalainformala.trips.Trip;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TripRepository {

    private TripDao tripDao;

    public TripRepository(TripDao tripDao){
        this.tripDao = tripDao;
    }

    LiveData<List<Trip>> getTrips(){
        return tripDao.getAlphabetizedTrips();
    }

    public void insert(Trip trip){
        TripDatabase.EXECUTOR.execute(() -> tripDao.insert(trip));
    }

    public void update(Trip trip){
        TripDatabase.EXECUTOR.execute(() -> tripDao.update(trip));
    }

    public LiveData<List<Trip>> getAllFavouriteTrips(){return tripDao.getAllFavouriteTrips();}

//    public void update(Trip trip) {
//        new UpdateTripAsyncTask(tripDao).execute(trip);
//    }
//
//    private static class UpdateTripAsyncTask extends AsyncTask<Trip, Void, Void> {
//        private TripDao tripDao;
//
//        private UpdateTripAsyncTask(TripDao tripDao) {
//            this.tripDao = tripDao;
//        }
//
//        @Override
//        protected Void doInBackground(Trip... trips) {
//            tripDao.update(trips[0]);
//            return null;
//        }
//    }
}
