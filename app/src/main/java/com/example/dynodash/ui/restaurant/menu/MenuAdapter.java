package com.example.dynodash.ui.restaurant.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynodash.R;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuRestaurantItem> mMenuItems;
    private OnItemClickListener mListener;

    public MenuAdapter() {
        mMenuItems = new ArrayList<>();
    }

    public void setMenuItems(List<MenuRestaurantItem> menuItems) {
        mMenuItems = menuItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onDeleteClick(MenuRestaurantItem item);
        void onUpdateClick(MenuRestaurantItem item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_menu_list_item, parent, false);
        return new MenuViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        // Get the current menu item
        MenuRestaurantItem menuItem = mMenuItems.get(position);

        // Set the text for the TextViews in the ViewHolder
        holder.bind(menuItem);
    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mDescriptionTextView;
        private TextView mPriceTextView;

        private Button mUpdateButton;
        private Button mDeleteButton;

        public MenuViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.editableMenuName);
            mDescriptionTextView = itemView.findViewById(R.id.editableMenuDescription);
            mPriceTextView = itemView.findViewById(R.id.editableMenuPrice);

            mUpdateButton = itemView.findViewById(R.id.btn_update);
            mDeleteButton = itemView.findViewById(R.id.btn_delete);

            mUpdateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        MenuRestaurantItem item = mMenuItems.get(position);
                        if (mListener != null) {
                            // Update object values
                            String updatedName = mNameTextView.getText().toString();
                            String updatedDescription  = mDescriptionTextView.getText().toString();
                            Double updatedPrice  = Double.parseDouble(mPriceTextView.getText().toString().replace("$", ""));

                            // Sanitization Checks
                            if (updatedName.isEmpty()) {
                                mNameTextView.setError("Name cannot be empty");
                                return;
                            }
                            if (updatedDescription.isEmpty()) {
                                mDescriptionTextView.setError("Description cannot be empty");
                                return;
                            }
                            if (String.valueOf(updatedPrice).isEmpty()) {
                                mPriceTextView.setError("Price cannot be empty");
                                return;
                            }

                            // Check if price matches correct format
                            if (!String.valueOf(updatedPrice).matches("\\d+(\\.\\d{1,2})?")) {
                                mPriceTextView.setError("Invalid price format");
                                return;
                            }

                            // Update the item's values
                            item.setName(updatedName);
                            item.setDescription(updatedDescription);
                            item.setPrice(updatedPrice);

                            mListener.onUpdateClick(item);
                        }
                    }
                }
            });

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        MenuRestaurantItem item = mMenuItems.get(position);
                        if (mListener != null) {
                            mListener.onDeleteClick(item);
                        }
                    }
                }
            });
        }

        public void bind(MenuRestaurantItem menuItem) {
            mNameTextView.setText(menuItem.getName());
            mDescriptionTextView.setText(menuItem.getDescription());
            mPriceTextView.setText("$" + String.format("%.2f", menuItem.getPrice()));
        }
    }
}