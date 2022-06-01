package com.libienz.se_2022_closet.startApp_1.clothes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;

import java.util.ArrayList;

public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.ViewHolder> {

    private ArrayList<Clothes> findRes = null;

    //아이템 뷰를 저장하는 뷰 홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView viewClothes_tv = (TextView) itemView.findViewById(R.id.viewClothes_tv);
        ImageView viewClothes_iv = (ImageView) itemView.findViewById(R.id.viewClothes_iv);

        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.searchResult_ll);
        }
    }

    ClothesAdapter(ArrayList<Clothes> list) { findRes = list; }

    //아이템 뷰를 위한 뷰 홀더 객체 생성해서 리턴
    @Override
    public ClothesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_clothes, parent, false);
        ClothesAdapter.ViewHolder viewHolder = new ClothesAdapter.ViewHolder(view);

        return viewHolder;
    }

    //포지션에 해당하는 데이터를 뷰 홀더의 아이템 뷰에 표시
    @Override
    public void onBindViewHolder(ClothesAdapter.ViewHolder viewHolder, int position) {
        //이미지 표시하는 부분 코드 짜야 함
        ArrayList<String> tag = findRes.get(position).gettag();
        viewHolder.viewClothes_tv.setText(null);
        for (String Tag : tag) {
            viewHolder.viewClothes_tv.append("  #" + Tag);
        }
    }

    //전체 데이터 개수 리턴
    @Override
    public int getItemCount() {
        return findRes.size();
    }
}
