package com.example.pawfectmatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ActivityPetDetail extends AppCompatActivity {

    TextView tv_DetailPetCategory,tv_PetCategory,tv_PetFeatures,tv_PetAge,tv_PetBreed,tv_PetGender,tv_PetSize,tv_PetDetails;
    LinearLayout ll_PetDetail;
    Pet pet;
    Button  btn_Location,btn_ContactOwner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);
        pet = (Pet) getIntent().getSerializableExtra("data");
        tv_DetailPetCategory=findViewById(R.id.tv_DetailPetCategory);
        tv_PetCategory=findViewById(R.id.tv_PetCategory);
        tv_PetFeatures=findViewById(R.id.tv_PetFeatures);
        tv_PetAge=findViewById(R.id.tv_PetAge);
        tv_PetBreed=findViewById(R.id.tv_PetBreed);
        tv_PetGender=findViewById(R.id.tv_PetGender);
        tv_PetSize=findViewById(R.id.tv_PetSize);
        tv_PetDetails=findViewById(R.id.tv_PetDetails);
        ll_PetDetail=findViewById(R.id.ll_PetDetail);
        btn_Location=findViewById(R.id.btn_Location);
        btn_ContactOwner=findViewById(R.id.btn_ContactOwner);
        setDataField();
        btn_Location.setOnClickListener(v->{
            loadLocation();
        });
        btn_ContactOwner.setOnClickListener(v1->{
            dialerPad();
        });

    }

    private void dialerPad() {
    Intent intent=new Intent(Intent.ACTION_DIAL);
    intent.setData(Uri.parse("tel:"+pet.getPhone()));
    startActivity(intent);
    }

    private void loadLocation() {
        Intent intent=new Intent(getApplicationContext(),ShowMap.class);
        intent.putExtra("data",pet);
        startActivity(intent);
    }

    private void setDataField() {
        tv_DetailPetCategory.setText(pet.getPetName());
        tv_PetCategory.setText(pet.getPetName());
        tv_PetFeatures.setText(pet.getBestHabit());
        tv_PetAge.setText(pet.getAge());
        tv_PetBreed.setText(pet.getBreed());
        tv_PetGender.setText(pet.getGender());
        tv_PetSize.setText(pet.getSize());
        tv_PetDetails.setText(pet.getDescription());
        Picasso.get()
                .load(pet.getImage())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        ll_PetDetail.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        // Handle failed loading or set a default background if needed
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        // Optional: You can set a placeholder drawable before the image loads
                    }
                });
    }
}