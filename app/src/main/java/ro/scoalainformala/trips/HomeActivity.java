package ro.scoalainformala.trips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ro.scoalainformala.trips.R.drawable;
import ro.scoalainformala.trips.Trip.Trip;
import ro.scoalainformala.trips.Trip.TripViewModel;

import static ro.scoalainformala.trips.R.*;
import static ro.scoalainformala.trips.R.drawable.*;

public class HomeActivity extends AppCompatActivity{

    public static final int NEW_TRIP_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_TRIP_ACTIVITY_REQUEST_CODE = 2;

    private TripViewModel tripViewModel;

    private ImageView tripImage;
    private TextView tripTitle;
    private TextView tripDestination;
    private RatingBar tripRating;
    private TextView tripPrice;
    private TextView tripType;
    private TextView tripStartDate;
    private TextView tripEndDate;
    private Button favButton;
    private Button setFavTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_home);

//        tripTitle = findViewById(R.id.details_trip_name);
//        tripDestination = findViewById(R.id.details_trip_destination);
//        tripRating = findViewById(R.id.details_trip_rating);
//        tripPrice = findViewById(R.id.details_trip_price);
//        tripType = findViewById(R.id.details_trip_type);
//        tripStartDate = findViewById(R.id.details_start_date);
//        tripEndDate = findViewById(R.id.details_end_date);

        setFavTrip = findViewById(R.id.trip_check_favorites);
//        setFavTrip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // add trip to favTrips list
//            }
//        });

        RecyclerView recyclerView = findViewById(id.recycler_view);
        TripListAdapter adapter = new TripListAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);

        tripViewModel.getTrips().observe(this, trips -> {
            adapter.setTrips(trips);
        });

        adapter.setOnItemClickListener(new TripListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Trip trip) {
                Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);
                // put details via intent
                intent.putExtra(DetailsActivity.EXTRA_IMAGE_D, trip.getImage());
                intent.putExtra(DetailsActivity.EXTRA_TITLE_D, trip.getTitle());
                intent.putExtra(DetailsActivity.EXTRA_DESTINATION_D, trip.getDestination());
                intent.putExtra(DetailsActivity.EXTRA_PRICE_D, trip.getPrice());
                intent.putExtra(DetailsActivity.EXTRA_RATING_D, trip.getRating());
                intent.putExtra(DetailsActivity.EXTRA_TYPE_D, trip.getType());
                intent.putExtra(DetailsActivity.EXTRA_START_DATE_D, trip.getStartDate());
                intent.putExtra(DetailsActivity.EXTRA_END_DATE_D, trip.getEndDate());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(Trip trip) {
                Intent intent = new Intent(HomeActivity.this, AddEditTripActivity.class);
                intent.putExtra(AddEditTripActivity.EXTRA_IMAGE, trip.getImage());
                intent.putExtra(AddEditTripActivity.EXTRA_TITLE, trip.getTitle());
                intent.putExtra(AddEditTripActivity.EXTRA_DESTINATION, trip.getDestination());
                intent.putExtra(AddEditTripActivity.EXTRA_PRICE, trip.getPrice());
                intent.putExtra(AddEditTripActivity.EXTRA_RATING, trip.getRating());
                intent.putExtra(AddEditTripActivity.EXTRA_TYPE, trip.getType());
                intent.putExtra(AddEditTripActivity.EXTRA_START_DATE, trip.getStartDate());
                intent.putExtra(AddEditTripActivity.EXTRA_END_DATE, trip.getEndDate());
                startActivityForResult(intent, EDIT_TRIP_ACTIVITY_REQUEST_CODE);
            }
        });

        FloatingActionButton floatingActionButton = findViewById(id.button_add_trip);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddEditTripActivity.class);
                startActivityForResult(intent, NEW_TRIP_ACTIVITY_REQUEST_CODE);
            }
        });

        favButton = findViewById(R.id.button_favourites);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FavouritesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_TRIP_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            int image = data.getIntExtra(AddEditTripActivity.EXTRA_IMAGE, blank);
            String title = data.getStringExtra(AddEditTripActivity.EXTRA_TITLE);
            String destination = data.getStringExtra(AddEditTripActivity.EXTRA_DESTINATION);
            String price = data.getStringExtra(AddEditTripActivity.EXTRA_PRICE);
            float rating = Float.parseFloat(data.getStringExtra(AddEditTripActivity.EXTRA_RATING));
            String type = data.getStringExtra(AddEditTripActivity.EXTRA_TYPE);
            String startDate = data.getStringExtra(AddEditTripActivity.EXTRA_START_DATE);
            String endDate = data.getStringExtra(AddEditTripActivity.EXTRA_END_DATE);
            Trip trip = new Trip(title, image, destination, price, rating, type, startDate, endDate, false);
            tripViewModel.insert(trip);

            Toast.makeText(this, "Trip saved", Toast.LENGTH_LONG).show();

        } else if (requestCode == EDIT_TRIP_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            int id = data.getIntExtra(AddEditTripActivity.EXTRA_ID, -1);
            if(id == -1){
                Toast.makeText(this, "Trip can't be updated", Toast.LENGTH_LONG).show();
                return;
            }

            String title = data.getStringExtra(AddEditTripActivity.EXTRA_TITLE);
            int image = data.getIntExtra(AddEditTripActivity.EXTRA_IMAGE, blank);
            String destination = data.getStringExtra(AddEditTripActivity.EXTRA_DESTINATION);
            String price = data.getStringExtra(AddEditTripActivity.EXTRA_PRICE);
            float rating = Float.parseFloat(data.getStringExtra(AddEditTripActivity.EXTRA_RATING));
            String type = data.getStringExtra(AddEditTripActivity.EXTRA_TYPE);
            String startDate = data.getStringExtra(AddEditTripActivity.EXTRA_START_DATE);
            String endDate = data.getStringExtra(AddEditTripActivity.EXTRA_END_DATE);
            Trip trip = new Trip(title, image, destination, price, rating, type, startDate, endDate, false);
            trip.setId(id);
            tripViewModel.update(trip);

            Toast.makeText(this, "Trip updated", Toast.LENGTH_LONG).show();

        } else{
            Snackbar.make(findViewById(id.root), string.empty_not_saved, Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

}
