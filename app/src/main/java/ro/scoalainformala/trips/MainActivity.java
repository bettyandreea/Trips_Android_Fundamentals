package ro.scoalainformala.trips;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import cz.msebera.android.httpclient.protocol.HTTP;

public class MainActivity extends AppCompatActivity {

    // Location permission
    @SuppressLint("MissingPermission")
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted) {
                    // Permission is granted. Continue the action or workflow in your app.
                    displayCurrentUserLocation();
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their decision
                    handlePermissionDenied();
                }
            });
    private FusedLocationProviderClient fusedLocationClient;

    // Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_about_us:
                        Intent about = new Intent(MainActivity.this, AboutUs.class);
                        startActivity(about);
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            // We already have location
            displayCurrentUserLocation();
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    private void displayCurrentUserLocation(){
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            // Got last known location. In some rare situation this can be null.
            TextView locationText = findViewById(R.id.location);
            if(location != null){
                // Logic to handle location object
                locationText.setText(location.toString());
            } else{
                locationText.setText("IDK");
            }
        });
    }

    private void handlePermissionDenied(){
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);

        Snackbar.make(coordinatorLayout, R.string.no_location_error, Snackbar.LENGTH_LONG).show();
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