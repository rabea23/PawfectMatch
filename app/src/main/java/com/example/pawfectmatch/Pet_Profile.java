package com.example.pawfectmatch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Pet_Profile extends Fragment {

    private RecyclerView rv_PetRecyclerView;
    private List<Pet> list;
    private Button addNew;
    OnStateChange onStateChange;
    DAB database = new DAB();
    ProfileAdapter adapter;
    Extra extra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_profile, container, false);
        rv_PetRecyclerView = view.findViewById(R.id.rv_PetRecyclerView);
        addNew = view.findViewById(R.id.addNew);
        list = new ArrayList<>();
        database = new DAB();
        extra = new Extra(getContext());
        addNew.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ActivityAddPet.class);
            startActivity(intent);
        });
        extra.showProgressDialog("Please wait", "Data load");
        getAllData();
        rv_PetRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapter = new ProfileAdapter(list, getContext(), onStateChange);
        rv_PetRecyclerView.setAdapter(adapter);
        return view;

    }

    void getAllData() {
        database.getDataPersonal().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot FetchData : snapshot.getChildren()) {
                    Pet model = FetchData.getValue(Pet.class);
                    model.setId(FetchData.getKey());
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
                extra.cancelProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                extra.cancelProgressDialog();
            }
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
}