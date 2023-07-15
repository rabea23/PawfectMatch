package com.example.pawfectmatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.telecom.Call;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityAddPet extends AppCompatActivity {

    private EditText edtSize, edtBreed, edtWeight, edtGender, edtLocation, edtNumber, edtSpecifications, edtDetails, edtAge;
    private ImageView ivImage,ivBack;
    private Button btnAdd;
    TextView tvFilterHeading;
    FirebaseStorage mStore;
    Extra extra;
    double Lat, Lng;
    String Type, Size, Breed, weight, gender, location, number, specification, details, age, Id;
    DAB obj;
    Pet tempModel = new Pet();
    List<Pet> tempList = null;
    Pet editDataModel;
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    String image;
    public static Uri uri;
    OnStateChange onStateChange;
    String[] items = {"Dog", "Cat", "Birds"};


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (data.getData() != null) {
                uri = data.getData();
                ivImage.setImageURI(uri);
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);


        edtSize = findViewById(R.id.edtSize);
        edtBreed = findViewById(R.id.edtBreed);
        edtWeight = findViewById(R.id.edtWeight);
        edtGender = findViewById(R.id.edtGender);
        edtAge = findViewById(R.id.edtAge);
        edtLocation = findViewById(R.id.edtLocation);
        edtNumber = findViewById(R.id.edtNumber);
        edtSpecifications = findViewById(R.id.edtSpecifications);
        edtDetails = findViewById(R.id.edtDetails);
        ivImage = findViewById(R.id.ivImage);
        ivBack = findViewById(R.id.ivBack);
        tvFilterHeading = findViewById(R.id.tvFilterHeading);
        btnAdd = findViewById(R.id.btnAdd);
        obj = new DAB();

        Type = getIntent().getStringExtra("Type");
        image = getIntent().getStringExtra("uri");
        Size = getIntent().getStringExtra("Size");
        Breed = getIntent().getStringExtra("Breed");
        weight = getIntent().getStringExtra("weight");
        gender = getIntent().getStringExtra("gender");
        age = getIntent().getStringExtra("age");
        location = getIntent().getStringExtra("Address");
        number = getIntent().getStringExtra("number");
        specification = getIntent().getStringExtra("specification");
        details = getIntent().getStringExtra("details");
        Lat = getIntent().getDoubleExtra("Lat", 0);
        Lng = getIntent().getDoubleExtra("Lng", 0);

        mStore = FirebaseStorage.getInstance();
        tempList = new ArrayList<>();
        extra = new Extra(ActivityAddPet.this);
        editDataModel = (Pet) getIntent().getSerializableExtra("data");
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        autoCompleteTxt.setText("Dog");
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTxt.setAdapter(adapterItems);
        setData();
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Type: " + item, Toast.LENGTH_SHORT).show();
                Type = item;
            }
        });

        if (editDataModel == null) {
            editDataModel = (Pet) getIntent().getSerializableExtra("editDataModel");
        }
        if (editDataModel != null) {
            edtSize.setText(editDataModel.getSize());
            edtBreed.setText(editDataModel.getBreed());
            edtWeight.setText(editDataModel.getWeight());
            edtGender.setText(editDataModel.getGender());
            edtAge.setText(editDataModel.getAge());
            edtNumber.setText(editDataModel.getPhone());
            edtSpecifications.setText(editDataModel.getBestHabit());
            edtDetails.setText(editDataModel.getDescription());
            Picasso.get().load(editDataModel.getImage()).into(ivImage);
            btnAdd.setText("Update");
            tvFilterHeading.setText("Edite Pet Detail");
        }


        edtLocation.setOnClickListener(v -> {
            getTextAll();
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            intent.putExtra("Type", Type);
            intent.putExtra("Size", Size);
            intent.putExtra("Breed", Breed);
            intent.putExtra("weight", weight);
            intent.putExtra("age", age);
            intent.putExtra("gender", gender);
            intent.putExtra("specification", specification);
            intent.putExtra("details", details);
            intent.putExtra("number", number);
            intent.putExtra("editDataModel", editDataModel);
            intent.putExtra("image", uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        btnAdd.setOnClickListener(v -> {
            if (btnAdd.getText().equals("Update")) {
                extra.showProgressDialog("please wait", "Update successfully");
                getTextAll();
                if (uri != null && Lat != 0 && Lng != 0 && Type != "") {
                    StorageReference filePath = mStore.getReference().child("ImagePost").child(uri.getLastPathSegment());
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    HashMap m1 = new HashMap();
                                    m1.put("age", age);
                                    m1.put("bestHabit", specification);
                                    m1.put("breed", Breed);
                                    m1.put("description", details);
                                    m1.put("gender", gender);
                                    m1.put("image", task.getResult().toString());
                                    m1.put("lat", Lat);
                                    m1.put("like", true);
                                    m1.put("lng", editDataModel.Lng);
                                    m1.put("size", Size);
                                    m1.put("weight", weight);
                                    m1.put("petName", Type);
                                    Toast.makeText(ActivityAddPet.this, "" + editDataModel.getId(), Toast.LENGTH_SHORT).show();
                                    obj.updatePersonal(editDataModel.getId(), m1).addOnSuccessListener(suc -> {
                                    }).addOnFailureListener(fail -> {
                                        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                                        extra.cancelProgressDialog();
                                    });
                                    obj.updateLive(editDataModel.getId(), m1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            extra.cancelProgressDialog();
                                            Toast.makeText(getApplicationContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                            intent.putExtra("value","1");
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            extra.cancelProgressDialog();
                                            Toast.makeText(ActivityAddPet.this, "update fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ActivityAddPet.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ActivityAddPet.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    extra.cancelProgressDialog();
                    Toast.makeText(this, "image or Location check please", Toast.LENGTH_SHORT).show();
                }
            } else {

                extra.showProgressDialog("Please wait", "Insertion Pet Data");
                validation();
                getTextAll();

                if (uri != null && Lat != 0 && Lng != 0) {

                    StorageReference filePath = mStore.getReference().child("ImagePost").child(uri.getLastPathSegment());
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    Pet pet = new Pet(Type, specification, age, gender, Breed, Size, details, task.getResult().toString(), number, weight, Lat, Lng, false);

                                    obj.addLive(pet).addOnSuccessListener(suc -> {
                                        extra.showToast("Insertion Successfully");
                                    }).addOnFailureListener(er -> {
                                        Toast.makeText(getApplicationContext(), er.getMessage(), Toast.LENGTH_SHORT).show();
                                        extra.cancelProgressDialog();
                                    });

                                    obj.getData().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot FetchData : snapshot.getChildren()) {
                                                tempModel = FetchData.getValue(Pet.class);
                                                tempModel.setId(FetchData.getKey());
                                                tempList.add(tempModel);
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
                                            if (tempList.size() == 1) {
                                                Id = tempModel.getId();
                                                callPersonal(pet, Id);
                                            } else {
                                                Pet temp = tempList.get(tempList.size() - 1);
                                                Id = temp.getId();
                                                callPersonal(pet, Id);
                                            }
                                        }
                                    }, 5000);


                                }

                            });
                        }
                    });
                } else {
                    extra.cancelProgressDialog();
                    extra.showToast("add image please");
                    extra.showToast("some fields are missing");
                }
            }

        });


        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 45);

            }
        });
        ivBack.setOnClickListener(v->{
            Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
            finish();
        });

    }

    private void callPersonal(Pet pet, String Id) {
        Toast.makeText(ActivityAddPet.this, "" + Id, Toast.LENGTH_SHORT).show();
        obj.addPersonal(pet, Id).addOnSuccessListener(suc -> {
            extra.showToast("done");
            finish();
            extra.cancelProgressDialog();
        }).addOnFailureListener(er -> {
            Toast.makeText(getApplicationContext(), er.getMessage(), Toast.LENGTH_SHORT).show();
            extra.cancelProgressDialog();
        });

    }

    private void setData() {
        edtSize.setText(Size);
        edtBreed.setText(Breed);
        edtWeight.setText(weight);
        edtGender.setText(gender);
        edtAge.setText(age);
        edtLocation.setText(location);
        edtDetails.setText(details);
        edtNumber.setText(number);
        edtSpecifications.setText(specification);
        autoCompleteTxt.setText(Type);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Type: " + item, Toast.LENGTH_SHORT).show();
                Type = item;
            }
        });
    }

    private void validation() {
        if (edtSize.getText().toString().isEmpty()) {
            edtSize.setError("please fill this filed");
            return;
        } else if (edtBreed.getText().toString().isEmpty()) {
            edtBreed.setError("please fill this filed");
            return;
        } else if (edtWeight.getText().toString().isEmpty()) {
            edtWeight.setError("please fill this filed");
            return;
        } else if (edtGender.getText().toString().isEmpty()) {
            edtGender.setError("please fill this filed");
            return;
        } else if (edtLocation.getText().toString().isEmpty()) {
            edtLocation.setError("please fill this filed");
            return;
        } else if (edtNumber.getText().toString().isEmpty()) {
            edtNumber.setError("please fill this filed");
            return;
        } else if (edtSpecifications.getText().toString().isEmpty()) {
            edtSpecifications.setError("please fill this filed");
            return;
        } else if (edtDetails.getText().toString().isEmpty()) {
            edtDetails.setError("please fill this filed");
            return;
        } else if (edtAge.getText().toString().isEmpty()) {
            edtAge.setError("please fill this filed");
            return;
        } else if (uri == null) {
            Toast.makeText(this, "please add image", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void getTextAll() {
        Size = edtSize.getText().toString();
        Breed = edtBreed.getText().toString();
        weight = edtWeight.getText().toString();
        gender = edtGender.getText().toString();
        location = edtLocation.getText().toString();
        number = edtNumber.getText().toString();
        specification = edtSpecifications.getText().toString();
        details = edtDetails.getText().toString();
        age = edtAge.getText().toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}