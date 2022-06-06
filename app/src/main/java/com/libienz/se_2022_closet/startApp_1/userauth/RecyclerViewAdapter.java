package com.libienz.se_2022_closet.startApp_1.userauth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ClothesViewHolder>{

    private ArrayList<Clothes> mClothesList;
    private FirebaseStorage storage;
    private Context context;

    public RecyclerViewAdapter(ArrayList<Clothes> mClothesList, Context context)
    {
        this.mClothesList = mClothesList;
        this.context=context;
        storage = FirebaseStorage.getInstance();
        notifyDataSetChanged();
    }
    /*
    public RecyclerViewAdapter(ArrayList<Clothes> mClothesList, Context context) {
        this.mClothesList = mClothesList;
        this.context = context;
    }*/

    @NonNull
    @Override
    public ClothesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clothes_items, parent, false);
        ClothesViewHolder holder = new ClothesViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClothesViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(mClothesList.get(position).getClothesImg())
                .into(holder.iv_clothes);
    }

    @Override
    public int getItemCount() {
        return (mClothesList!=null ? mClothesList.size() : 0);
    }

    public class ClothesViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_clothes;

        public ClothesViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_clothes=itemView.findViewById(R.id.iv_clothes);
        }
    }
}

