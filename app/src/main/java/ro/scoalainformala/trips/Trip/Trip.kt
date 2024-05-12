package ro.scoalainformala.trips.Trip

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_table")
class Trip(title: String, image: Int, destination: String, price: String, rating: Float, type: String, startDate: String, endDate: String, isFavourite: Boolean) {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @JvmField
    @ColumnInfo(name = "title")
    var title = ""
    @JvmField
    val image: Int

    @JvmField
    @ColumnInfo(name = "destination")
    val destination: String

    @JvmField
    @ColumnInfo(name = "price")
    val price: String

    @JvmField
    @ColumnInfo(name = "rating")
    val rating: Float

    @JvmField
    @ColumnInfo(name = "type")
    val type: String

    @JvmField
    @ColumnInfo(name = "start_date")
    val startDate: String

    @JvmField
    @ColumnInfo(name = "end_date")
    val endDate: String

    @JvmField
    @ColumnInfo(name = "isFavourite")
    var isFavourite: Boolean

    init {
        this.title = title
        this.image = image
        this.destination = destination
        this.price = price
        this.rating = rating
        this.type = type
        this.startDate = startDate
        this.endDate = endDate
        this.isFavourite = isFavourite
    }
}
