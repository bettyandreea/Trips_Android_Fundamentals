package ro.scoalainformala.trips;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import ro.scoalainformala.trips.Trip.Trip;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {

    private OnItemClickListener listener;
    private final LayoutInflater mInflater;
    private List<Trip> mTrips;

    public interface OnItemClickListener {
        void onItemClick(Trip trip);
        void onLongItemClick(Trip trip);
        void OnFavouriteItemClick(Trip trip);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TripListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TripListAdapter.TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item_trip, parent, false);
        return new TripListAdapter.TripViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TripListAdapter.TripViewHolder holder, int position) {
        if (mTrips != null) {
            Trip current = mTrips.get(position);
            holder.tripName.setText(current.title);
            Glide.with(holder.tripImage)
                    .load(current.image)
                    .into(holder.tripImage);
            //holder.tripImage.setImageResource(current.getImage());
            holder.destination.setText(current.destination);
            holder.price.setText(current.price);
            holder.rating.setRating(current.rating);
            holder.favourite.setChecked(current.isFavourite);
        } else {
            holder.tripName.setText("Nothing");
            holder.tripImage.setImageResource(R.drawable.blank);
            holder.destination.setText("Nothing");
            holder.price.setText("$0");
            holder.rating.setRating(0F);
            holder.favourite.setChecked(false);
        }
    }

    void setTrips(List<Trip> trips) {
        mTrips = trips;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTrips != null)
            return mTrips.size();
        else return 0;
    }

    public Trip getTripAt(int position) {
        return mTrips.get(position);
    }

    class TripViewHolder extends RecyclerView.ViewHolder {
        private final ImageView tripImage;
        private final TextView tripName;
        private final TextView destination;
        private final TextView price;
        private final RatingBar rating;
        private final CheckBox favourite;

        private TripViewHolder(View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.trip_name);
            tripImage = itemView.findViewById(R.id.trip_image);
            destination = itemView.findViewById(R.id.trip_destination);
            price = itemView.findViewById(R.id.trip_price);
            rating = itemView.findViewById(R.id.trip_rating);
            /*Button*/ favourite = itemView.findViewById(R.id.trip_check_favorites);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(mTrips.get(position));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                        listener.onLongItemClick(mTrips.get(position));
                    return false;
                }
            });

            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.OnFavouriteItemClick(mTrips.get(position));
                        favourite.setChecked(mTrips.get(position).isFavourite);
                    }
                }
            });
        }
    }
}

