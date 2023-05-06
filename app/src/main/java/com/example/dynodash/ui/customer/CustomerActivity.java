package com.example.dynodash.ui.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynodash.DBHelper;
import com.example.dynodash.MainActivity;
import com.example.dynodash.R;
import com.example.dynodash.RestaurantModel;
import com.example.dynodash.Utils;
import com.example.dynodash.ui.customer.list.RestaurantListAdapter;
import com.example.dynodash.ui.customer.list.RestaurantListItem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/*
 Main Page for Customer
 TODO recyclerview for cached locations
 TODO Search for restaurant name
*/

public class CustomerActivity extends AppCompatActivity {

    // Two Recyclers - Search Results and Past Results
    private RecyclerView mSearchResultsRecycler;
    private RecyclerView mPastResultsRecycler;
    private RestaurantListAdapter mSearchResultsAdapter;
    private RestaurantListAdapter mPastResultsAdapter;
    private Button logoutButton;

    // ViewModel
    private CustomerViewModel mViewModel;

    // TextViews
    private EditText SearchQuery;
    private Button SearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_layout);
        DBHelper dbHelper = new DBHelper(getApplicationContext());

        mViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        // Retrieve the UID
        Intent intentFetches = getIntent();
        String customerID = intentFetches.getStringExtra("USER_ID");

        // Check for QR Code
        if (intentFetches.hasExtra("restaurantID") && intentFetches.hasExtra("tableNumber")) {
            // both extras are present in the intent
            String restaurantID = intentFetches.getStringExtra("restaurantID");
            String tableNumber = intentFetches.getStringExtra("tableNumber");
            Utils.forwardUserOnQRCode(restaurantID, customerID, this, tableNumber);
        }

        // Add your code here
        mSearchResultsRecycler = findViewById(R.id.search_results_recyclerview);
        mPastResultsRecycler = findViewById(R.id.past_results_recyclerview);
        SearchQuery = findViewById(R.id.search_restaurants);
        SearchButton = findViewById(R.id.search_restaurants_button);

        // Logout
        logoutButton = findViewById(R.id.customerLogOut);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CustomerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Search Button Trigger
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If not empty string then publish search request
                if (!SearchQuery.getText().toString().isEmpty()) {
                    mViewModel.getSearchResults(SearchQuery.getText().toString()).observe(CustomerActivity.this, new Observer<List<RestaurantListItem>>() {
                        @Override
                        public void onChanged(List<RestaurantListItem> restaurantListItems) {
                            mSearchResultsAdapter.setRestaurantItems(restaurantListItems);
                        }
                    });
                }
            }
        });



        mSearchResultsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mPastResultsRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Initialise the adapter
        mSearchResultsAdapter = new RestaurantListAdapter();
        mPastResultsAdapter = new RestaurantListAdapter();

        mSearchResultsRecycler.setAdapter(mSearchResultsAdapter);
        mPastResultsRecycler.setAdapter(mPastResultsAdapter);

        mSearchResultsAdapter.setOnItemClickListener(new RestaurantListAdapter.OnItemClickListener() {
            @Override
            public void onRestaurantClick(RestaurantListItem item) {
                // See if the item already exists in past search
                if (!dbHelper.restaurantExists(item)) { // If doesn't exist add to past results
                    dbHelper.addRestaurant(item); // Then create SQLite record
                }
                Intent intent = new Intent(CustomerActivity.this, CustomerRestaurantActivity.class);
                intent.putExtra("restaurantID", item.getRestaurantID());
                intent.putExtra("restaurantName", item.getRestaurantName());
                intent.putExtra("restaurantDesc", item.getRestaurantDesc());
                intent.putExtra("restaurantAddr", item.getRestaurantAddr());

                // If intent has table number than forward it
                if (intentFetches.hasExtra("tableNumber")) {
                    intent.putExtra("tableNumber", intent.getStringExtra("tableNumber"));
                }
                CustomerActivity.this.startActivity(intent);
            }

            @Override
            public void onMapClick(RestaurantListItem item) {mapAction(item);}

            @Override
            public void onShareClick(RestaurantListItem item) {

            }
        });
        mPastResultsAdapter.setOnItemClickListener(new RestaurantListAdapter.OnItemClickListener() {
            @Override
            public void onRestaurantClick(RestaurantListItem item) {
                Intent intent = new Intent(CustomerActivity.this, CustomerRestaurantActivity.class);
                intent.putExtra("restaurantID", item.getRestaurantID());
                intent.putExtra("restaurantName", item.getRestaurantName());
                intent.putExtra("restaurantDesc", item.getRestaurantDesc());
                intent.putExtra("restaurantAddr", item.getRestaurantAddr());
                CustomerActivity.this.startActivity(intent);
            }

            @Override
            public void onMapClick(RestaurantListItem item) {mapAction(item);}

            @Override
            public void onShareClick(RestaurantListItem item) {


            }
        });

        mSearchResultsAdapter.setRestaurantItems(new ArrayList<RestaurantListItem>());
        mPastResultsAdapter.setRestaurantItems(new ArrayList<RestaurantListItem>());

        // Set the past restaurant items
        List<RestaurantListItem> pastFetchedRestaurants = dbHelper.getAllRestaurants();
        mPastResultsAdapter.setRestaurantItems(pastFetchedRestaurants);

    }

    public void mapAction(RestaurantListItem restaurant) {
        // Create a Uri object that specifies the search query
        Uri uri = Uri.parse("geo:0,0?q=" + restaurant.getRestaurantAddr());

        // Create an Intent with the ACTION_VIEW action and the Uri object as the data
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        // Set the package name for the intent (this ensures that the user is taken to Google Maps)
        intent.setPackage("com.google.android.apps.maps");

        // Start the activity
        startActivity(intent);

    }

    public void shareAction(RestaurantListItem restaurant) {

    }
}
