package com.libienz.se_2022_closet.startApp_1.favorites;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.clothes.RecyclerViewAdapter;
import com.libienz.se_2022_closet.startApp_1.cody.CodyAdapter;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;
import com.libienz.se_2022_closet.startApp_1.data.Cody;

import java.util.ArrayList;

public class FavoriteClothesAdapter extends RecyclerView.Adapter<FavoriteClothesAdapter.ViewHolder>{

    private ArrayList<Clothes> favClothes = null;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("clothes").child(user.getUid());
    private Context context;

    //아이템 뷰를 저장하는 뷰 홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        //LinearLayout layout;
        ImageView iv_favclothes;
        TextView tv_clothesKey;

        ViewHolder(View itemView) {
            super(itemView);
            iv_favclothes = (ImageView) itemView.findViewById(R.id.iv_favclothes);
            tv_clothesKey= (TextView) itemView.findViewById(R.id.tv_clotheskey);
        }
    }

    public FavoriteClothesAdapter(ArrayList<Clothes> list, Context context) {
        this.favClothes = list;
        this.context=context;
        notifyDataSetChanged();
    }

    //아이템 뷰를 위한 뷰 홀더 객체 생성해서 리턴
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favclothes_items, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //포지션에 해당하는 데이터를 뷰 홀더의 아이템 뷰에 표시
    @Override
    public void onBindViewHolder (ViewHolder viewHolder, int position) {
        if (storageReference != null) {
            StorageReference submitReference = storageReference.child(favClothes.get(position).getClothesKey() + ".png");
            submitReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide.with(viewHolder.itemView).load(uri).into(viewHolder.iv_favclothes);
                }
            });
            viewHolder.tv_clothesKey.setText(favClothes.get(position).getClothesKey());
        }
        /*
        ArrayList<String> tag = favClothes.get(position).getClothesTag();
        viewHolder.tv_clothesKey.setText(null);
        for (String Tag : tag) {
            viewHolder.tv_clothesKey.append("  #" + Tag);
        }*/
    }

    //전체 데이터 개수 리턴
    @Override
    public int getItemCount() {
        return favClothes.size();
    }


}
