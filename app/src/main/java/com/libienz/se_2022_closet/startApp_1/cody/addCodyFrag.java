package com.libienz.se_2022_closet.startApp_1.cody;

import static com.libienz.se_2022_closet.startApp_1.util.FirebaseReference.userRef;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.clothes.ClothesAdapter;
import com.libienz.se_2022_closet.startApp_1.clothes.RecyclerViewAdapter;
import com.libienz.se_2022_closet.startApp_1.clothes.addClothesFrag;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;
import com.libienz.se_2022_closet.startApp_1.data.Cody;
import com.libienz.se_2022_closet.startApp_1.userauth.MainActivity;

import java.util.ArrayList;


public class addCodyFrag extends Fragment {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<String> codycomp = new ArrayList<>();
    private ArrayList<String> hashtag = new ArrayList<>(10);
    private ArrayList<String> clothesKey = new ArrayList<>();
    private RecyclerView codyaddC_rv;
    private RecyclerViewAdapter adapter;
    private ArrayList<Clothes> clothes;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_cody, container, false);

        //완료 : 코디를 구성하는 의류를 입력받는 부분을 작성합니다. 입력받은 의류의 키를 ArrayList<String> codycomp에 집어넣는 것만 구현하면 됩니다
        //리사이클러뷰와 어댑터 세팅
        codyaddC_rv = (RecyclerView) view.findViewById(R.id.codyaddC_rv);
        codyaddC_rv.setHasFixedSize(true);

        codyaddC_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        codyaddC_rv.scrollToPosition(0);
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

        codyaddC_rv.setAdapter(adapter);
        codyaddC_rv.setItemAnimator(new DefaultItemAnimator());

        //RecyclerViewAdapter에서 선택한 의류의 포지션을 받아온다
        //여러 개를 선택한 후 의류 추가 버튼을 누르면 한번에 등록됩니다.
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                clothesKey.add(clothes.get(position).getClothesKey());
            }
        });

        //버튼을 누를 시 포지션에 해당하는 clotheskey를 codycomp에 add
        Button addCodyComp_btn = (Button) view.findViewById(R.id.addCodyComp_btn);
        addCodyComp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clothesKey.isEmpty()){
                    Toast.makeText(container.getContext(), "의류를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    for(String k : clothesKey){
                        codycomp.add(k);
                    }
                    Toast.makeText(container.getContext(), "의류를 추가하였습니다.", Toast.LENGTH_SHORT).show();
                }
                clothesKey.clear();
            }
        });


        //코디세트 이름(키워드)을 입력받음
        EditText addCodyKey_et = (EditText) view.findViewById(R.id.addCodyKey_et);

        //태그 정보를 입력받음
        EditText addCodyTag_et = (EditText) view.findViewById(R.id.addCodyTag_et);
        TextView showAddedCodyTag_tv = view.findViewById(R.id.showAddedCodyTag_tv);
        addCodyTag_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    hashtag.add(addCodyTag_et.getText().toString());
                    showAddedCodyTag_tv.append("#" + hashtag.get(hashtag.size() - 1) + " ");
                    addCodyTag_et.setText(null);
                    return true;
                }
                return false;
            }
        });

        //등록하기 버튼을 눌러 코디를 등록함
        Button addCody_btn = (Button) view.findViewById(R.id.doneAddCody_btn);
        addCody_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //유저 정보 및 키워드, 태그 입력을 확인하고 코디 등록
                if (user != null && addCodyKey_et.getText() != null && !hashtag.isEmpty()){
                    addCody(user.getUid(), codycomp, addCodyKey_et.getText().toString(), hashtag);
                    Toast.makeText(container.getContext(), "코디 정보가 등록되었습니다", Toast.LENGTH_SHORT).show();

                    hashtag.clear();
                    addCodyTag_et.setText(null);
                    addCodyKey_et.setText(null);
                    //프래그먼트 종료, 메인 화면으로 돌아감
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
                else if (addCodyKey_et.getText() == null) {
                    Toast.makeText(container.getContext(), "코디 이름을을 등록해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (hashtag.isEmpty()) {
                    Toast.makeText(container.getContext(), "태그를 작성해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(container.getContext(), "등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    //코디 추가 메소드
    public void addCody(String idToken, ArrayList<String> Comp, String Key, ArrayList<String> Tag) {
        Cody cody = new Cody(Key, Comp, Tag, false);

        //파이어베이스 리얼타임 데이터베이스에 의류 정보 저장
        userRef.child(idToken).child("Cody").child(Key).setValue(cody);
    }
}