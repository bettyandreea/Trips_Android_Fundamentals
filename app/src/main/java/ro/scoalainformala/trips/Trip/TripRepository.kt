package ro.scoalainformala.trips.Trip

import androidx.lifecycle.LiveData

class TripRepository(private val tripDao: TripDao) {
    val trips: LiveData<List<Trip>>
        get() = tripDao.alphabetizedTrips

    fun insert(trip: Trip) {
        TripDatabase.Companion.EXECUTOR.execute(Runnable { tripDao.insert(trip) })
    }

    fun update(trip: Trip) {
        TripDatabase.Companion.EXECUTOR.execute(Runnable { tripDao.update(trip) })
    }

    val allFavouriteTrips: LiveData<List<Trip>>
        get() = tripDao.allFavouriteTrips
}
