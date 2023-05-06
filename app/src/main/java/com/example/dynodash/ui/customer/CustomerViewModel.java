package com.example.dynodash.ui.customer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dynodash.Utils;
import com.example.dynodash.ui.customer.list.MenuListItem;
import com.example.dynodash.ui.customer.list.OrderItem;
import com.example.dynodash.ui.customer.list.RestaurantListItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerViewModel extends ViewModel {

    private DatabaseReference databaseReference;
    private final MutableLiveData<List<MenuListItem>> menuItemsLiveData;


    public CustomerViewModel() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        menuItemsLiveData = new MutableLiveData<>();
    }

    public void createOrder(
            String customerID,
            String restaurantID,
            Integer tableNumber,
            List<OrderItem> orderItems,
            Double orderTotal
    ) {

        // Get the Customer Name
        databaseReference.child("users").child(customerID).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("CustomerViewModel:53", "Error getting data", task.getException());
                }
                else { // On successful fetch of customer name, saturate the order
                    Log.d("This is the Customer View Model Order Creation", String.valueOf(task.getResult().getValue()));
                    String customerName = String.valueOf(task.getResult().getValue());

                    String orderID = Utils.generateCUID(); // Generate a new Order ID CUID

                    // Input the order details
                    DatabaseReference orderReference = databaseReference.child("restaurants").child(restaurantID).child("orders").child(orderID);
                    orderReference.child("customerID").setValue(customerID);
                    orderReference.child("customer_name").setValue(customerName);
                    orderReference.child("table").setValue(tableNumber);
                    orderReference.child("completed").setValue(false);

                    // Add the order items
                    DatabaseReference orderItemsReference = orderReference.child("items");

                    for (OrderItem item : orderItems) {
                        orderItemsReference.child(item.getItemID()).child("name").setValue(item.getItemName());
                        orderItemsReference.child(item.getItemID()).child("price").setValue(item.getSinglePrice());
                        orderItemsReference.child(item.getItemID()).child("quantity").setValue(item.getQuantity());
                    }
                    orderReference.child("orderTotal").setValue(orderTotal);

                }
            }
        });

    }

    public LiveData<List<RestaurantListItem>> getSearchResults(String searchQuery) {
        MutableLiveData<List<RestaurantListItem>> searchResultsLiveData = new MutableLiveData<>();

        Query query = databaseReference.child("restaurants");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<RestaurantListItem> searchResultsList = new ArrayList<>();

                for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
                    String id = restaurantSnapshot.getKey();
                    String name = restaurantSnapshot.child("name").getValue(String.class);
                    String description = restaurantSnapshot.child("description").getValue(String.class);
                    String address = restaurantSnapshot.child("address").getValue(String.class);

                    if (name != null && name.toLowerCase().startsWith(searchQuery.toLowerCase())) {
                        RestaurantListItem restaurant = new RestaurantListItem(
                                name,
                                description,
                                address,
                                id
                        );
                        searchResultsList.add(restaurant);
                    }
                }

                searchResultsLiveData.setValue(searchResultsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("OnCancelled@CustomerViewModel:57", "getSearchResults:onCancelled", error.toException());
                searchResultsLiveData.setValue(null);
            }
        });

        return searchResultsLiveData;
    }

    // Linear search through ordered items, return -1 if not found
    public static Integer linearSearchThroughOrders(List<OrderItem> orderedItems, String idQuery) {
        for (int j = 0; j < orderedItems.size(); j ++ ) {
            OrderItem orderItem = orderedItems.get(j);
            if (orderItem.getItemID().equals(idQuery)) {
                return j;
            }
        }
        return -1;
    }

    // Format Menu Items in line with ordered items
    public static List<MenuListItem> saturateOrdersIntoItems(List<OrderItem> orderItems, List<MenuListItem> menuItems) {
        // Recurse through the ordered items, adjusting the quantity of items in the array
        ArrayList<MenuListItem> formattedItems = (ArrayList<MenuListItem>) menuItems;
        for (int i = 0; i < formattedItems.size(); i ++) {
            MenuListItem menuListItem = formattedItems.get(i);
            Integer orderedItemPosition = linearSearchThroughOrders(orderItems, menuListItem.getItemID());
            if (orderedItemPosition != -1) { // If the menu item exists in the ordered items array
                Integer quantity = orderItems.get(orderedItemPosition).getQuantity();
                menuListItem.setQuantity(quantity);
            }
        }

        return formattedItems;
    }

    // Get menu items for recycler menu
    public LiveData<List<MenuListItem>> getMenuItems(String restaurantId) {
        Query query = databaseReference.child("restaurants").child(restaurantId).child("menu");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MenuListItem> menuItems = new ArrayList<>();
                for (DataSnapshot menuItemSnapshot : snapshot.getChildren()) {
                    String id = menuItemSnapshot.getKey();
                    String name = menuItemSnapshot.child("name").getValue(String.class);
                    String description = menuItemSnapshot.child("description").getValue(String.class);
                    Double price = menuItemSnapshot.child("price").getValue(Double.class);
                    MenuListItem menuItem = new MenuListItem(id, name, description, price);
                    menuItems.add(menuItem);
                }
                menuItemsLiveData.postValue(menuItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Log.e("CustomeViewModel@OnCancelled:91", "onCancelled: Unable to load menu list items customer side: " + error);
            }
        });
        return menuItemsLiveData;
    }

}
