package com.example.dynodash.ui.restaurant.code;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.dynodash.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CodeFragment extends Fragment {

    private CodeViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private CodeListAdapter mCodeListAdapter;

    // Database reference
    public static CodeFragment newInstance() {
        return new CodeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.restaurant_code_fragment, container, false);

        mRecyclerView = root.findViewById(R.id.codeRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCodeListAdapter = new CodeListAdapter();
        mRecyclerView.setAdapter(mCodeListAdapter);

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Table items
        ArrayList<CodeListItem> codeList = new ArrayList<CodeListItem>();

        CodeListItem item1 = new CodeListItem(1);
        codeList.add(item1);

        CodeListItem item2 = new CodeListItem(2);
        codeList.add(item2);

        CodeListItem item3 = new CodeListItem(3);
        codeList.add(item3);

        mCodeListAdapter.setCodeItems(codeList);

        // Make reference
//        databaseReference.child("restaurants").child(restaurantId).child("tables").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("CodeViewModel", "onComplete: Failed to retrieve number of tables" );
//                } else {
//                    // Recurse through number of tables, create n objects
//                    Log.d("DEBUG TAG", "onComplete: Tables: " + String.valueOf(task.getResult().getValue()));
//                    Integer numTables = Integer.valueOf(String.valueOf(task.getResult().getValue()));
//                    for (int i = 1; i < numTables; i++) {
//                        tableItems.add( new CodeListItem(i) );
//                    }
//                    mCodeListAdapter.setCodeItems(tableItems);
//                }
//            }
//        });
    }

}