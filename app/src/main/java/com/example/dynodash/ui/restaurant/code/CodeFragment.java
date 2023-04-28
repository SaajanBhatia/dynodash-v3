package com.example.dynodash.ui.restaurant.code;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.dynodash.R;
import com.example.dynodash.ui.restaurant.RestaurantActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        mCodeListAdapter.setOnItemClickListener(new CodeListAdapter.OnItemClickListener() {
            @Override
            public void getCode(CodeListItem item) throws IOException {
                String table = String.valueOf(item.tableNumber);
                mViewModel.getBarcode( FirebaseAuth.getInstance().getCurrentUser().getUid(), table);
                Toast.makeText(getContext(), "Downloaded QR Code", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Table items
//        ArrayList<CodeListItem> codeList = new ArrayList<CodeListItem>();
//
//        CodeListItem item1 = new CodeListItem(1);
//        codeList.add(item1);
//
//        CodeListItem item2 = new CodeListItem(2);
//        codeList.add(item2);
//
//        CodeListItem item3 = new CodeListItem(3);
//        codeList.add(item3);
//
//        mCodeListAdapter.setCodeItems(codeList);

        mViewModel = new ViewModelProvider(this).get(CodeViewModel.class);

        // Saturate recycler view containing the orders adapter
        mViewModel.getTableList(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), new Observer<List<CodeListItem>>() {
            @Override
            public void onChanged(List<CodeListItem> codeListItems) {
                mCodeListAdapter.setCodeItems(codeListItems);
            }
        });

    }

}