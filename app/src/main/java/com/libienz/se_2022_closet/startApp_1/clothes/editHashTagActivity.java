package com.libienz.se_2022_closet.startApp_1.clothes;

import static com.libienz.se_2022_closet.startApp_1.util.FirebaseReference.userRef;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.circularreveal.CircularRevealHelper;
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
import com.libienz.se_2022_closet.startApp_1.util.FirebaseReference;

import java.util.ArrayList;
    /* 태그 수정하기
  기존의 태그를 어레이리스트로 불러오기 (readClothes에 있음)
  레이아웃에 출력
  바꿀 태그를 선택하세요 (번호를 저장해놓으면 좋을 것 같음)
  어레이 리스트의 번호로 접근해서 값을 바꿔 new_tag를 만들음
  DB에 다시 넣음
   */

public class editHashTagActivity extends AppCompatActivity {
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("user");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference().child("clothes").child(user.getUid());
    
    //editClothesFrag에서 해시태그 이름을 변경할 의류 키 값을 받아옴
    private String ClothesKey = "1364804085";
    private ArrayList<String> prev_tag; //기존 태그 리스트
    private ArrayList<String> new_tag; //새로운 태그를 받을 리스트
    private ArrayList<String> tag;
    private int where_tag; //바꿀 태그의 배열 번호
    private String name_tag; //새로운 태그의 이름
    protected ListView show_tag; //태그를 출력해줄 리스트뷰
    protected Button editHashTag_btn;
    protected EditText editHashTag_et;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hashtag);

        //의류 정보를 불러옵니다
        userRef.child(user.getUid()).child("Clothes").child(ClothesKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Clothes clothes = snapshot.getValue(Clothes.class);
                Log.d("cloth",clothes.toString());
                prev_tag = clothes.getClothesTag();
                new_tag = prev_tag;
                
                editHashTag_btn = (Button) findViewById(R.id.editHashTag_btn2);
                editHashTag_et = (EditText) findViewById(R.id.editHashTag_et);
                show_tag = (ListView) findViewById(R.id.show_tag);

                //어뎁터 설정
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_edit_hashtag, prev_tag);
                show_tag.setAdapter(adapter);
                //listview(tag)를 클릭할 경우 다음 동작을 실행
                show_tag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        where_tag = position;
                        //바꿀 tag 의 위치를 where_tag 에 넣어줍니다
                        editHashTag_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            //확인 버튼을 눌렀을 때, 새로운 tag의 이름(name_tag)을 where_tag의 위치로 new_tag에 넣어줍니다.
                            public void onClick(View v) {
                                name_tag = editHashTag_et.getText().toString();
                                new_tag.set(where_tag, name_tag);
                                //확인 메세지
                                Toast.makeText(this, "이 으로 변경되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //DB에 저장
        tag = new_tag;
        userRef.child(user.getUid()).child("Clothes").child(ClothesKey).setValue(tag);
    }
}
