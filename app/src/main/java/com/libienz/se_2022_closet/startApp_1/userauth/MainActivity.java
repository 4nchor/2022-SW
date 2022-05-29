package com.libienz.se_2022_closet.startApp_1.userauth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.fragments.addClothesFrag;
import com.libienz.se_2022_closet.startApp_1.fragments.readClothesFrag;
import com.libienz.se_2022_closet.startApp_1.ootd.OOTDActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private addClothesFrag addClothesFrag;
    private readClothesFrag readClothesFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        fragmentManager = getSupportFragmentManager();

        addClothesFrag = new addClothesFrag();
        readClothesFrag = new readClothesFrag();

        Button logout_btn = (Button) findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
                startActivity(intent);
            }
        });

        Button ootd_btn = (Button) findViewById(R.id.ootd_btn);
        ootd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OOTDActivity.class);
                startActivity(intent);
            }
        });

        Button addClothes_btn = (Button) findViewById(R.id.addClothes_btn);
        addClothes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.addClothes_fg, addClothesFrag).commit();
            }
        });

        Button readClothes_btn = (Button) findViewById(R.id.readClothes_btn);
        readClothes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.readClothes_fg, readClothesFrag).commit();
            }
        });

    }
}