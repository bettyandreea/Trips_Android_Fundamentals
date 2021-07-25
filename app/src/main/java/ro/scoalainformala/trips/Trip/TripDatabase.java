package ro.scoalainformala.trips.Trip;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ro.scoalainformala.trips.R;

@Database(entities = {Trip.class}, version = 6, exportSchema = false)
public abstract class TripDatabase extends RoomDatabase {

    private static volatile TripDatabase INSTANCE;
    abstract TripDao getTripDao();

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService EXECUTOR =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TripDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (TripDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, TripDatabase.class, "trip_database")
                            .addCallback(ROOM_DATABASE_CALLBACK)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback ROOM_DATABASE_CALLBACK = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            EXECUTOR.execute(() -> {
                TripDao dao = INSTANCE.getTripDao();
                dao.deleteAll();

                Trip trip = new Trip("Trip to Maldives", R.drawable.maldives_trip, "Maldives", "$1500", 8.5F, "Sea Side", "24.07.2021", "31.07.2021", true);
                dao.insert(trip);
                trip = new Trip("Meet Disneyland", R.drawable.disneyland_trip, "Paris", "$2000", 9.5F, "City Break", "12.05.2022", "17.05.2022", false);
                dao.insert(trip);
                trip = new Trip("Paradise of Mykonos", R.drawable.mykonos_trip, "Mykonos", "$1450", 8.5F, "Sea Side", "20.08.2021", "28.08.2021", false);
                dao.insert(trip);
                trip = new Trip("Shopping at Milan", R.drawable.milan_trip, "Milan", "$1500", 8.0F, "City Break", "13.10.2021", "15.10.2021", false);
                dao.insert(trip);
                trip = new Trip("Big Apple - New York City", R.drawable.new_york_trip, "New York City", "$3000", 9.5F, "City Break", "23.07.2021", "05.08.2021", true);
                dao.insert(trip);
                trip = new Trip("Tour of London", R.drawable.london_trip, "London", "$1000", 7.5F, "City Break", "12.08.2021", "20.08.2021", false);
                dao.insert(trip);
            });
        }
    };
}
