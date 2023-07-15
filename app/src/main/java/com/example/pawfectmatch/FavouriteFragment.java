package com.example.pawfectmatch;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private RecyclerView rv_PetRecyclerView;
    private List<Pet> list = new ArrayList<>();
    ;
    OnStateChange onStateChange;
    PetAdapter adapter;
    DAB database;
    Extra extra;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        database = new DAB();
        extra = new Extra(getContext());

        if (ActivitySplash.listFavourite.size() == 0) {
            view = inflater.inflate(R.layout.is_null_layout, container, false);

        } else {
            view = inflater.inflate(R.layout.fragment_favrate, container, false);
            rv_PetRecyclerView = view.findViewById(R.id.rv_PetRecyclerView);
            getAllData();
            extra.showProgressDialog("please wait...", "Data Load");
            rv_PetRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            ActivitySplash.listFavourite=list;
            adapter = new PetAdapter(getContext(), list, onStateChange);
            rv_PetRecyclerView.setAdapter(adapter);
        }
        return view;

    }

    void getAllData() {

        database.getDataFavourite().addValueEventListener(new ValueEventListener() {
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
