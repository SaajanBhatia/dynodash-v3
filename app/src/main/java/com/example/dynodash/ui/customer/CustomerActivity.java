package com.example.dynodash.ui.customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
    private RestaurantListAdapter mRestaurantAdapter;

    // ViewModel
    private CustomerViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_layout);

        // Retrieve the UID
        Intent intent = getIntent();
        String customerID = intent.getStringExtra("USER_ID");

        // Add your code here
        mSearchResultsRecycler = findViewById(R.id.search_results_recyclerview);
        mPastResultsRecycler = findViewById(R.id.past_results_recyclerview);

        mSearchResultsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mPastResultsRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Initialise the adapter
        mRestaurantAdapter = new RestaurantListAdapter();

        mSearchResultsRecycler.setAdapter(mRestaurantAdapter);
        mPastResultsRecycler.setAdapter(mRestaurantAdapter);

        mRestaurantAdapter.setOnItemClickListener(new RestaurantListAdapter.OnItemClickListener() {
            @Override
            public void onRestaurantClick(RestaurantListItem item) {
                // TODO Set the change in intent when restaurant card pressed
            }
        });

        List<RestaurantListItem> sampleList = new ArrayList<>();
        RestaurantListItem sample1 = new RestaurantListItem(
                "Luigi's",
                "This is the name of a description",
                "116 Bressey Grove",
                ""
        );
        RestaurantListItem sample2 = new RestaurantListItem(
                "Mario's",
                "This is the name of a description",
                "234 Bressey Grove",
                ""
        );
        RestaurantListItem sample3 = new RestaurantListItem(
                "Alpha's",
                "This is the name of a description",
                "34 Bressey Grove",
                ""
        );
        sampleList.add(sample1);
        sampleList.add(sample2);
        sampleList.add(sample3);
        sampleList.add(sample2);
        mRestaurantAdapter.setRestaurantItems(sampleList);

    }
}
