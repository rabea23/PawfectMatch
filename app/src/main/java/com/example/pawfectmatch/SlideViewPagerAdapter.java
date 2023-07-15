package com.example.pawfectmatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SlideViewPagerAdapter extends PagerAdapter {

    Context ctx;
    List<Pet> list;
    DAB database;

    List<Pet> list1;
    int Look = 0;
    public static int wrappedPosition;

    public SlideViewPagerAdapter(Context ctx, List<Pet> list) {
        this.ctx = ctx;
        this.list = list;
        database = new DAB();
    }

    @Override
    public int getCount() {
        return list.size() > 1 ? Integer.MAX_VALUE : list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_screen, container, false);
        TextView tvName, tvQuality, tvDescription;
        ImageView ivLargeImg, ivDelete, ivBigHeart, ivSetting;

        tvQuality = view.findViewById(R.id.tvQuality);
        tvDescription = view.findViewById(R.id.tvDescription);
        ivLargeImg = view.findViewById(R.id.ivLargeImg);
        ivDelete = view.findViewById(R.id.ivDelete);
        ivBigHeart = view.findViewById(R.id.ivBigHeart);
        tvName = view.findViewById(R.id.tvName);
        ivSetting = view.findViewById(R.id.ivSetting);

        wrappedPosition = position % list.size();
        Pet pet = list.get(wrappedPosition);
        ivBigHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pet pet = list.get(position);
                list1 = new ArrayList<>();
                database.getDataFavourite().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot FetchData : snapshot.getChildren()) {
                            Pet model = FetchData.getValue(Pet.class);
                            model.setId(FetchData.getKey());
                            list1.add(model);
                        }
                        for (int i = 0; i < list1.size(); i++) {
                            Pet tempModel = list1.get(i);
                            Pet tempCurrent = list.get(position);
                            tempCurrent.getId();
                            tempModel.getId();
                            if (tempModel.getId().equals(tempCurrent.getId())) {
                                Toast.makeText(ctx, "Move Left", Toast.LENGTH_SHORT).show();

                                Look = 1;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if (Look == 0) {
                    database.addFavourite(pet, pet.getId()).addOnSuccessListener(suc -> {
                        int nextPosition = position + 1;
                        SlideViewPagerAdapter.wrappedPosition = nextPosition % list.size();
                        HomeFragment.viewPager.setCurrentItem(nextPosition);
                    }).addOnFailureListener(er -> {

                        Toast.makeText(ctx, er.getMessage(), Toast.LENGTH_SHORT).show();
                    });


                }
            }
        });
        ivSetting.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, ActivityPetDetail.class);
            intent.putExtra("data", pet);
            ctx.startActivity(intent);
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.viewPager.setCurrentItem(position);
            }
        });
        ivDelete.setOnClickListener(v->{
            int nextPosition = position + 1;
            SlideViewPagerAdapter.wrappedPosition = nextPosition % list.size();
            HomeFragment.viewPager.setCurrentItem(nextPosition);
        });


        tvName.setText(pet.getPetName());
        tvQuality.setText(pet.getBestHabit());
        tvDescription.setText(pet.getDescription());
        Picasso.get().load(pet.getImage()).into(ivLargeImg);
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

    }
}
