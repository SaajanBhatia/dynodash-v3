package com.example.dynodash.ui.restaurant.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynodash.R;
import com.example.dynodash.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MenuFragment extends Fragment {

    private MenuViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private MenuAdapter mMenuAdapter;

    private EditText mNewItemName;
    private EditText mNewMenuItemDescription;
    private EditText mNewMenuItemPrice;
    private MaterialButton mAddNewMenuItem;


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

        mMenuAdapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onUpdateClick(MenuRestaurantItem item) {
                mViewModel.updateMenuItem(FirebaseAuth.getInstance().getCurrentUser().getUid(), item);
            }

            @Override
            public void onDeleteClick(MenuRestaurantItem item) {
                mViewModel.deleteMenuItem(FirebaseAuth.getInstance().getCurrentUser().getUid(), item.getId());
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        mNewItemName = view.findViewById(R.id.newMenuItemName);
        mNewMenuItemDescription = view.findViewById(R.id.newMenuItemDescription);
        mNewMenuItemPrice = view.findViewById(R.id.newMenuItemPrice);
        mAddNewMenuItem = view.findViewById(R.id.addNewMenuItem);

        mViewModel.getMenuItems(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), new Observer<List<MenuRestaurantItem>>() {
            @Override
            public void onChanged(List<MenuRestaurantItem> menuItems) {
                mMenuAdapter.setMenuItems(menuItems);
            }
        });

        mAddNewMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNewItemName.getText().toString();
                String description = mNewMenuItemDescription.getText().toString();
                String price = mNewMenuItemPrice.getText().toString();

                if (name.isEmpty()) {
                    mNewItemName.setError("Name cannot be empty");
                    return;
                }
                if (description.isEmpty()) {
                    mNewMenuItemDescription.setError("Description cannot be empty");
                    return;
                }
                if (price.isEmpty()) {
                    mNewMenuItemPrice.setError("Price cannot be empty");
                    return;
                }

                // Check if price matches correct format
                if (!price.matches("\\d+(\\.\\d{1,2})?")) {
                    mNewMenuItemPrice.setError("Invalid price format");
                    return;
                }

                // Check if price matches correct format

                mNewItemName.setError(null);
                mNewMenuItemDescription.setError(null);
                mNewMenuItemPrice.setError(null);

                // Create new item instance
                MenuRestaurantItem newItem = new MenuRestaurantItem(Utils.generateCUID(), name, description, Double.valueOf(price));

                // Ask View Model to create new menu item
                mViewModel.addMenuItem(FirebaseAuth.getInstance().getCurrentUser().getUid(), newItem);

                // Clear text fields
                mNewItemName.setText("");
                mNewMenuItemDescription.setText("");
                mNewMenuItemPrice.setText("");
            }
        });
    }

}
