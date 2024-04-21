package ro.scoalainformala.trips.Trip

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(trip: Trip?)

    @Update
    fun update(trip: Trip?)

    @Query("DELETE FROM trip_table")
    fun deleteAll()

    @get:Query("SELECT * FROM trip_table ORDER BY title ASC")
    val alphabetizedTrips: LiveData<List<Trip?>?>?

    @get:Query("SELECT * FROM trip_table WHERE isFavourite= 1")
    val allFavouriteTrips: LiveData<List<Trip?>?>?
}
