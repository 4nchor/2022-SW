package com.libienz.se_2022_closet.startApp_1.util;

import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libienz.se_2022_closet.R;

public class InputCheckUtility {

    private InputCheckUtility() {}

    public static boolean isEditTextEmpty(EditText edt) {
        if (edt.getText().toString().equals("")) {
            return true;
        }
        else {
            return false;
        }
    }



    public static void emailNotExistCheckAndNotify(EditText edt, TextView tv, String true_message, String false_message) {

        if(isEditTextEmpty(edt)) {
            tv.setText("이메일을 기입하세요");
            tv.setTextColor(Color.parseColor("#B30000"));
            return;
        }

        String inputEmail = edt.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("user");
        boolean flag;
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot userSnaposhot : snapshot.getChildren()) {
                    String dbItrEmail = userSnaposhot.child("email").getValue().toString();
                    Log.d("toFind",dbItrEmail);
                    if(inputEmail.equals(dbItrEmail)) { //db에서 긁어온 메일과 입력 메일이 같을 때때
                        tv.setTextColor(Color.parseColor("#B30000"));
                        tv.setText(false_message);
                        return;
                    }
                }

                tv.setText(true_message);
                tv.setTextColor(Color.parseColor("#008000"));
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }

        });
    }

    public static boolean nicknameNotEmptyCheckAndNotify(EditText edt, TextView tv, String true_message, String false_message) {
        if(isEditTextEmpty(edt)) {
            tv.setText(false_message);
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }
        else {
            tv.setText(true_message);
            tv.setTextColor(Color.parseColor("#008000"));
            return true;
        }
    }


    public static boolean passwordNotEmptyCheckAndNotify(EditText edt, TextView tv, String true_message, String false_message) {
        if(isEditTextEmpty(edt)) {
            tv.setText(false_message);
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }
        else {
            tv.setText(true_message);
            tv.setTextColor(Color.parseColor("#008000"));
            return true;
        }
    }


}
