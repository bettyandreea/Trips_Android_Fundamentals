package ro.scoalainformala.trips;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
    int clickCount = 0;

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

        adapterFav.setOnItemClickListener(new TripListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Trip trip) {
                Intent intent = new Intent(FavouritesActivity.this, DetailsActivity.class);
                // put details via intent
                intent.putExtra(DetailsActivity.EXTRA_IMAGE_D, trip.image);
                intent.putExtra(DetailsActivity.EXTRA_TITLE_D, trip.title);
                intent.putExtra(DetailsActivity.EXTRA_DESTINATION_D, trip.destination);
                intent.putExtra(DetailsActivity.EXTRA_PRICE_D, trip.price);
                intent.putExtra(DetailsActivity.EXTRA_RATING_D, trip.rating);
                intent.putExtra(DetailsActivity.EXTRA_TYPE_D, trip.type);
                intent.putExtra(DetailsActivity.EXTRA_START_DATE_D, trip.startDate);
                intent.putExtra(DetailsActivity.EXTRA_END_DATE_D, trip.endDate);
                intent.putExtra(DetailsActivity.EXTRA_IS_FAVOURITE_D, trip.isFavourite);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(Trip trip) {
                Intent intent = new Intent(FavouritesActivity.this, AddEditTripActivity.class);
                intent.putExtra(AddEditTripActivity.EXTRA_ID, trip.id);
                intent.putExtra(AddEditTripActivity.EXTRA_TITLE, trip.title);
                intent.putExtra(AddEditTripActivity.EXTRA_IMAGE, trip.image);
                intent.putExtra(AddEditTripActivity.EXTRA_DESTINATION, trip.destination);
                intent.putExtra(AddEditTripActivity.EXTRA_PRICE, trip.price);
                intent.putExtra(AddEditTripActivity.EXTRA_TYPE, trip.type);
                intent.putExtra(AddEditTripActivity.EXTRA_RATING, trip.rating);
                intent.putExtra(AddEditTripActivity.EXTRA_START_DATE, trip.startDate);
                intent.putExtra(AddEditTripActivity.EXTRA_END_DATE, trip.endDate);
                intent.putExtra(AddEditTripActivity.EXTRA_IS_FAVOURITE, trip.isFavourite);
                startActivityForResult(intent, HomeActivity.EDIT_TRIP_ACTIVITY_REQUEST_CODE);
            }

            @Override
            public void OnFavouriteItemClick(Trip trip){
                clickCount++;
                if (clickCount % 2 == 0){
                    trip.isFavourite = false;
                } else {
                    trip.isFavourite = true;
                }
                tripViewModel.update(trip);
                Toast.makeText(FavouritesActivity.this, "Trip updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
