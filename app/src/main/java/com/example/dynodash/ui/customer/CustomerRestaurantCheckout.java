package com.example.dynodash.ui.customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.dynodash.R;
import com.example.dynodash.Utils;
import com.example.dynodash.ui.customer.list.OrderItem;
import com.example.dynodash.ui.restaurant.orders.OrdersSingleFoodItem;
import com.example.dynodash.ui.restaurant.orders.OrdersSingleFoodItemAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomerRestaurantCheckout extends AppCompatActivity {

    private ListView checkoutItems;
    private TextView totalTextView;
    private TextInputEditText tableNumberEditText;
    private Button checkoutButton;
    private CustomerViewModel mViewModel;
    private List<OrderItem> orderedItems = new ArrayList<>();
    private String mTableNumber;

    public ArrayList<OrderItem> getSharedPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("ordered_items", "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<OrderItem>>() {
        }.getType();
        ArrayList<OrderItem> orderedItems = gson.fromJson(json, type);
        return orderedItems;
    }

    public void setSharedPrefs() {
        Gson gson = new Gson();
        String json = gson.toJson(orderedItems);

        // Write the JSON to shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ordered_items", json);
        editor.apply();
    }

    private Double calculateTotalCost(ArrayList<OrderItem> orderItems) {
        Double total = 0.00;
        for (OrderItem orderItem : orderItems) {
            total = total + (orderItem.getSinglePrice() * orderItem.getQuantity());
        }
        return Math.ceil(total * 100) / 100;
    }

    private ArrayList<OrdersSingleFoodItem> convertToAdapterFormat(ArrayList<OrderItem> orderItems) {
        ArrayList<OrdersSingleFoodItem> singleFoodItems = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            OrdersSingleFoodItem singleFoodItem = new OrdersSingleFoodItem(
                    orderItem.getItemName(),
                    orderItem.getQuantity(),
                    orderItem.getSinglePrice()
            );
            singleFoodItem.itemID = orderItem.getItemID();
            singleFoodItems.add(singleFoodItem);
        }

        return singleFoodItems;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_restaurant_checkout);

        // ViewModel Declaration
        mViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        Intent intentFetches = getIntent();

        Intent intent = getIntent();
        String restaurantID = intent.getStringExtra("restaurantID");
        Log.d("Customer Restaurant Checkout Line 97", "onCreate: Restaurant ID: " + restaurantID);

        // UI Components
        checkoutItems = findViewById(R.id.order_list_view_final);
        OrdersSingleFoodItemAdapter adapter = new OrdersSingleFoodItemAdapter(
                this,
                R.layout.restaurant_orders_list_item_single,
                convertToAdapterFormat(getSharedPrefs())
        );
        checkoutItems.setAdapter(adapter);

        totalTextView = findViewById(R.id.total_value_text_view);
        totalTextView.setText("£ " + calculateTotalCost(getSharedPrefs()).toString());

        tableNumberEditText = findViewById(R.id.table_number_edit_text);
        checkoutButton = findViewById(R.id.checkout_button_final);

        checkoutButton.setEnabled(true);

        // If intent has table number than forward it
        if (intentFetches.hasExtra("tableNumber")) {
            mTableNumber = intentFetches.getStringExtra("tableNumber");
            tableNumberEditText.setText("30");
        }

        // On checkout, create order in real time database, redirect to all restaurants
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If table number empty then alert toast
                if (tableNumberEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(CustomerRestaurantCheckout.this, "Please Enter Table Number", Toast.LENGTH_SHORT).show();
                } else { // Checkout functionality
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String customerID = currentUser.getUid();
                    mViewModel.createOrder(
                            customerID,
                            intent.getStringExtra("restaurantID"),
                            Integer.valueOf(tableNumberEditText.getText().toString().trim()),
                            getSharedPrefs(),
                            calculateTotalCost(getSharedPrefs())
                    );

                    // Push Notification when order is made
                    Utils.pushNotification(CustomerRestaurantCheckout.this, "Your order is on its way!",
                            "Hey! Your order is being prepared. " +
                            "You will receive a notification when your order is ready.");

                    // Clear order
                    setSharedPrefs();

                    Intent intent = new Intent(CustomerRestaurantCheckout.this, CustomerActivity.class);
                    intent.putExtra("customerID", customerID);
                    startActivity(intent);
                    finish(); // Prevent going to order


                }
            }
        });


    }
}
