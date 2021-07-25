package ro.scoalainformala.trips;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ro.scoalainformala.trips.Trip.Trip;
import ro.scoalainformala.trips.Trip.TripViewModel;

public class FavouritesActivity extends AppCompatActivity {

    private TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        RecyclerView recyclerViewFav = findViewById(R.id.recycler_view_fav);
        TripListAdapter adapterFav = new TripListAdapter(this);

        recyclerViewFav.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFav.setAdapter(adapterFav);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);

        tripViewModel.getFavTrips().observe(this, trips -> {
            adapterFav.setTrips(trips);
        });
    }
}
