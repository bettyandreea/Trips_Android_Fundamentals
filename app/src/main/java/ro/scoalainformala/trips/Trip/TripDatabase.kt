package ro.scoalainformala.trips.Trip

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ro.scoalainformala.trips.R
import java.util.concurrent.Executors
import kotlin.concurrent.Volatile

@Database(entities = [Trip::class], version = 6, exportSchema = false)
abstract class TripDatabase : RoomDatabase() {
    abstract val tripDao: TripDao

    companion object {
        @Volatile
        private var INSTANCE: TripDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        val EXECUTOR = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        fun getInstance(context: Context?): TripDatabase? {
            if (INSTANCE == null) {
                synchronized(TripDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context!!, TripDatabase::class.java, "trip_database")
                                .addCallback(ROOM_DATABASE_CALLBACK)
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        private val ROOM_DATABASE_CALLBACK: Callback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                EXECUTOR.execute {
                    val dao = INSTANCE!!.tripDao
                    dao.deleteAll()
                    var trip = Trip("Trip to Maldives", R.drawable.maldives_trip, "Male", "$1500", 4.5f, "Sea Side", "24.07.2021", "31.07.2021", true)
                    dao.insert(trip)
                    trip = Trip("Meet Disneyland", R.drawable.disneyland_trip, "Paris", "$2000", 5f, "City Break", "12.05.2022", "17.05.2022", false)
                    dao.insert(trip)
                    trip = Trip("Paradise of Mykonos", R.drawable.mykonos_trip, "Mykonos", "$1450", 4.5f, "Sea Side", "20.08.2021", "28.08.2021", false)
                    dao.insert(trip)
                    trip = Trip("Shopping at Milan", R.drawable.milan_trip, "Milan", "$1500", 4.0f, "City Break", "13.10.2021", "15.10.2021", false)
                    dao.insert(trip)
                    trip = Trip("Big Apple - New York City", R.drawable.new_york_trip, "New York City", "$3000", 5f, "City Break", "23.07.2021", "05.08.2021", true)
                    dao.insert(trip)
                    trip = Trip("Tour of London", R.drawable.london_trip, "London", "$1000", 3.5f, "City Break", "12.08.2021", "20.08.2021", false)
                    dao.insert(trip)
                }
            }
        }
    }
}
