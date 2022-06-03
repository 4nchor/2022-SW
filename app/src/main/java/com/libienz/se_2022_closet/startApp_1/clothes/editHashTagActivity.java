package com.libienz.se_2022_closet.startApp_1.clothes;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libienz.se_2022_closet.R;

public class editHashTagActivity extends AppCompatActivity {
    SharedPreferences pre;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("user");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference().child("clothes").child(user.getUid());
    private String prev_tag;
    private String next_tag;
    private String ClothesKey;

    protected Button editHashTag_btn;
    protected TextView editHashTag_et;
    protected Button editHashTag_btn2;
    protected TextView editHashTag_et2;

    //editClothesFrag에서 바꿀 해시태그 값을 받아오기
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hashtag);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

    }
    
    //본 레이아웃(edit_hashtag)에서 새로운 태그 값을 받아오기
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hashtag);

        editHashTag_et2 = (EditText) findViewById(R.id.editHashTag_et2);
        editHashTag_btn2 = (Button) findViewById(R.id.editHashTag_btn2);
        editHashTag_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_tag = editHashTag_et2.getText().toString();
            }
        });

    }

    @Override
    public void onClick(View v) {


    }
    
    //파이어베이스에 저장

}
