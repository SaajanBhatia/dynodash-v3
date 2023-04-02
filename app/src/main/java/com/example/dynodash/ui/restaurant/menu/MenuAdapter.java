package com.example.dynodash.ui.restaurant.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynodash.R;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuRestaurantItem> mMenuItems;

    public MenuAdapter() {
        mMenuItems = new ArrayList<>();
    }

    public void setMenuItems(List<MenuRestaurantItem> menuItems) {
        mMenuItems = menuItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_menu_list_item, parent, false);
        return new MenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuRestaurantItem menuItem = mMenuItems.get(position);
        holder.bind(menuItem);
    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;
        private TextView mDescriptionTextView;
        private TextView mPriceTextView;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.editableMenuName);
            mDescriptionTextView = itemView.findViewById(R.id.editableMenuDescription);
            mPriceTextView = itemView.findViewById(R.id.editableMenuPrice);
        }

        public void bind(MenuRestaurantItem menuItem) {
            mNameTextView.setText(menuItem.getName());
            mDescriptionTextView.setText(menuItem.getDescription());
            mPriceTextView.setText("$" + String.format("%.2f", menuItem.getPrice()));
        }
    }
}
