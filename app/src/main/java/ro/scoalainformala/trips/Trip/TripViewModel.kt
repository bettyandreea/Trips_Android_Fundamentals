package ro.scoalainformala.trips.Trip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class TripViewModel(application: Application) : AndroidViewModel(application) {
    private val tripRepository: TripRepository
    @JvmField
    val trips: LiveData<List<Trip?>?>?
    init {
        val tripDao : TripDao = TripDatabase.getInstance(application)!!.tripDao
        tripRepository = TripRepository(tripDao)
        trips = tripRepository.trips
    }

    val favTrips: LiveData<List<Trip?>?>?
        get() = tripRepository.allFavouriteTrips

    fun insert(trip: Trip?) {
        tripRepository.insert(trip)
    }

    fun update(trip: Trip?) {
        tripRepository.update(trip)
    }
}
