package com.libienz.se_2022_closet.startApp_1.clothes;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ClothesViewHolder>{

    private ArrayList<Clothes> mClothesList;
    private FirebaseStorage storage;
    private Context context;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("clothes").child(user.getUid());


    //아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View view, int position); //뷰와 포지션값
    }
    //리스너 객체 참조 변수
    private OnItemClickListener mListener = null;

    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public RecyclerViewAdapter(){

    }

    public RecyclerViewAdapter(ArrayList<Clothes> mClothesList, Context context)
    {
        this.mClothesList = mClothesList;
        this.context=context;
        //storage = FirebaseStorage.getInstance();
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ClothesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clothes_items, parent, false);
        ClothesViewHolder holder = new ClothesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClothesViewHolder holder, int position) {

        if (storageReference != null) {
            StorageReference submitReference = storageReference.child(mClothesList.get(position).getClothesKey() + ".png");
            submitReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide.with(holder.itemView)
                            .load(uri).
                            into(holder.iv_clothes);
                }
            });
        }
        holder.tv_clotheskey.setText(mClothesList.get(position).getClothesInfo());

    }

    @Override
    public int getItemCount() {

        if (mClothesList != null) {
            //Log.d("itemCount", "getItemCount: "+mClothesList.size());
            return mClothesList.size();
        }
        return 0;
    }



    public class ClothesViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_clothes;
        TextView tv_clotheskey;

        public ClothesViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_clothes=itemView.findViewById(R.id.iv_clothes);
            tv_clotheskey=itemView.findViewById(R.id.tv_clotheskey);

            itemView.setClickable(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // TODO : use pos.
                        mListener.onItemClick(v, pos);

                        //Log.d("onclickrecycleritem", "onClick: key"+mClothesList.get(pos).getClothesKey());
                    }

                }
            });
        }
    }
}

