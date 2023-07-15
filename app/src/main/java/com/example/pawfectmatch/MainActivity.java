package com.example.pawfectmatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnStateChange{
    private BottomNavigationView bottomNavigationView;
    private ImageView imv_filter;
    TextView title;
    public static List<Pet> list=new ArrayList<>();
    String Type, Breed, Size, Age, Item;
    DAB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Type=getIntent().getStringExtra("Type");
        Breed=getIntent().getStringExtra("Breed");
        Size=getIntent().getStringExtra("Size");
        Age=getIntent().getStringExtra("Age");
        Item=getIntent().getStringExtra("Item");
        if(Type!=null || Age!=null || Breed!=null || Size!=null || Item!=null){
            Bundle bundle = new Bundle();
            bundle.putString("Type", Type);
            bundle.putString("Age", Age);
            bundle.putString("Breed", Breed);
            bundle.putString("Size", Size);
            bundle.putString("Item", Item);
            HomeFragment homeFragment=new HomeFragment();
            homeFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager(); // Replace with your FragmentManager instance
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, homeFragment); // Replace "R.id.container" with the ID of your fragment container
            fragmentTransaction.commit();


        }else{
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameLayout, homeFragment).commit();
        }

        bottomNavigationView = findViewById(R.id.bottomMenu);
        title = findViewById(R.id.title);

        imv_filter = findViewById(R.id.imv_filter);
        imv_filter.setOnClickListener(v->{
            Intent intent=new Intent(getApplicationContext(),ActivityFilter.class);
            startActivity(intent);
            finish();
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.bottom_home:
                        title.setVisibility(View.VISIBLE);
                        imv_filter.setVisibility(View.VISIBLE);
                        fragment = new HomeFragment();
                        break;
                    case R.id.favourite:
                        title.setVisibility(View.GONE);
                        imv_filter.setVisibility(View.GONE);
                        fragment = new FavouriteFragment();
                        break;
                    case R.id.pets:
                        title.setVisibility(View.GONE);
                        imv_filter.setVisibility(View.GONE);
                        fragment = new Pet_Profile();
                        break;
                }
                if (fragment != null) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frameLayout, fragment).commit();
                    return true;
                }
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.getBackStackEntryCount();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            super.onBackPressed();

    }


    @Override
    public void onStateChange() {
        title.setVisibility(View.VISIBLE);
        imv_filter.setVisibility(View.VISIBLE);
        HomeFragment homeFragment=new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager(); // Replace with your FragmentManager instance
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, homeFragment); // Replace "R.id.container" with the ID of your fragment container
        fragmentTransaction.commit();
    }

    @Override
    public void onStateChangeFavourite() {
        title.setVisibility(View.GONE);
        imv_filter.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FavouriteFragment()).commit();

    }

    @Override
    public void onStateChangePersonal() {

        title.setVisibility(View.GONE);
        imv_filter.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Pet_Profile()).commit();

    }

}