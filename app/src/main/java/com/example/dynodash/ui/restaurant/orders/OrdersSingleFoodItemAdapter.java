package com.example.dynodash.ui.restaurant.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dynodash.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrdersSingleFoodItemAdapter extends ArrayAdapter<OrdersSingleFoodItem> {
    private Context mContext;
    private int mResource;

    public OrdersSingleFoodItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<OrdersSingleFoodItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            itemView = inflater.inflate(mResource, parent, false);
        }

        OrdersSingleFoodItem item = getItem(position);

        TextView itemNameTextView = itemView.findViewById(R.id.item_name_single);
        TextView itemQuantityTextView = itemView.findViewById(R.id.item_quantity_single);
        TextView itemPriceTextView = itemView.findViewById(R.id.item_price_single);

        itemNameTextView.setText(item.itemName);
        itemQuantityTextView.setText(String.valueOf(item.itemQuantity));
        itemPriceTextView.setText(String.valueOf(item.itemPrice));

        return itemView;
    }


}
