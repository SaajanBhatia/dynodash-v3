package com.example.dynodash.ui.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynodash.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantsViewHolder> {

    private List<RestaurantListItem> mRestaurants;
    private OnItemClickListener mListener;

    public void RestaurantsAdapter() {
        mRestaurants = new ArrayList<>();
    }

    public void setRestaurantItems(List<RestaurantListItem> restaurantList) {
        mRestaurants = restaurantList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onRestaurantClick(RestaurantListItem item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_restaurant_card_item, parent, false);
        return new RestaurantsViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {
        RestaurantListItem restaurantItem = mRestaurants.get(position);

        holder.bind(restaurantItem);
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    class RestaurantsViewHolder extends RecyclerView.ViewHolder {

        // List TextView's
        public TextView mRestaurantNameTextView;
        public TextView mRestaurantDescTextView;
        public TextView mRestaurantAddrTextView;
        public MaterialCardView mRestaurantCard;


        public RestaurantsViewHolder(@NonNull View itemView, final OnItemClickListener mListener) {
            super(itemView);
            mRestaurantNameTextView = itemView.findViewById(R.id.restaurant_customer_name);
            mRestaurantDescTextView = itemView.findViewById(R.id.restaurant_customer_description);
            mRestaurantAddrTextView = itemView.findViewById(R.id.restaurant_customer_address);

            mRestaurantCard = itemView.findViewById(R.id.restaurant_list_item_card);

            // Onclick listener for the card press
            mRestaurantCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        RestaurantListItem listItem = mRestaurants.get(position);
                        if (mListener != null) {
                            mListener.onRestaurantClick(listItem);
                        }
                    }
                }
            });
        }

        // Bind Items
        public void bind(RestaurantListItem restaurantItem) {
            mRestaurantNameTextView.setText(restaurantItem.getRestaurantName());
            mRestaurantDescTextView.setText(restaurantItem.getRestaurantDesc());
            mRestaurantAddrTextView.setText(restaurantItem.getRestaurantAddr());

        }
    }
}
