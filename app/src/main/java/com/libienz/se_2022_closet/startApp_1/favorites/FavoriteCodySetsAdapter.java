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
    private FavoriteCodySetsAdapter.OnItemClickListener myListener = null;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("clothes").child(user.getUid());

    //아이템 뷰를 저장하는 뷰 홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView tv_codyKey = (TextView) itemView.findViewById(R.id.viewCodyKey_tv);
        TextView tv_codyTag = (TextView) itemView.findViewById(R.id.viewCodyTag_tv);

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

    public FavoriteCodySetsAdapter(ArrayList<Cody> list) { favCody = list; }

    //아이템 뷰를 위한 뷰 홀더 객체 생성해서 리턴
    @Override
    public FavoriteCodySetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_cody, parent, false);
        FavoriteCodySetsAdapter.ViewHolder viewHolder = new FavoriteCodySetsAdapter.ViewHolder(view);

        return viewHolder;
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

    //검색 결과 클릭 시 열람 페이지로 넘어가게 하기 위한 리스너 정의
    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    //리스너 객체를 전달하는 메소드 정의
    public void setOnItemClickListener(FavoriteCodySetsAdapter.OnItemClickListener listener){
        this.myListener = listener;
    }

}
