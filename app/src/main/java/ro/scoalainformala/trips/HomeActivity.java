package ro.scoalainformala.trips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import cz.msebera.android.httpclient.protocol.HTTP;
import ro.scoalainformala.trips.R.drawable;
import ro.scoalainformala.trips.Trip.Trip;
import ro.scoalainformala.trips.Trip.TripViewModel;

import static ro.scoalainformala.trips.R.*;
import static ro.scoalainformala.trips.R.drawable.*;

public class HomeActivity extends AppCompatActivity{

    public static final int NEW_TRIP_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_TRIP_ACTIVITY_REQUEST_CODE = 2;

    // Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private TripViewModel tripViewModel;

    private Button favButton;
    private CheckBox setFavTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_home);

        // Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Header layout
        View headerView = navigationView.getHeaderView(0);
        ImageView headerImage = headerView.findViewById(R.id.app_img);
        TextView headerName = headerView.findViewById(R.id.user_name);
        TextView headerMail = headerView.findViewById(R.id.user_email);
        headerImage.setImageResource(R.drawable.ic_header_travel);
        headerName.setText("Beatrice Vizuroiu");
        headerMail.setText("b.vizuroiu@student.tudelft.nl");

        // Navigation Drawer Menu
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Navigation View
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_about_us:
                        loadFragment(new aboutFragment());
//                        Toast.makeText(MainActivity.this, "About Us", Toast.LENGTH_LONG);
                        break;

                    case R.id.nav_contact:
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jan@example.com"}); // recipients
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
                        startActivity(emailIntent);
                        //Toast.makeText(MainActivity.this, "Contact", Toast.LENGTH_LONG);
                        break;

                    case R.id.nav_share:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my app to send.");
                        sendIntent.setType("text/plain");
                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                        //Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_LONG);
                        break;

                    default:
                        break;
                }
                Log.d("MainActivity", "onNavigationItemSelected: " + menuItem.getTitle());
                return false;
            }
        });

        setFavTrip = findViewById(R.id.trip_check_favorites);

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
                intent.putExtra(DetailsActivity.EXTRA_IS_FAVOURITE_D, trip.isFavourite());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(Trip trip) {
                Intent intent = new Intent(HomeActivity.this, AddEditTripActivity.class);
                intent.putExtra(AddEditTripActivity.EXTRA_ID, trip.getId());
                intent.putExtra(AddEditTripActivity.EXTRA_TITLE, trip.getTitle());
                intent.putExtra(AddEditTripActivity.EXTRA_IMAGE, trip.getImage());
                intent.putExtra(AddEditTripActivity.EXTRA_DESTINATION, trip.getDestination());
                intent.putExtra(AddEditTripActivity.EXTRA_PRICE, trip.getPrice());
                intent.putExtra(AddEditTripActivity.EXTRA_TYPE, trip.getType());
                intent.putExtra(AddEditTripActivity.EXTRA_RATING, trip.getRating());
                intent.putExtra(AddEditTripActivity.EXTRA_START_DATE, trip.getStartDate());
                intent.putExtra(AddEditTripActivity.EXTRA_END_DATE, trip.getEndDate());
                intent.putExtra(AddEditTripActivity.EXTRA_IS_FAVOURITE, trip.isFavourite());
                startActivityForResult(intent, EDIT_TRIP_ACTIVITY_REQUEST_CODE);
            }

            @Override
            public void OnFavouriteItemClick(Trip trip){
                Trip updatedTrip = trip;
                updatedTrip.setIsFavourite(true);
                tripViewModel.update(trip);
                Toast.makeText(HomeActivity.this, "Trip updated", Toast.LENGTH_SHORT).show();
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

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id.frame, fragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_TRIP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            // String title, int image, String destination, String price, float rating, String type, String startDate, String endDate, Boolean isFavourite
            String title = data.getStringExtra(AddEditTripActivity.EXTRA_TITLE);
            String destination = data.getStringExtra(AddEditTripActivity.EXTRA_DESTINATION);
            int image = data.getIntExtra(AddEditTripActivity.EXTRA_IMAGE, R.drawable.blank);
            String price = data.getStringExtra(AddEditTripActivity.EXTRA_PRICE);
            float rating = Float.parseFloat(data.getStringExtra(AddEditTripActivity.EXTRA_RATING));
            String type = data.getStringExtra(AddEditTripActivity.EXTRA_TYPE);
            String startDate = data.getStringExtra(AddEditTripActivity.EXTRA_START_DATE);
            String endDate = data.getStringExtra(AddEditTripActivity.EXTRA_END_DATE);
            Boolean isFavorite = Boolean.parseBoolean(data.getStringExtra(AddEditTripActivity.EXTRA_IS_FAVOURITE));
            Trip trip = new Trip(title, image, destination, price, rating, type, startDate, endDate, isFavorite);

            tripViewModel.insert(trip);
            Toast.makeText(this, "Trip saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_TRIP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditTripActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Trip can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditTripActivity.EXTRA_TITLE);
            String destination = data.getStringExtra(AddEditTripActivity.EXTRA_DESTINATION);
            int image = data.getIntExtra(AddEditTripActivity.EXTRA_IMAGE, R.drawable.blank);
            String price = data.getStringExtra(AddEditTripActivity.EXTRA_PRICE);
            float rating = Float.parseFloat(data.getStringExtra(AddEditTripActivity.EXTRA_RATING));
            String type = data.getStringExtra(AddEditTripActivity.EXTRA_TYPE);
            String startDate = data.getStringExtra(AddEditTripActivity.EXTRA_START_DATE);
            String endDate = data.getStringExtra(AddEditTripActivity.EXTRA_END_DATE);
            Boolean isFavorite = Boolean.parseBoolean(data.getStringExtra(AddEditTripActivity.EXTRA_IS_FAVOURITE));
            Trip trip = new Trip(title, image, destination, price, rating, type, startDate, endDate, isFavorite);
            trip.setId(id);
            tripViewModel.update(trip);
            Toast.makeText(this, "Trip updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("MainActivity", "onOptionsItemSelected: " + item);
        return toggle.onOptionsItemSelected(item);
    }
}
