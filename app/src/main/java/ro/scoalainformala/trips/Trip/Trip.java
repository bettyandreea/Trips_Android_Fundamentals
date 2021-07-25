package ro.scoalainformala.trips.Trip;

import android.nfc.tech.NfcA;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trip_table")
public class Trip {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title = "";

    private int image;

    @ColumnInfo(name = "destination")
    private String destination;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "rating")
    private float rating;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "start_date")
    private String startDate;

    @ColumnInfo(name = "end_date")
    private String endDate;

    private Boolean isFavourite;

    public Trip(String title, int image, String destination, String price, float rating, String type, String startDate, String endDate, Boolean isFavourite) {
        this.title = title;
        this.image = image;
        this.destination = destination;
        this.price = price;
        this.rating = rating;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isFavourite = isFavourite;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDestination() {
        return destination;
    }

    public String getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public String getType() {
        return type;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Boolean isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public int getImage() {
        return image;
    }
}
