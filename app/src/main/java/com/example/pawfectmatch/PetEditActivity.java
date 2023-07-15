package com.example.pawfectmatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;

public class PetEditActivity extends AppCompatActivity {

    private EditText edtType, edtSize, edtBreed, edtWeight, edtGender, edtLocation, edtNumber, edtSpecifications, edtDetails, edtAge;
    private ImageView ivImage;
    private Button btnAdd;
    private Uri uri;
    FirebaseStorage mStore;
    Extra extra;
    double Lat, Lng;
    String Type, Size, Breed, weight, gender, location, number, specification, details, age, Id;
    DAB obj;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri=data.getData();
        ivImage.setImageURI(uri);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_edit);
    }
}