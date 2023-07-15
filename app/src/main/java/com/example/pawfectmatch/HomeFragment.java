package com.example.pawfectmatch;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    public static ViewPager viewPager;
    SlideViewPagerAdapter adapter;
    DAB database;
    Extra extra;
    private List<Pet> list;
    OnStateChange onStateChange;
    int Look = 0;
    View view;
    List<Pet> list1;
    String Type = null, petProfile,Breed=null,Size=null,Age=null,Item=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(ActivitySplash.list.size()==0){
            view = inflater.inflate(R.layout.is_null_layout, container, false);
            return view;
        }else {
            view = inflater.inflate(R.layout.fragment_home2, container, false);
            database = new DAB();
            extra = new Extra(getContext());
            Bundle arguments = getArguments();
            if (arguments != null) {
                Type = arguments.getString("Type");
                Breed = arguments.getString("Breed");
                Size = arguments.getString("Size");
                Age = arguments.getString("Age");
                Item = arguments.getString("Item");
                getDataSpecific(Type,Breed,Size,Item,Age);
            } else {
                extra.showProgressDialog("please wait...", "Data Load");
                getAllData();
            }
            viewPager = view.findViewById(R.id.viewpager);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter = new SlideViewPagerAdapter(getContext(), list);
                    viewPager.setAdapter(adapter);
                }
            }, 2000);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                private int currentPosition = viewPager.getCurrentItem();

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // Not needed for this case
                }

                @Override
                public void onPageSelected(int position) {
                    Pet pet;
                    if (position > currentPosition) {

                    } else if (position < currentPosition) {
                        list1 = new ArrayList<>();
                        if (position == position) {

                            int wrappedPosition = position % list.size();
                            if (list.size() - 1 == wrappedPosition) {
                                pet = list.get(0);
                            } else {
                                pet = list.get(wrappedPosition + 1);

                            }
                            extra.showProgressDialog("Liked post", "Post Like");
                            likedPost(pet);

                        } else {
                            extra.cancelProgressDialog();
                        }
                    }
                    currentPosition = position;
                }


                @Override
                public void onPageScrollStateChanged(int state) {
                    // Not needed for this case
                }
            });
        }
        return view;
    }

    private void getDataSpecific(String Type,String Breed,String Size ,String Item,String Age) {
        list = new ArrayList<>();
        database.getData().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot FetchData : snapshot.getChildren()) {
                    Pet model = FetchData.getValue(Pet.class);
                    model.setId(FetchData.getKey());
                    if(Type!=null && Breed.equals("") && Size.equals("") && Age.equals("") && Item==null){
                        if(model.getPetName().equals(Type)){
                            list.add(model);

                        }
                    } if(Type!=null && Breed!=null && Size.equals("") && Age.equals("") && Item==null){
                        if(model.getPetName().equals(Type) && model.getBreed().equals(Breed)){
                            list.add(model);

                        }
                    } if(Type!=null && Breed!=null && Size!=null && Age.equals("") && Item==null){
                        if(model.getPetName().equals(Type) && model.getBreed().equals(Breed) && model.getSize().equals(Size)){
                            list.add(model);

                        }
                    }if(Type!=null && Breed!=null && Size!=null && Age!=null && Item==null){
                        if(model.getPetName().equals(Type) && model.getBreed().equals(Breed) && model.getSize().equals(Size) && model.getAge().equals(Age)){
                            list.add(model);

                        }
                    }if(Type!=null && Breed!=null && Size!=null && Age!=null && Item!=null){
                        if(model.getPetName().equals(Type) && model.getBreed().equals(Breed) && model.getSize().equals(Size) && model.getAge().equals(Age) && model.getGender().equals(Item)){
                            list.add(model);

                        }

                    }if(Breed!=null && Type==null && Size.equals("") && Age.equals("") && Item==null){
                        if(model.getBreed().equals(Breed)){
                            list.add(model);

                        }
                    }if(Size!=null &&Type==null && Breed.equals("")  && Age.equals("") && Item==null){
                        if(model.getSize().equals(Size)){
                            list.add(model);

                        }
                    }if(Age!=null && Type==null && Breed.equals("") && Size.equals("")  && Item==null){
                        if(model.getAge().equals(Age)){
                            list.add(model);

                        }
                    }if(Item!=null && Type==null && Breed.equals("") && Size.equals("") && Age.equals("")){
                        if(model.getGender().equals(Item)){
                            list.add(model);

                        }
                    }if(Type!=null && Breed.equals("") && Size!=null && Age.equals("") && Item==null){
                        if(model.getPetName().equals(Type) && model.getSize().equals(Size)){
                            list.add(model);

                        }
                    }if(Type!=null && Breed.equals("") && Size.equals("") && Age!=null && Item==null){
                        if(model.getPetName().equals(Type) && model.getAge().equals(Age)){
                            list.add(model);

                        }
                    }if(Type!=null && Breed.equals("") && Size.equals("") && Age.equals("") && Item!=null){
                        if(model.getPetName().equals(Type) && model.getGender().equals(Item)){
                            list.add(model);

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                extra.showToast(error.getMessage());
            }
        });

    }

    private void dislikePost(Pet pet) {
        database.RemoveFavourite(
                pet.getId()).addOnSuccessListener(suc -> {
            extra.cancelProgressDialog();

        }).addOnFailureListener(er -> {
            extra.cancelProgressDialog();
            Toast.makeText(getContext(), er.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    void getAllData() {
        list = new ArrayList<>();
        List<Pet> temp = new ArrayList<>();
        List<Pet> temp1 = new ArrayList<>();
        database.getDataFavourite().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot FetchData : snapshot.getChildren()) {
                    Pet model = FetchData.getValue(Pet.class);
                    model.setId(FetchData.getKey());
                    temp.add(model);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                extra.showToast(error.getMessage());
            }
        });
        database.getData().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot FetchData : snapshot.getChildren()) {
                    Pet model = FetchData.getValue(Pet.class);
                    model.setId(FetchData.getKey());
                    temp1.add(model);
                    ActivitySplash.list=temp1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                extra.cancelProgressDialog();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (Pet pet1 : temp1) {
                    boolean idMatched = false;
                    for (Pet pet2 : temp) {
                        if (pet1.getId().equals(pet2.getId())) {
                            idMatched = true;
                            break;
                        }
                    }
                    if (!idMatched) {
                        extra.cancelProgressDialog();
                        list.add(pet1);
                    }
                    extra.cancelProgressDialog();
                }

            }
        }, 2000);


    }

    void likedPost(Pet pet) {
        database.addFavourite(pet, pet.getId()).addOnSuccessListener(suc -> {
            onStateChange.onStateChange();
            extra.cancelProgressDialog();
        }).addOnFailureListener(er -> {
            extra.cancelProgressDialog();
            Toast.makeText(getContext(), er.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            onStateChange = (OnStateChange) context;
        } else {
            throw new RuntimeException("MainActivityInterface is not implemented in MainActivity");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}