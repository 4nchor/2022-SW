package com.libienz.se_2022_closet.startApp_1.favorites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.cody.CodyAdapter;
import com.libienz.se_2022_closet.startApp_1.data.Cody;

import java.util.ArrayList;

public class FavoriteCodySetsAdapter extends RecyclerView.Adapter<FavoriteCodySetsAdapter.ViewHolder> {

    private ArrayList<Cody> favCody = null;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("clothes").child(user.getUid());
    private Context context;

    //아이템 뷰를 저장하는 뷰 홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView tv_codyKey;
        TextView tv_codyTag;

        ViewHolder(View itemView) {
            super(itemView);
            tv_codyKey = (TextView) itemView.findViewById(R.id.viewCodyKey_tv);
            tv_codyTag = (TextView) itemView.findViewById(R.id.viewCodyTag_tv);
        }
    }

    public FavoriteCodySetsAdapter(ArrayList<Cody> list, Context context) {
        this.favCody = list;
        this.context = context;
        notifyDataSetChanged();
    }

    //아이템 뷰를 위한 뷰 홀더 객체 생성해서 리턴
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cody, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //포지션에 해당하는 데이터를 뷰 홀더의 아이템 뷰에 표시
    @Override
    public void onBindViewHolder (FavoriteCodySetsAdapter.ViewHolder viewHolder, int position) {
        String key = favCody.get(position).getCodyKey();
        viewHolder.tv_codyKey.setText(key);

        ArrayList<String> tag = favCody.get(position).getCodyTag();
        viewHolder.tv_codyTag.setText(null);
        for (String Tag : tag) {
            viewHolder.tv_codyTag.append("  #" + Tag);
        }

    }

    //전체 데이터 개수 리턴
    @Override
    public int getItemCount() {
        return favCody.size();
    }


}
