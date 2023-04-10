package com.example.dynodash.ui.restaurant.orders;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OrdersViewModel extends ViewModel {

    // Database reference
    private final DatabaseReference databaseReference;
    private final MutableLiveData<List<OrdersListItem>> orderItemsLiveData;

    public OrdersViewModel() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        orderItemsLiveData = new MutableLiveData<>();
    }

    public void completeOrderItem(String restaurantId, String orderId) {
        // Database Reference to the order to complete
        DatabaseReference keyRef = databaseReference.child("restaurants").child(restaurantId).child("orders").child(orderId);
        keyRef.child("completed").setValue(true);
    }

    public LiveData<List<OrdersListItem>> getOutgoingOrders(String restaurantId){
        Query query = databaseReference.child("restaurants").child(restaurantId).child("orders");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<OrdersListItem> orderItems = new ArrayList<>();
                for (DataSnapshot orderItemSnapshot : snapshot.getChildren()) {

                    // If the order is not completed
                    if ( !orderItemSnapshot.child("completed").getValue(Boolean.class) ){
                        // Order Item Attributes
                        String orderID = orderItemSnapshot.getKey();
                        String customerID = orderItemSnapshot.child("customerID").getValue(String.class);
                        String customerName = orderItemSnapshot.child("customer_name").getValue(String.class);
                        Boolean completed = orderItemSnapshot.child("completed").getValue(Boolean.class);
                        Integer tableNumber = orderItemSnapshot.child("table").getValue(Integer.class);
                        Double orderTotal = orderItemSnapshot.child("orderTotal").getValue(Double.class);

                        // Get the ordered items list
                        ArrayList<OrdersSingleFoodItem> itemsList = new ArrayList<>();
                        for (DataSnapshot itemSnapshot : orderItemSnapshot.child("items").getChildren()) {
                            String itemName = itemSnapshot.child("name").getValue(String.class);
                            Integer itemQuantity = itemSnapshot.child("quantity").getValue(Integer.class);
                            Double itemPrice = itemSnapshot.child("price").getValue(Double.class);

                            OrdersSingleFoodItem item = new OrdersSingleFoodItem(
                                    itemName, itemQuantity, itemPrice
                            );
                            itemsList.add(item);
                        }

                        OrdersListItem orderItem = new OrdersListItem(
                                orderID,
                                customerID,
                                customerName,
                                completed,
                                tableNumber,
                                orderTotal,
                                itemsList
                        );

                        orderItems.add(orderItem);
                    }
                }
                orderItemsLiveData.postValue(orderItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Orders View Model", "onCancelled: Error fetching orders");
            }
        });
        return orderItemsLiveData;
    }
}
