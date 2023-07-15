package com.example.pawfectmatch;

import android.app.ProgressDialog;
import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Extra {
    private Context context;
    private ProgressDialog progressDialog;
    public Extra(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    public void showProgressDialog(String Message, String Title) {
        progressDialog.setTitle(Title);
        progressDialog.setMessage(Message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public String getDeviceId()
    {
       String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    public void cancelProgressDialog() {
        progressDialog.dismiss();
    }

    public void showToast(String Message) {
        Toast.makeText(context, Message, Toast.LENGTH_LONG).show();
    }

}
