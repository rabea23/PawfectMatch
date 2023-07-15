package com.example.pawfectmatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.style.AlignmentSpan;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivitySplash extends AppCompatActivity {

    Button btnStart;
    public static List<Pet> list=new ArrayList<>();
    public static List<Pet> listFavourite=new ArrayList<>();
    public static String deviceId;
    DAB database;
    Extra extra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        btnStart = findViewById(R.id.btnStart);
        extra=new Extra(ActivitySplash.this);
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        database=new DAB();
        getAllData();
        getFavouriteData();

        btnStart.setOnClickListener(v -> {
            extra.showProgressDialog("Loading wait..","Data Load");
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                   startActivity(intent);
                   finish();
                   extra.cancelProgressDialog();
               }
           },2000);


        });

    }

    private void getFavouriteData() {
        database.getDataFavourite().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot FetchData : snapshot.getChildren()) {
                    Pet model = FetchData.getValue(Pet.class);
                    model.setId(FetchData.getKey());
                    listFavourite.add(model);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    void getAllData(){
        database.getData().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot FetchData : snapshot.getChildren()) {
                    Pet model = FetchData.getValue(Pet.class);
                    model.setId(FetchData.getKey());
                    list.add(model);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}