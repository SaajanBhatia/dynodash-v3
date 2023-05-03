package com.example.dynodash.ui.restaurant.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynodash.R;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private List<OrdersListItem> mOrders;
    private OnItemClickListener mListener;

    public OrdersAdapter() { mOrders = new ArrayList<>(); }

    public void setOrderItems(List<OrdersListItem> orderItems) {
        mOrders = orderItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onCompleteOrder(OrdersListItem item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {mListener = listener;}


    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_orders_list_item, parent, false);
        return new OrdersViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        // Get current order item
        OrdersListItem ordersItem = mOrders.get(position);

        // Set the text for the TextViews within the ViewHolder
        holder.bind(ordersItem);
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    class OrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView mOrderNameTextView;
        public ListView mOrderItemsListView;
        public TextView mOrderPriceTextView;
        public TextView mOrderIdTextView;
        public TextView mTableNumberTextView;
        public Button mCompleteOrderButton;

        public OrdersViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mOrderNameTextView = itemView.findViewById(R.id.orderNameTextView);
            mOrderItemsListView = itemView.findViewById(R.id.orderItemsListView);
            mOrderPriceTextView = itemView.findViewById(R.id.orderPriceTextView);
            mOrderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            mTableNumberTextView = itemView.findViewById(R.id.tableNumberTextView);
            mCompleteOrderButton = itemView.findViewById(R.id.completeOrderButton);

            mCompleteOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        OrdersListItem orderItem = mOrders.get(position);
                        if (mListener != null) {
                            mListener.onCompleteOrder(orderItem);
                        }
                    }
                }
            });
        }

        public void bind(OrdersListItem ordersItem) {
            mOrderNameTextView.setText(ordersItem.getCustomerName());
            mOrderPriceTextView.setText(String.valueOf(ordersItem.getOrderTotal()));
            mOrderIdTextView.setText("Order ID: " + ordersItem.getOrderID());
            mTableNumberTextView.setText("Table: " + String.valueOf(ordersItem.getTableNumber()));

            // Bind List Order Items
            OrdersSingleFoodItemAdapter adapter = new OrdersSingleFoodItemAdapter(
                    itemView.getContext(),
                    R.layout.restaurant_orders_list_item_single,
                    ordersItem.getOrderedFood()
            );
            mOrderItemsListView.setAdapter(adapter);

            // Set the height of the ListView based on the number of items
            ViewGroup.LayoutParams params = mOrderItemsListView.getLayoutParams();
            params.height = mOrderItemsListView.getCount() * (int) itemView.getContext().getResources().getDimension(R.dimen.list_item_height);
            mOrderItemsListView.setLayoutParams(params);
        }
    }
}

