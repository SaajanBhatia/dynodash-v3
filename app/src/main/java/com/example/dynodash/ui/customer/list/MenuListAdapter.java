package com.example.dynodash.ui.customer.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynodash.R;

import java.util.ArrayList;
import java.util.List;

// Adapter to saturate the recyclerview for the Menu Items Customer Side
public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuViewHolder> {

    private List<MenuListItem> mMenuItems;
    private OnItemClickListener mListener;

    public MenuListAdapter() { mMenuItems = new ArrayList<>(); }

    public void setMenuItems(List<MenuListItem> menuItems) {
        mMenuItems = menuItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        // Increasing and Decreasing Quantity
        void onAddQuantity(MenuListItem item);
        void onSubtractQuantity(MenuListItem item);
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
        MenuListItem menuItem = mMenuItems.get(position);
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

        private Button addQuantity;
        private Button subtractQuantity;
        private TextView itemQuantity;

        private LinearLayout itemQuantityLayoutView;
        private LinearLayout adminControls;

        public MenuViewHolder(@NonNull View itemView, final MenuListAdapter.OnItemClickListener listener) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.editableMenuName);
            mDescriptionTextView = itemView.findViewById(R.id.editableMenuDescription);
            mPriceTextView = itemView.findViewById(R.id.editableMenuPrice);

            addQuantity = itemView.findViewById(R.id.btn_plus);
            subtractQuantity = itemView.findViewById(R.id.btn_minus);
            itemQuantity = itemView.findViewById(R.id.item_customer_quantity);

            // Set linear layout view to visible
            itemQuantityLayoutView = itemView.findViewById(R.id.customer_selection_linear_layout);
            itemQuantityLayoutView.setVisibility(View.VISIBLE);

            // Hide Update Delete Item (Admin Controls)
            adminControls = itemView.findViewById(R.id.update_delete_linear_layout);
            adminControls.setVisibility(View.GONE);

            // Make the card text fields non editable
            mNameTextView.setKeyListener(null);
            mDescriptionTextView.setKeyListener(null);
            mPriceTextView.setKeyListener(null);

            addQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        MenuListItem item = mMenuItems.get(position);
                        if (mListener != null) {
                            mListener.onAddQuantity(item);
                        }
                    }
                }
            });

            subtractQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        MenuListItem item = mMenuItems.get(position);
                        if (mListener != null) {
                            mListener.onSubtractQuantity(item);
                        }
                    }
                }
            });

        }

        public void bind(MenuListItem menuItem) {
            mNameTextView.setText(menuItem.getItemName());
            mDescriptionTextView.setText(menuItem.getItemDesc());
            mPriceTextView.setText("$" + String.format("%.2f", menuItem.getItemPrice()));
            itemQuantity.setText(menuItem.getQuantity().toString());

            // Disable the decrease quantity if quantity equals 0
            if (menuItem.getQuantity() == 0) {
                subtractQuantity.setEnabled(false);
            } else {
                subtractQuantity.setEnabled(true);
            }
        }
    }
}
