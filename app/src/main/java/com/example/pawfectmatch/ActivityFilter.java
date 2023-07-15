package com.example.pawfectmatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityFilter extends AppCompatActivity {

    String[] items = {"Dog", "Cat", "Birds"};
    String[] genders = {"Male", "Female"};
    AutoCompleteTextView autoCompleteTxt, auto_gender;
    ArrayAdapter<String> adapterItems, adapterGender;
    Button btnSearch;
    String Type, Breed, Size, Age, Item;
    EditText edtBreed, edtSize, edtAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        auto_gender = findViewById(R.id.auto_gender);
        btnSearch = findViewById(R.id.btnSearch);

        edtBreed = findViewById(R.id.edtBreed);
        edtSize = findViewById(R.id.edtSize);
        edtAge = findViewById(R.id.edtAge);



        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTxt.setAdapter(adapterItems);
        adapterGender = new ArrayAdapter<String>(this, R.layout.list_item, genders);
        auto_gender.setAdapter(adapterGender);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Type: " + item, Toast.LENGTH_SHORT).show();
                Type = item;
            }
        });
        auto_gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Type: " + item, Toast.LENGTH_SHORT).show();
                Item = item;
            }
        });
        btnSearch.setOnClickListener(v -> {
            Breed = edtBreed.getText().toString();
            Size = edtSize.getText().toString();
            Age = edtAge.getText().toString();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("Type", Type);
                intent.putExtra("Breed", Breed);
                intent.putExtra("Size", Size);
                intent.putExtra("Age", Age);
                intent.putExtra("Item", Item);
                startActivity(intent);
                finish();
        });

    }
}