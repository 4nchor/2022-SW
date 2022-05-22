package com.libienz.se_2022_closet.startApp_1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.databinding.ActivityFindEmailBinding;
import com.libienz.se_2022_closet.databinding.ActivityJoinBinding;

public class FindEmailActivity extends AppCompatActivity {

    private ActivityFindEmailBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("user");

    public void emailDuplicateCheck() {

        String inputEmail = binding.enterEmailToFindId.getText().toString();
        if (inputEmail.equals("")) {
            binding.notifyFindRes.setText("조회해볼 이메일 아이디를 입력해주세요!");
            binding.notifyFindRes.setTextColor(Color.parseColor("#B30000"));
            return;
        }
        Log.d("toFind",inputEmail);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot userSnaposhot : snapshot.getChildren()) {

                    String dbItrEmail = userSnaposhot.child("email").getValue().toString();

                    Log.d("toFind",dbItrEmail);
                    if(inputEmail.equals(dbItrEmail)) { //db에서 긁어온 메일과 입력 메일이 같을 때때
                        binding.notifyFindRes.setText("회원가입되어 있는 아이디입니다!");
                        binding.notifyFindRes.setTextColor(Color.parseColor("#008000"));
                        return;
                    }
                }
                binding.notifyFindRes.setText("회원가입되어 있지 않은 아이디입니다!");
                binding.notifyFindRes.setTextColor(Color.parseColor("#B30000"));
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_email);
        auth = FirebaseAuth.getInstance();


        binding.findIdBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                emailDuplicateCheck();
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