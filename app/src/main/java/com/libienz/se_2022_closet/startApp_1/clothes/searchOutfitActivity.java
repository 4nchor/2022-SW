package com.libienz.se_2022_closet.startApp_1.clothes;

import static com.libienz.se_2022_closet.startApp_1.util.FirebaseReference.userRef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;
import com.libienz.se_2022_closet.startApp_1.userauth.LoginActiyity;
import com.libienz.se_2022_closet.startApp_1.userauth.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class searchOutfitActivity extends AppCompatActivity {

    private String searchKey; //사용자가 입력하는 검색어
    private ArrayList<Clothes> findRes = new ArrayList<Clothes>();
    private ArrayList<String> tag_want_to_find = new ArrayList<String>();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("user");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference().child("clothes").child(user.getUid());


    //유저가 입력한 태그들로 리스트를 만들었습니다 : want_to_find_list
    //목적 배열에 want_to_find_list에 있는 모든 원소들이 있는지 알려주는 메소드 입니다.
    public boolean checkAllTagisIn(ArrayList<String> want_to_find_list, ArrayList<String> obj) {
        for (String str : want_to_find_list) {
            if (!(obj.contains(str))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_outfit);

        TextView searchOutfit_tv = (TextView) findViewById(R.id.searchOutfit_tv);
        EditText searchOutfit_et = (EditText) findViewById(R.id.searchOutfit_et);
        Button search_btn = (Button) findViewById(R.id.search_btn);
        RadioGroup searchWhat_rg = (RadioGroup) findViewById(R.id.searchWhat_rg);
        RadioButton searchClothes_rb = (RadioButton) findViewById(R.id.searchClothes_rb);
        RadioButton searchCody_rb = (RadioButton) findViewById(R.id.searchCody_rb);
        Switch searchCodyforKeyword_swt = (Switch) findViewById(R.id.searchCody_swt);

        //검색 결과 보여 주는 view 설정
        RecyclerView searchResult_rv = findViewById(R.id.searchResult_rv);
        searchResult_rv.setLayoutManager(new LinearLayoutManager(this));

        searchClothes_rb.setChecked(true);

        //의류랑 코디 세트 중 무얼 검색할 건지 체크
        searchWhat_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (searchCody_rb.isChecked()) {
                    searchCodyforKeyword_swt.setVisibility(View.VISIBLE);
                    searchOutfit_tv.setText(null);
                    tag_want_to_find.clear();
                    findRes.clear();
                }
                else {
                    searchCodyforKeyword_swt.setChecked(false);
                    searchOutfit_tv.setText(null);
                    searchCodyforKeyword_swt.setVisibility(View.GONE);
                    tag_want_to_find.clear();
                    findRes.clear();
                }
            }
        });

        //코디 세트를 검색한다면 태그랑 키워드 중 어떤 걸로 검색할 건지 체크
        searchCodyforKeyword_swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (searchCodyforKeyword_swt.isChecked()) {
                    searchOutfit_et.setHint("키워드로 검색");
                    searchOutfit_tv.setText(null);
                    tag_want_to_find.clear();
                    findRes.clear();
                }
                else {
                    searchOutfit_et.setHint("태그로 검색");
                    searchOutfit_tv.setText(null);
                    tag_want_to_find.clear();
                    findRes.clear();
                }
            }
        });

        //태그로 검색하는 경우 태그를 하나씩 입력할 때마다 검색 결과를 갱신하기 위해 아래와 같이 작성함...
        //따로 검색 버튼 없이 검색어 입력 후 엔터를 누르면 검색되는 방식
        searchOutfit_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchKey = searchOutfit_et.getText().toString();
                    tag_want_to_find.add(searchKey);
                    if (!searchCodyforKeyword_swt.isChecked()){
                        searchOutfit_tv.append("  #" + searchKey);
                        searchOutfit_et.setText(null);
                    }
                    search_btn.performClick();
                    findRes.clear();
                    return true;
                }
                return false;
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //태그로 의류를 검색하는 경우
                if (searchClothes_rb.isChecked()) {
                    searchOutfit_et.getText().toString();
                    userRef.child(user.getUid()).child("Clothes").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //반복문을 통해서 DB에서 해당 유저의 모든 옷들을 순회합니다.
                            //dbItrCloth가 매 루프마다 긁어오는 옷을 담는 Clothes 타입 변수 입니다.
                            //db에서 가져온 옷들마다 내가 찾고 싶은 태그가 모두 들어있는지 확인하고 그렇다면 Result 배열에 추가합니다.
                            //----------- 다른 해결해야 하는 문제점 ------------
                            //태그가 저장될 때 태그들 사이에 공백문자가 계속해서 저장됩니다. (배열의 한칸을 먹고 있어요 저번 부터 발견했는데 다만 따로 생각하신 바가 있을까봐 그냥 둡니다) <- 제 환경에서는 전혀 이런 오류가 없어서 왜 이런지 모르겠습니다 ㅠㅠ 따라서 보류
                            //검색하는 동안 언제까지 입력한 태그를 저장할지 언제 배열을 dump할지 정해주셔야 할 것 같습니다. 사용자가 새로운 검색을 하고 싶을 때를 위해서요 <- 이거 해결! 검색 모드 바꾸면 지금까지 입력돼 있던 태그 리셋되도록 했습니다
                            //코디에도 완전 똑같은 코드 쓰시면 됩니다. 데베 긁어오는 부분 한부분만 고치면 되겠네요 <- 감사합니다
                            for (DataSnapshot cloth_snapshot : snapshot.getChildren()) {
                                Clothes dbItrCloth = cloth_snapshot.getValue(Clothes.class);
                                if (checkAllTagisIn(tag_want_to_find, dbItrCloth.getClothesTag())) {
                                    findRes.add(dbItrCloth);
                                }
                            }
                            Log.d("wantfind", tag_want_to_find.toString());
                            for (Clothes clth : findRes) {
                                Log.d("seq", clth.getClothesTag().toString());
                            }

                            //검색 결과 출력
                            ClothesAdapter adapter = new ClothesAdapter(findRes);
                            searchResult_rv.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }

                else if (searchCody_rb.isChecked() && !searchCodyforKeyword_swt.isChecked()); //태그로 코디 검색하는 경우
                else if (searchCody_rb.isChecked() && searchCodyforKeyword_swt.isChecked()); //키워드로 코디 검색하는 경우
            }
        });

        //검색 결과 클릭했을 때 열람 페이지를 띄움
        ClothesAdapter adapter = new ClothesAdapter(findRes);
        adapter.setOnItemClickListener(new ClothesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                //ClothesKey를 열람 프래그먼트에 번들로 전달
                String ClothesKey = findRes.get(pos).getClothesKey();
                Bundle bundle = new Bundle();
                bundle.putString("ClothesKey", ClothesKey);

                readClothesFrag fragment = new readClothesFrag();
                fragment.setArguments(bundle);

                //열람 프래그먼트 띄움, 레이아웃 완성 안 됨 (프레임이 안 들어감!!!!!!!!!!!!!!!!!!!!)
                //getSupportFragmentManager().beginTransaction().replace("프래그먼트 들어갈 레이아웃 이름", fragment).commit();
            }
        });
    }

}