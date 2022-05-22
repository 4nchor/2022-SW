package com.libienz.se_2022_closet.startApp_1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.libienz.se_2022_closet.R;

public class FindPwActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        //binding = DataBindingUtil.setContentView(this, R.layout.activity_join);
        //auth = FirebaseAuth.getInstance();



    }
}