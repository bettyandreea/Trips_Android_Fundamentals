package ro.scoalainformala.trips;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ro.scoalainformala.trips.Trip.Trip;
import ro.scoalainformala.trips.Trip.TripViewModel;

public class FavouritesActivity extends AppCompatActivity {

    public List<Trip> favTrips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
    }
}
