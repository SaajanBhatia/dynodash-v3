package com.example.dynodash.ui.customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynodash.R;
import com.example.dynodash.ui.customer.list.MenuListAdapter;
import com.example.dynodash.ui.customer.list.MenuListItem;
import com.example.dynodash.ui.customer.list.OrderItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomerRestaurantActivity extends AppCompatActivity {

    private String restaurantID;
    private FloatingActionButton checkoutButton;
    private RecyclerView menuItemsRecycler;
    private TextView restaurantName;
    private TextView restaurantDesc;
    private TextView restaurantAddr;
    private CustomerViewModel mViewModel;
    private MenuListAdapter mMenuCustomerAdapter;
    private ArrayList<OrderItem> orderedItems;
    private String restaurantNameIntentRead;
    private String restaurantDescIntentRead;
    private String restaurantAddrIntentRead;
    private LinearLayout buttonConsole;

    public void setSharedPrefs() {
        Gson gson = new Gson();
        String json = gson.toJson(orderedItems);

        // Write the JSON to shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ordered_items", json);
        editor.apply();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_restaurant_single_view);

        // ViewModel Declaration
        mViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        // Use SharedPreferences to read and write the orders
        orderedItems = new ArrayList<>();
        setSharedPrefs();


        // Get the restaurantID from the intent
        restaurantID = getIntent().getStringExtra("restaurantID");

        // Get the restaurant details from the intent
        restaurantNameIntentRead = getIntent().getStringExtra("restaurantName");
        restaurantDescIntentRead = getIntent().getStringExtra("restaurantDesc");
        restaurantAddrIntentRead = getIntent().getStringExtra("restaurantAddr");

        // Declare UI Items
        checkoutButton = findViewById(R.id.fab);

        menuItemsRecycler = findViewById(R.id.menu_items_recycler_view);
        restaurantName = findViewById(R.id.restaurant_customer_name);
        restaurantDesc = findViewById(R.id.restaurant_customer_description);
        restaurantAddr = findViewById(R.id.restaurant_customer_address);

        // Set restaurant details from Intent Data
        restaurantName.setText(!restaurantNameIntentRead.isEmpty() ? restaurantNameIntentRead : "Restaurant Name");
        restaurantDesc.setText(!restaurantDescIntentRead.isEmpty() ? restaurantDescIntentRead : "Restaurant Description");
        restaurantAddr.setText(!restaurantAddrIntentRead.isEmpty() ? restaurantAddrIntentRead : "Restaurant Address");

        // Recycler View Declaration
        menuItemsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMenuCustomerAdapter = new MenuListAdapter();
        menuItemsRecycler.setAdapter(mMenuCustomerAdapter);

        // Hide the map component on restaurant activity screen
        buttonConsole = findViewById(R.id.button_linear_layout);
        buttonConsole.setVisibility(View.GONE);

        // Saturate Recycler View from ViewModel
        mViewModel.getMenuItems(restaurantID).observe(CustomerRestaurantActivity.this, new Observer<List<MenuListItem>>() {
            @Override
            public void onChanged(List<MenuListItem> menuListItems) {
                // Once we have the menu items, increase or decrease the displayed quantity depending on the ordered food
                mMenuCustomerAdapter.setMenuItems(CustomerViewModel.saturateOrdersIntoItems(orderedItems, menuListItems));
            }
        });

        Intent intentFetches = getIntent();

        // Checkout Listener
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Prevent checkout if basket is empty
                if (!orderedItems.isEmpty()) {
                    // When clicked, open intent passing the ordered items array
                    Intent intent = new Intent(CustomerRestaurantActivity.this, CustomerRestaurantCheckout.class);
                    intent.putExtra("restaurantID", restaurantID);// Pass the restaurant ID through intent

                    // If intent has table number than forward it
                    if (intentFetches.hasExtra("tableNumber")) {
                        intent.putExtra("tableNumber", intent.getStringExtra("tableNumber"));
                    }

                    CustomerRestaurantActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(CustomerRestaurantActivity.this, "Basket is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // On Click Listeners from Recycler
        mMenuCustomerAdapter.setOnItemClickListener(new MenuListAdapter.OnItemClickListener() {
            @Override
            public void onAddQuantity(MenuListItem item) {
                // First check if item exists in orders
                Integer orderedItemPosition = CustomerViewModel.linearSearchThroughOrders(orderedItems, item.getItemID());
                if (orderedItemPosition != -1) { // If it exists in the ordered items, increase its quantity
                    OrderItem originalItem = orderedItems.get(orderedItemPosition);
                    originalItem.setQuantity(originalItem.getQuantity() + 1); // Increase quantity by 1
                    orderedItems.set(orderedItemPosition, originalItem); // Update the ArrayList

                } else { // Else create a new item in ordered items
                    OrderItem newOrderItem = new OrderItem(
                            item.getItemID(),
                            1,
                            item.getItemPrice(),
                            item.getItemName()
                    ); // Set the new quantity to 1
                    orderedItems.add(newOrderItem); // Add new item to ordered items
                }

                // Update Shared Preferences
                setSharedPrefs();

                // Update the RecyclerView List
                mViewModel.getMenuItems(restaurantID).observe(CustomerRestaurantActivity.this, new Observer<List<MenuListItem>>() {
                    @Override
                    public void onChanged(List<MenuListItem> menuListItems) {
                        // Once we have the menu items, increase or decrease the displayed quantity depending on the ordered food
                        mMenuCustomerAdapter.setMenuItems(CustomerViewModel.saturateOrdersIntoItems(orderedItems, menuListItems));
                    }
                });
            }

            @Override
            public void onSubtractQuantity(MenuListItem item) {
                Integer orderedItemPosition = CustomerViewModel.linearSearchThroughOrders(orderedItems, item.getItemID());
                if (orderedItemPosition != -1) { // If it exists in the ordered items
                    OrderItem originalItem = orderedItems.get(orderedItemPosition);
                    originalItem.setQuantity(originalItem.getQuantity() - 1); // Decrease quantity by 1

                    // If 0, remove from basket
                    if (originalItem.getQuantity().equals(0)) {
                        orderedItems.remove(originalItem);
                    } else {
                        orderedItems.set(orderedItemPosition, originalItem); // Else update the ArrayList
                    }
                }

                // Update Shared Preferences
                setSharedPrefs();

                // Update the RecyclerView List
                mViewModel.getMenuItems(restaurantID).observe(CustomerRestaurantActivity.this, new Observer<List<MenuListItem>>() {
                    @Override
                    public void onChanged(List<MenuListItem> menuListItems) {
                        // Once we have the menu items, increase or decrease the displayed quantity depending on the ordered food
                        mMenuCustomerAdapter.setMenuItems(CustomerViewModel.saturateOrdersIntoItems(orderedItems, menuListItems));
                    }
                });
            }
        });


    }
}

