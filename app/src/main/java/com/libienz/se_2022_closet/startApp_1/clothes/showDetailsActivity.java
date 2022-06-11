package com.libienz.se_2022_closet.startApp_1.clothes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.cody.addCodyFrag;
import com.libienz.se_2022_closet.startApp_1.cody.readCodyFrag;

public class showDetailsActivity extends AppCompatActivity {

    //옷&코디 상세 보기or수정 등등을 다 여기로 옮길까 생각중입니다..

    private FirebaseAuth auth;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private addClothesFrag addClothesFrag;
    private ReadAllClothesFrag readAllClothesFrag;
    private com.libienz.se_2022_closet.startApp_1.cody.addCodyFrag addCodyFrag;
    private com.libienz.se_2022_closet.startApp_1.cody.readCodyFrag readCodyFrag;
    private boolean isFrag = false; //프래그먼트 백스택에 남은 것이 있는지 여부를 나타내는 변수

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.show_details_fg, readAllClothesFrag).commit();




    }
}
