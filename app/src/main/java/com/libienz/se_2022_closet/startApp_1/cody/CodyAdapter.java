package com.libienz.se_2022_closet.startApp_1.cody;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;
import com.libienz.se_2022_closet.startApp_1.data.Cody;

import java.util.ArrayList;

public class CodyAdapter extends RecyclerView.Adapter<CodyAdapter.ViewHolder> {

    private ArrayList<Cody> findRes = null;
    private OnItemClickListener myListener = null;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("clothes").child(user.getUid());

    //아이템 뷰를 저장하는 뷰 홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView viewCodyKey_tv = (TextView) itemView.findViewById(R.id.viewCodyKey_tv);
        TextView viewCodyTag_tv = (TextView) itemView.findViewById(R.id.viewCodyTag_tv);

        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.searchResult_ll);

            //아이템 뷰 클릭 리스너
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (myListener != null) myListener.onItemClick(v, pos);
                    }
                }
            });
        }
    }

    public CodyAdapter(ArrayList<Cody> list) { findRes = list; }

    //아이템 뷰를 위한 뷰 홀더 객체 생성해서 리턴
    @Override
    public CodyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_cody, parent, false);
        CodyAdapter.ViewHolder viewHolder = new CodyAdapter.ViewHolder(view);

        return viewHolder;
    }

    //포지션에 해당하는 데이터를 뷰 홀더의 아이템 뷰에 표시
    @Override
    public void onBindViewHolder (CodyAdapter.ViewHolder viewHolder, int position) {
        String key = findRes.get(position).getCodyKey();
        viewHolder.viewCodyKey_tv.setText(key);

        ArrayList<String> tag = findRes.get(position).getCodyTag();
        viewHolder.viewCodyTag_tv.setText(null);
        for (String Tag : tag) {
            viewHolder.viewCodyTag_tv.append("  #" + Tag);
        }
    }

    //전체 데이터 개수 리턴
    @Override
    public int getItemCount() {
        return findRes.size();
    }

    //검색 결과 클릭 시 열람 페이지로 넘어가게 하기 위한 리스너 정의
    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    //리스너 객체를 전달하는 메소드 정의
    public void setOnItemClickListener(OnItemClickListener listener){
        this.myListener = listener;
    }
}