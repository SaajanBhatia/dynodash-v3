package com.example.dynodash.ui.restaurant.code;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.dynodash.R;

public class CodeFragment extends Fragment {

    private CodeViewModel mViewModel;

    public static CodeFragment newInstance() {
        return new CodeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_code_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views and setup any necessary listeners or adapters

        mViewModel = new ViewModelProvider(this).get(CodeViewModel.class);
        // TODO: Use the ViewModel
    }

}