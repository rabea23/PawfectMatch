package com.example.pawfectmatch;


import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAB {
    private DatabaseReference databaseReference, databaseReferencePersonal,databaseReferenceFavouritie;

    Context context;

    public DAB() {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReferencePersonal = db.getReference("PersonalDevice").child(ActivitySplash.deviceId);
        databaseReference = db.getReference("AllPet");
        databaseReferenceFavouritie = db.getReference("Favourite").child(ActivitySplash.deviceId);
    }

    public Task<Void> addLive(Pet model) {
        return databaseReference.push().setValue(model);
    }

    public Task<Void> addPersonal(Pet model, String id) {
        return databaseReferencePersonal.child(id).setValue(model);
    }
    public Task<Void> addFavourite(Pet model, String id) {
        return databaseReferenceFavouritie.child(id).setValue(model);
    }

    public Query getData() {
        return databaseReference.orderByKey();
    }

    public Query getDataPersonal() {
        return databaseReferencePersonal.orderByKey();
    }
    public Query getDataFavourite() {
        return databaseReferenceFavouritie.orderByKey();
    }

    public Task<Void> RemovePersonal(String key) {
        return databaseReferencePersonal.child(key).removeValue();
    }
    public Task<Void> RemoveFavourite(String key) {
        return databaseReferenceFavouritie.child(key).removeValue();
    }
    public Task<Void> RemoveLive(String key) {
        return databaseReference.child(key).removeValue();
    }

    public Task<Void> updateLive(String key, HashMap hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> updatePersonal(String key, HashMap hashMap) {
        return databaseReferencePersonal.child(key).updateChildren(hashMap);
    }
    public Task<Void> updateFavourite(String key, HashMap hashMap) {
        return databaseReferenceFavouritie.child(key).updateChildren(hashMap);
    }

}
