package com.libienz.se_2022_closet.startApp_1.cody;

import static com.libienz.se_2022_closet.startApp_1.util.FirebaseReference.userRef;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
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
import com.libienz.se_2022_closet.startApp_1.clothes.RecyclerViewAdapter;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;
import com.libienz.se_2022_closet.startApp_1.data.Cody;

import java.security.SecureRandom;
import java.util.ArrayList;

public class editCodyFrag extends Fragment {

    private ArrayList<String> hashtag = new ArrayList<>(10);
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String prevCodyname = null;
    private String newCodyname = null;
    private String clothesKey = null;
    private String CodyKey; //수정할 코디 세트의 key
    private RecyclerViewAdapter adapter;
    private ArrayList<Clothes> codyCompCl;
    private ArrayList<Clothes> clothes;
    private ArrayList<String> codyComp = new ArrayList<>();
    private ArrayList<String> hashTag = new ArrayList<>();
    private TextView showeditTag_tv;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_cody, container, false);

        //CodyKey = "cody0";
        //수정할 코디 세트의 key를 readCodyFrag로부터 받아옴
        CodyKey = getArguments().getString("CodyKey");
        //TODO : 코디 세트 수정 기능 만들기...
        //완료 : Layout도 만들기...

        EditText editCodyname_et = (EditText) view.findViewById(R.id.editCodyname_et);
        Button doneeditCody_btn = (Button) view.findViewById(R.id.doneeditCody_btn);

        //코디 정보를 긁어오기
        userRef.child(user.getUid()).child("Cody").child(CodyKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Cody cody = snapshot.getValue(Cody.class);
                prevCodyname = cody.getCodyKey();
                codyComp = cody.getCodyComp();
                hashTag = cody.getCodyTag();

                showeditTag_tv = (TextView) view.findViewById(R.id.showeditTag_tv);
                for (int i = 0; i < hashTag.size(); i++)
                    showeditTag_tv.append("#" + hashTag.get(i) + " ");
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
                    codyCompCl.add(snapshot.getValue(Clothes.class));
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
        editCodycomp_rv.setHasFixedSize(true);
        editCodycomp_rv.scrollToPosition(0);
        context = container.getContext();
        clothes = new ArrayList<>();
        adapter = new RecyclerViewAdapter(clothes, context);

        userRef.child(user.getUid()).child("Clothes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clothes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //여러값을 하나씩 불러옴
                    Clothes cl = snapshot.getValue(Clothes.class);
                    clothes.add(cl);

                    Log.d("ReadAllClothesFrag", "Single ValueEventListener : " + snapshot.getValue());
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editCodycomp_rv.setAdapter(adapter);
        editCodycomp_rv.setItemAnimator(new DefaultItemAnimator());

        //RecyclerViewAdapter에서 선택한 의류의 포지션을 받아온다
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                String clothesKey = clothes.get(pos).getClothesKey();
                boolean check = false;

                for (int i = 0; i < codyComp.size(); i++){
                    if(codyComp.get(i).equals(clothesKey)){
                        check = true;
                        break;
                    }
                }

                if(check){ //Codycomp에 없는 clothesKey이면 Codycomp에 추가한다
                    codyComp.remove(clothesKey);
                    Toast.makeText(container.getContext(), "코디에 의류를 제거하였습니다.", Toast.LENGTH_SHORT).show();
                }
                else { //Codycomp에 있는 clothesKey이면 Codycomp에서 아웃
                    codyComp.add(clothesKey);
                    Toast.makeText(container.getContext(), "코디에 의류를 추가하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //이미 있는 해시태그를 출력, 있는 해시태그를 또 작성하면 삭제 & 없는 해시태그를 작성하면 추가
        Button editHashTag_btn3 = (Button) view.findViewById(R.id.editHashTag_btn3);
        EditText editCodytag_et = (EditText) view.findViewById(R.id.editCodytag_et);

        editHashTag_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newtag = editCodytag_et.getText().toString();
                boolean check = false;

                if(newtag !=  null) {
                    for (int i = 0; i < hashTag.size(); i++) {
                        if (hashTag.get(i).equals(newtag)) {
                            check = true;
                            break;
                        }
                    }
                }
                if(check) {
                    hashTag.remove(newtag);
                    Toast.makeText(container.getContext(), "태그를 제거하였습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    hashTag.add(newtag);
                    Toast.makeText(container.getContext(), "태그를 추가하였습니다.", Toast.LENGTH_SHORT).show();
                }
                showeditTag_tv.setText(null);
                for (int i = 0; i < hashTag.size(); i++) {
                    showeditTag_tv.append("#" + hashTag.get(i) + " ");
                }
            }
        });




        //코디 세트 이름 수정과 등록
        doneeditCody_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCodyname = editCodyname_et.getText().toString();

                if(newCodyname.isEmpty()){ newCodyname = prevCodyname; }

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
        Cody cody = new Cody(Key, Comp, Tag, false);

        //파이어베이스 리얼타임 데이터베이스에 의류 정보 저장
        userRef.child(idToken).child("Cody").child(Key).setValue(cody);
    }
}