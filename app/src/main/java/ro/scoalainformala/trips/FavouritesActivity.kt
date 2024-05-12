package ro.scoalainformala.trips

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ro.scoalainformala.trips.Trip.Trip
import ro.scoalainformala.trips.Trip.TripViewModel


class FavouritesActivity : AppCompatActivity() {
//    private var tripViewModel: TripViewModel? = null
    private val tripViewModel: TripViewModel by viewModels()  // Q: better for Kotlin?
    var clickCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        val recyclerViewFav = findViewById<RecyclerView>(R.id.recycler_view_fav)
        val adapterFav = TripListAdapter(this)

        recyclerViewFav.layoutManager = LinearLayoutManager(this)
        recyclerViewFav.adapter = adapterFav

//        tripViewModel = ViewModelProvider(this).get(TripViewModel::class.java)

        tripViewModel.favTrips.observe(this) { trips: List<Trip> ->
            adapterFav.setTrips(trips)
        }

        adapterFav.setOnItemClickListener(object : TripListAdapter.OnItemClickListener {
            override fun onItemClick(trip: Trip) {
                val intent = Intent(this@FavouritesActivity, DetailsActivity::class.java)
                // put details via intent
                intent.putExtra(DetailsActivity.EXTRA_IMAGE_D, trip.image)
                intent.putExtra(DetailsActivity.EXTRA_TITLE_D, trip.title)
                intent.putExtra(DetailsActivity.EXTRA_DESTINATION_D, trip.destination)
                intent.putExtra(DetailsActivity.EXTRA_PRICE_D, trip.price)
                intent.putExtra(DetailsActivity.EXTRA_RATING_D, trip.rating)
                intent.putExtra(DetailsActivity.EXTRA_TYPE_D, trip.type)
                intent.putExtra(DetailsActivity.EXTRA_START_DATE_D, trip.startDate)
                intent.putExtra(DetailsActivity.EXTRA_END_DATE_D, trip.endDate)
                intent.putExtra(DetailsActivity.EXTRA_IS_FAVOURITE_D, trip.isFavourite)
                startActivity(intent)
            }

            override fun onLongItemClick(trip: Trip) {
                val intent = Intent(this@FavouritesActivity, AddEditTripActivity::class.java)
                intent.putExtra(AddEditTripActivity.EXTRA_ID, trip.id)
                intent.putExtra(AddEditTripActivity.EXTRA_TITLE, trip.title)
                intent.putExtra(AddEditTripActivity.EXTRA_IMAGE, trip.image)
                intent.putExtra(AddEditTripActivity.EXTRA_DESTINATION, trip.destination)
                intent.putExtra(AddEditTripActivity.EXTRA_PRICE, trip.price)
                intent.putExtra(AddEditTripActivity.EXTRA_TYPE, trip.type)
                intent.putExtra(AddEditTripActivity.EXTRA_RATING, trip.rating)
                intent.putExtra(AddEditTripActivity.EXTRA_START_DATE, trip.startDate)
                intent.putExtra(AddEditTripActivity.EXTRA_END_DATE, trip.endDate)
                intent.putExtra(AddEditTripActivity.EXTRA_IS_FAVOURITE, trip.isFavourite)
                startActivityForResult(intent, HomeActivity.EDIT_TRIP_ACTIVITY_REQUEST_CODE)
            }

            override fun OnFavouriteItemClick(trip: Trip) {
                clickCount++
                if (clickCount % 2 == 0) {
                    trip.isFavourite = false
                } else {
                    trip.isFavourite = true
                }
                tripViewModel!!.update(trip)
                Toast.makeText(this@FavouritesActivity, "Trip updated", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
