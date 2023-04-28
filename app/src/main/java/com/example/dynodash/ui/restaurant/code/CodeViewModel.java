package com.example.dynodash.ui.restaurant.code;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dynodash.ui.restaurant.RestaurantActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CodeViewModel extends ViewModel {

    // Database reference
    private final DatabaseReference databaseReference;
    private final MutableLiveData<List<CodeListItem>> codeItemLiveData;
    private final OkHttpClient client;

    public CodeViewModel() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        codeItemLiveData = new MutableLiveData<>();
        client = new OkHttpClient();

    }

    public LiveData<List<CodeListItem>> getTableList(String restaurantId) {
        Query query = databaseReference.child("restaurants").child(restaurantId).child("tables");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CodeListItem> codeItems = new ArrayList<>();
                Integer totalTables = snapshot.getValue(Integer.class);

                // Create a list of code items
                for (int i = 1; i <= totalTables; i ++) codeItems.add(new CodeListItem(i));
                codeItemLiveData.postValue(codeItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CodeViewModel@47", "onCancelled: Code List Items Failed");
            }
        });
        return codeItemLiveData;
    }

    public void getBarcode(String restaurantId, String tableId) throws IOException {
        Request request = new Request.Builder()
                .url("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=app://dyno?restaurantID=" + restaurantId + "%26table=" + tableId)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure here
                Log.e("CodeViewModel:80", "onFailure: Error on Failure of calling response", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // Handle error here
                    Log.e("CodeViewModel:86", "onResponse: Failed Barcode GET Request");
                }

                InputStream inputStream = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Save the image to the phone's external storage
                String fileName = "qr_code_table_" +  tableId + ".png";
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();


            }
        });
    }
}
