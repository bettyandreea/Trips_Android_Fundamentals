package ro.scoalainformala.trips.Trip;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Trip trip);

    @Update
    void update(Trip trip);

    @Query("DELETE FROM trip_table")
    void deleteAll();

    @Query("SELECT * FROM trip_table ORDER BY title ASC")
    LiveData<List<Trip>> getAlphabetizedTrips();

    @Query("SELECT * FROM trip_table WHERE isFavourite='true'")
    LiveData<List<Trip>> getAllFavouriteTrips();
}
