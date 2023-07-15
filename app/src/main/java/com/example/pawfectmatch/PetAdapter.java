package com.example.pawfectmatch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {
    private Context context;
    private List<Pet> petList;
    Extra extra;
    DAB database;
    OnStateChange onStateChange;


    public PetAdapter(Context context, List<Pet> petList,OnStateChange onStateChange) {
        this.context = context;
        this.petList = petList;
        database = new DAB();
        extra = new Extra(context);
        this.onStateChange=onStateChange;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflateL = LayoutInflater.from(parent.getContext());
        View view = inflateL.inflate(R.layout.row_view_saved_pet, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pet pet = petList.get(position);
        holder.tvName.setText(pet.getPetName());
        Picasso.get().load(pet.getImage()).into(holder.ivDog);
        holder.tvQuality.setText(pet.getBestHabit());
        holder.ivHeart.setColorFilter(Color.parseColor("#FF0000"));
        holder.ivHeart.setOnClickListener(v -> {
            extra.showProgressDialog("please wait...", "DisLiked");
            database.RemoveFavourite(
                    pet.getId()).addOnSuccessListener(suc->{
                extra.cancelProgressDialog();

            }).addOnFailureListener(er->{
                extra.cancelProgressDialog();
                Toast.makeText(v.getContext(), er.getMessage(), Toast.LENGTH_SHORT).show();
            });
            petList.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(position);;
            onStateChange.onStateChangeFavourite();

        });
        holder.ivDog.setOnClickListener(v->{
            Intent intent=new Intent(context,ActivityPetDetail.class);
            intent.putExtra("data",pet);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivDog, ivHeart;
        TextView tvName, tvQuality;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDog = itemView.findViewById(R.id.ivDog);
            ivHeart = itemView.findViewById(R.id.ivHeart);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuality = itemView.findViewById(R.id.tvQuality);
        }
    }
}
