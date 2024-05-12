package ro.scoalainformala.trips

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ro.scoalainformala.trips.Trip.Trip
import ro.scoalainformala.trips.TripListAdapter.TripViewHolder

class TripListAdapter(context: Context?) : RecyclerView.Adapter<TripViewHolder>() {
    private var listener: OnItemClickListener? = null
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mTrips: List<Trip>? = null

    interface OnItemClickListener {
        fun onItemClick(trip: Trip?)
        fun onLongItemClick(trip: Trip?)
        fun OnFavouriteItemClick(trip: Trip?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView = mInflater.inflate(R.layout.recyclerview_item_trip, parent, false)
        return TripViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        if (mTrips != null) {
            val current = mTrips!![position]
            holder.tripName.text = current.title
            Glide.with(holder.tripImage)
                .load(current.image)
                .into(holder.tripImage)
            //holder.tripImage.setImageResource(current.getImage());
            holder.destination.text = current.destination
            holder.price.text = current.price
            holder.rating.rating = current.rating
            holder.favourite.isChecked = current.isFavourite
        } else {
            holder.tripName.text = "Nothing"
            holder.tripImage.setImageResource(R.drawable.blank)
            holder.destination.text = "Nothing"
            holder.price.text = "$0"
            holder.rating.rating = 0f
            holder.favourite.isChecked = false
        }
    }

    fun setTrips(trips: List<Trip>?) {
        mTrips = trips
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mTrips != null) mTrips!!.size
        else 0
    }

    fun getTripAt(position: Int): Trip {
        return mTrips!![position]
    }

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tripImage: ImageView = itemView.findViewById(R.id.trip_image)
        val tripName: TextView = itemView.findViewById(R.id.trip_name)
        val destination: TextView = itemView.findViewById(R.id.trip_destination)
        val price: TextView = itemView.findViewById(R.id.trip_price)
        val rating: RatingBar = itemView.findViewById(R.id.trip_rating)

        /*Button*/
        val favourite: CheckBox = itemView.findViewById(R.id.trip_check_favorites)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) listener!!.onItemClick(
                    mTrips!![position]
                )
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) listener!!.onLongItemClick(
                    mTrips!![position]
                )
                false
            }

            favourite.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.OnFavouriteItemClick(mTrips!![position])
                    favourite.isChecked = mTrips!![position].isFavourite
                }
            }
        }
    }
}

