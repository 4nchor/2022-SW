package com.libienz.se_2022_closet.startApp_1.util;

import android.graphics.Color;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateUserUtility {

    private UpdateUserUtility(){}



    public static void passwordReset(EditText edt, TextView check_msg) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("user");

        String inputEmail = edt.getText().toString();
        if (inputEmail.equals("")) {
            check_msg.setText("이메일 아이디를 입력해주세요!");
            check_msg.setTextColor(Color.parseColor("#B30000"));
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
                        //String password = userSnaposhot.child("password").getValue().toString();
                        //String email = binding.enterEmailToFindPw.getText().toString();
                        auth.setLanguageCode("ko");
                        auth.sendPasswordResetEmail(inputEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    check_msg.setText("입력하신 이메일로 인증링크를 전송했습니다. 링크를 클릭하여 새로운 패스워드를 설정해주세요!");
                                    check_msg.setTextColor(Color.parseColor("#008000"));
                                    Log.d("sent", "Email sent.");
                                }
                                else {
                                    check_msg.setText("입력하신 이메일로 인증링크를 전송하는데에 실패했습니다.");
                                    check_msg.setTextColor(Color.parseColor("#B30000"));
                                }
                            }
                        });
                        return;
                    }
                }
                check_msg.setText("회원가입되어 있지 않은 아이디입니다!");
                check_msg.setTextColor(Color.parseColor("#B30000"));
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });


    }

}
