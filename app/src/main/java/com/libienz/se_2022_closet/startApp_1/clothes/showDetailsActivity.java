package com.libienz.se_2022_closet.startApp_1.clothes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.libienz.se_2022_closet.R;

public class showDetailsActivity extends AppCompatActivity {

    //옷&코디 상세 보기or수정 등등을 다 여기로 옮길까 생각중입니다..

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);


    }
}
