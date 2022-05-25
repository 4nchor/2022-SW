package com.libienz.se_2022_closet.startApp_1.userauth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.ootd.OOTDActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

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
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), OOTDActivity.class);
                startActivity(intent);
            }
        });
    }
}