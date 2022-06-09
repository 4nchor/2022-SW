package com.libienz.se_2022_closet.startApp_1.cody;

import static com.libienz.se_2022_closet.startApp_1.util.FirebaseReference.userRef;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.clothes.ClothesAdapter;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;
import com.libienz.se_2022_closet.startApp_1.data.Cody;

import java.security.SecureRandom;
import java.util.ArrayList;

public class editCodyFrag extends Fragment {

    private ArrayList<String> hashtag = new ArrayList<>(10);
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String newCodyname = null;
    private String clothesKey = null;
    private String CodyKey; //수정할 코디 세트의 key
    private ClothesAdapter adapter;
    private ArrayList<Clothes> clothes = new ArrayList<>();
    private ArrayList<String> codyComp = new ArrayList<>();
    private ArrayList<String> hashTag = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_cody, container, false);

        //수정할 코디 세트의 key를 readCodyFrag로부터 받아옴
        CodyKey = getArguments().getString("CodyKey");
        //TODO : 코디 세트 수정 기능 만들기...
        //TODO : Layout도 만들기...

        EditText editCodyname_et = (EditText) view.findViewById(R.id.editCodyname_et);
        Button doneeditCody_btn = (Button) view.findViewById(R.id.doneeditCody_btn);

        //코디에 있는 의류 key를 긁어오기
        userRef.child(user.getUid()).child("Cody").child(CodyKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Cody cody = snapshot.getValue(Cody.class);
                codyComp = cody.getCodyComp();
                hashTag = cody.getCodyTag();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(container.getContext(), "의류 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //codyComp(의류 키)를 통해 의류 클래스 뽑아오기..
        for(String key : codyComp) {
            userRef.child(user.getUid()).child("Clothes").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    clothes.add(snapshot.getValue(Clothes.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(container.getContext(), "의류 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }


        //리사이클러뷰와 어댑터 세팅
        RecyclerView editCodycomp_rv = (RecyclerView) view.findViewById(R.id.editCodycomp_rv);
        editCodycomp_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ClothesAdapter(clothes);
        editCodycomp_rv.setAdapter(adapter);

        //ClothesAdaper에서 선택한 의류의 포지션을 받아온다
        adapter.setOnItemClickListener(new ClothesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                clothesKey = clothes.get(pos).getClothesKey();
                
                //Codycomp에 없는 clothesKey이면 Codycomp에 추가한다
                for(String key : codyComp){
                    if(key != clothesKey){
                        codyComp.add(clothesKey);
                    }
                    else if(key == clothesKey) { //Codycomp에 있는 clothesKey이면 Codycomp에서 아웃
                        codyComp.remove(clothesKey);
                    }
                    else {

                    }
                }
            }
        });
        
        

        //코디 세트 이름 수정과 등록
        doneeditCody_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCodyname = editCodyname_et.getText().toString();

                //유저 정보 및 키워드, 태그 입력을 확인하고 코디 등록
                if (user != null && newCodyname != null && !hashTag.isEmpty()){
                    addCody(user.getUid(), codyComp, newCodyname, hashTag);
                    Toast.makeText(container.getContext(), "코디 정보가 등록되었습니다", Toast.LENGTH_SHORT).show();
                    codyComp.clear();
                    hashtag.clear();
                    editCodyname_et.setText(null);
                    //프래그먼트 종료, 수정하기 전 화면으로 돌아감
                    getParentFragmentManager().beginTransaction().remove(editCodyFrag.this).commit();
                }
                else if (newCodyname == null) {
                    Toast.makeText(container.getContext(), "코디 이름을을 등록해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(container.getContext(), "등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    public void addCody(String idToken, ArrayList<String> Comp, String Key, ArrayList<String> Tag) {
        Cody cody = new Cody(Key, Comp, Tag);

        //파이어베이스 리얼타임 데이터베이스에 의류 정보 저장
        userRef.child(idToken).child("Cody").child(Key).setValue(cody);
    }
}