package com.example.pawfectmatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private List<Pet>list;
    private Context context;
    OnStateChange onStateChange;

    public ProfileAdapter(List<Pet> list, Context context,OnStateChange onStateChange) {
        this.list = list;
        this.context = context;
        this.onStateChange=onStateChange;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.row_view_pet_profile,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Pet pet=list.get(position);
        Picasso.get().load(pet.getImage()).into(holder.iv_PetImage);
        holder.tv_PetCategory.setText(pet.getPetName());

        holder.tv_PetFeatures.setText(pet.getBestHabit());

        holder.iv_Menu.setOnClickListener(v->{

            Context context1 = new ContextThemeWrapper(holder.iv_Menu.getContext(), R.style.PopupMenuStyle);
            PopupMenu popupMenu = new PopupMenu(context1, holder.iv_Menu);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popupMenu.setForceShowIcon(true);
            }
            popupMenu.inflate(R.menu.folde_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.deleteFolder:
                            list.remove(position);
                            DAB obj=new DAB();
                            obj.RemovePersonal(pet.getId()).addOnSuccessListener(suc->{
                            }).addOnFailureListener(er->{
                                Toast.makeText(v.getContext(), er.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                            obj.RemoveLive(pet.getId()).addOnSuccessListener(suc->{
                                Toast.makeText(v.getContext(), "Record id Deleted", Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(position);
                            }).addOnFailureListener(er->{
                                Toast.makeText(v.getContext(), er.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                            notifyItemRemoved(position);
                            notifyItemChanged(position);
                            onStateChange.onStateChangePersonal();
                            break;
                        case R.id.etd_folderName:
                            Intent intent=new Intent(context,ActivityAddPet.class);
                            intent.putExtra("data",pet);
                            context.startActivity(intent);
                            break;

                    }
                    return false;
                }
            });
            popupMenu.show();
        });
        holder.iv_PetImage.setOnClickListener(v->{
            Intent intent=new Intent(context,ActivityPetDetail.class);
            intent.putExtra("data",pet);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_PetImage,iv_Menu;
        TextView tv_PetCategory,tv_PetFeatures;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_PetImage =itemView.findViewById(R.id.iv_PetImage);
            tv_PetCategory =itemView.findViewById(R.id.tv_PetCategory);
            tv_PetFeatures =itemView.findViewById(R.id.tv_PetFeatures);
            iv_Menu =itemView.findViewById(R.id.iv_Menu);
        }
    }
}
