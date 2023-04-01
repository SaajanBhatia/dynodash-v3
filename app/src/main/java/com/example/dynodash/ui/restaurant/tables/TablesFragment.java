package com.example.dynodash.ui.restaurant.tables;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dynodash.R;
import com.example.dynodash.ui.restaurant.RestaurantActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;


public class TablesFragment extends Fragment {

    private TablesViewModel mViewModel;

    private TextInputLayout numberInputLayout;
    private TextInputEditText numberInput;
    private Button updateButton;
    private TextView tableCountTextView;

    private int currentTableCount = 10;
    private final int MIN_TABLES = 1;
    private final int MAX_TABLES = 100;

    public static TablesFragment newInstance() {
        return new TablesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.restaurant_tables_fragment, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RestaurantActivity activity = (RestaurantActivity) getActivity();

        // Initialize views and setup any necessary listeners or adapters
        numberInputLayout = view.findViewById(R.id.number_input_layout);
        numberInput = (TextInputEditText) numberInputLayout.getEditText();
        updateButton = view.findViewById(R.id.update_tables_button);
        tableCountTextView = view.findViewById(R.id.table_count_textview);

        // Init the ViewModel
        mViewModel = new ViewModelProvider(this).get(TablesViewModel.class);

        // Set initial table count value
        mViewModel.readInstanceAndUpdateTextView(FirebaseAuth.getInstance().getUid(), tableCountTextView);

        // Set up button click listener to update table count
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input value from the TextInputEditText
                String input = numberInput.getText().toString().trim();

                // If the text is not empty
                if (!TextUtils.isEmpty(input)) {

                    // If the regex pattern matches integer format
                    if (input.matches("\\d+")) {
                        int tableCount = Integer.parseInt(input);

                        // Sanity Checks
                        if (tableCount < MIN_TABLES) {
                            tableCount = MIN_TABLES;
                            Toast.makeText(getContext(), "Minimum number of tables is " + MIN_TABLES, Toast.LENGTH_SHORT).show();
                        } else if (tableCount > MAX_TABLES) {
                            tableCount = MAX_TABLES;
                            Toast.makeText(getContext(), "Maximum number of tables is " + MAX_TABLES, Toast.LENGTH_SHORT).show();
                        }

                        // Update Firebase Store
                        mViewModel.updateTables(activity.getUid(), tableCount, tableCountTextView);
                    }
                }


            }

        });
    }
}
