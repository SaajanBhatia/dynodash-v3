package com.example.dynodash.ui.restaurant.orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynodash.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private OrdersViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private OrdersAdapter mOrdersAdapter;

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.restaurant_orders_fragment, container, false);

        mRecyclerView = root.findViewById(R.id.ordersRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrdersAdapter = new OrdersAdapter();
        mRecyclerView.setAdapter(mOrdersAdapter);

        mOrdersAdapter.setOnItemClickListener(new OrdersAdapter.OnItemClickListener() {
            @Override
            public void onCompleteOrder(OrdersListItem item) {
                mViewModel.completeOrderItem(
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        item.getOrderID()
                );
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views and setup any necessary listeners or adapters
        mViewModel = new ViewModelProvider(this).get(OrdersViewModel.class);

        // Saturate the recycler view app
        mViewModel.getOutgoingOrders(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), new Observer<List<OrdersListItem>>() {
            @Override
            public void onChanged(List<OrdersListItem> ordersListItems) {
                mOrdersAdapter.setOrderItems(ordersListItems);
            }
        });


    }

}
