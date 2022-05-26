package com.libienz.se_2022_closet.startApp_1.userauth;

import static com.libienz.se_2022_closet.startApp_1.util.InputCheckUtility.emailNotExistCheckAndNotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.databinding.ActivityFindEmailBinding;
import com.libienz.se_2022_closet.startApp_1.util.InputCheckUtility;

public class FindEmailActivity extends AppCompatActivity {

    private ActivityFindEmailBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_email);
        auth = FirebaseAuth.getInstance();


        binding.findIdBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText edt = binding.enterEmailToFindId;
                TextView tv = binding.notifyFindRes;
                emailNotExistCheckAndNotify(edt, tv, "가입되지 않은 아이디 입니다.", "이미 가입되어 있는 아이디입니다");


            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActiyity.class);
                startActivity(intent);
            }
        });



    }
}