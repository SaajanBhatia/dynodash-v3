package com.example.dynodash.ui.restaurant.menu;

import android.os.Bundle;
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

import java.util.List;

public class MenuFragment extends Fragment {

    private MenuViewModel mViewModel;

    // Declare UI Elements
    private RecyclerView mRecyclerView;
    private MenuAdapter mMenuAdapter;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.restaurant_menu_fragment, container, false);

        mRecyclerView = root.findViewById(R.id.restaurantMenuItemRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMenuAdapter = new MenuAdapter();
        mRecyclerView.setAdapter(mMenuAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // Call the getMenuItems method of the ViewModel to fetch the data
        mViewModel.getMenuItems(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), new Observer<List<MenuRestaurantItem>>() {
            @Override
            public void onChanged(List<MenuRestaurantItem> menuItems) {
                // Update the UI with the new data
                mMenuAdapter.setMenuItems(menuItems);
            }
        });
    }

}
