package com.example.dynodash.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynodash.R;

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

    // ViewModel
    private CustomerViewModel mViewModel;

    // TextViews
    private EditText SearchQuery;
    private Button SearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_layout);

        mViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        // Retrieve the UID
        Intent intent = getIntent();
        String customerID = intent.getStringExtra("USER_ID");

        // Add your code here
        mSearchResultsRecycler = findViewById(R.id.search_results_recyclerview);
        mPastResultsRecycler = findViewById(R.id.past_results_recyclerview);
        SearchQuery = findViewById(R.id.search_restaurants);
        SearchButton = findViewById(R.id.search_restaurants_button);

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
                Intent intent = new Intent(CustomerActivity.this, CustomerRestaurantActivity.class);
                CustomerActivity.this.startActivity(intent);
            }
        });
        mPastResultsAdapter.setOnItemClickListener(new RestaurantListAdapter.OnItemClickListener() {
            @Override
            public void onRestaurantClick(RestaurantListItem item) {
                // TODO Set the change in intent when restaurant card pressed
            }
        });

        mSearchResultsAdapter.setRestaurantItems(new ArrayList<RestaurantListItem>());
        mPastResultsAdapter.setRestaurantItems(new ArrayList<RestaurantListItem>());

    }
}
