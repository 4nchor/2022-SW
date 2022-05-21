package com.libienz.se_2022_closet.startApp_1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.databinding.ActivityJoinBinding;
import com.libienz.se_2022_closet.startApp_1.data.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {

    private ActivityJoinBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("user");
    private UserInfo user;
    private boolean password_check_flag = false; //비밀번호와 비밀번호 확인란의 두 패스워드가 같은지를 나타내는 flag
    private boolean password_validation_flag = false; //비밀번호가 유효한지를 보여주는 flag
    private boolean email_checked_flag = false; //이메일 중복을 확인했는지 나타내는 flag
    private boolean nickname_filled_flag = false;
    private boolean gender_filled_flag = false;





    //EditText가 비어있는지 확인
    public boolean isEditTextEmpty(EditText edt) {

        if (edt.getText().toString().equals("")) {return true;}
        else {return false;}
    }

    //사용자가 닉네임을 기입했는지 확인
    public void nicknameEmptyCheck() {
        String nickname;
        EditText edt = binding.joinName;
        if (isEditTextEmpty(edt)) {
            nickname_filled_flag = false;
            binding.nameEmptyCheck.setText("닉네임을 기입하세요.");
        }

        else {
            nickname_filled_flag = true;
            binding.nameEmptyCheck.setText("");
        }
    }

    //사용자가 성별을 기입했는지 확인
    public void genderEmptyCheck() {

        RadioGroup radioGroup = binding.genderAll;
        int genderId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(genderId);
        if (radioButton == null) {
            binding.genderEmptyCheck.setText("성별을 기입하세요.");
            gender_filled_flag = false;
            return;
        }
        gender_filled_flag = true;
        binding.genderEmptyCheck.setText("");

    }

    //비밀번호와 비밀번호 확인이 같은 지 확인하는 메소드 같지 않다면 같지 않다고 텍스트를 보여준다.
    public void samePasswordCheck() {
        String pw = binding.joinPassword.getText().toString(); //비밀번호란에 입력된 문자열
        String pwck = binding.joinPwck.getText().toString(); //비밀번호 확인란에 입력된 문자열
        TextView isSame = binding.samePasswordCheck;

        if (pw.equals(pwck)) { //비밀번호와 비밀번호 확인이 같은 경우
            isSame.setText("");
            password_check_flag = true;
        }
        else {
            password_check_flag = false;
            isSame.setText("비밀번호가 일치하지 않습니다.");
        }
    }

    //비밀번호의 유효성 검사하는 메소드
    //비밀번호는 8~16자리의 영문, 숫자, 특수문자의 조합으로 이루어지고 공백문자나 아이디를 포함하였는 지 등도 검사한다.
    public void passwordValidCheck() {

        String userId = binding.joinEmail.getText().toString();
        EditText edt = binding.joinPassword;
        String password = edt.getText().toString(); //비밀번호란에 입력된 문자열
        TextView pw_valid = binding.pwValidCheck;

        String pwPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";
        Matcher matcher = Pattern.compile(pwPattern).matcher(password);

        pwPattern = "(.)\\1\\1\\1";
        Matcher matcher2 = Pattern.compile(pwPattern).matcher(password);

        if(isEditTextEmpty(edt)) { //비밀번호란이 비어있는 경우
            pw_valid.setText("비밀번호를 기입하세요.");
            password_validation_flag = false;
            return;
        }

        if(password.contains(userId)){ //비밀번호에 아이디가 포함된 경우
            pw_valid.setText("비밀번호에 아이디가 포함될 수 없습니다.");
            password_validation_flag = false;
            return;
        }

        if(password.contains(" ")){ //비밀번호가 공백문자를 포함한 경우
            pw_valid.setText("비밀번호는 공백문자를 포함할 수 없습니다.");
            password_validation_flag = false;
            return;
        }

        if(matcher2.find()){ //비밀번호에 같은 문자가 4개이상 나오는 경우
            pw_valid.setText("비밀번호는 같은 문자를 4개 이상 사용할 수 없습니다.");
            password_validation_flag = false;
            return;
        }

        if(!matcher.matches()){ //비밀번호가 정규식을 만족하지 않는 경우
            pw_valid.setText("비밀번호는 8~16자리의 영문과 숫자와 특수문자의 조합이어야 합니다.");
            password_validation_flag = false;
            return;
        }



        pw_valid.setText("");
        password_validation_flag = true;

    }

    //이메일 입력란이 비어있는지 확인
    public void emailEmptyCheck() {

        EditText edt = binding.joinEmail;
        if (isEditTextEmpty(edt)) {
            binding.emailCheck.setText("이메일을 기입하세요");
            return;
        }
        binding.emailCheck.setText("");
    }

    public void emailDuplicateCheck() {
        String inputEmail = binding.joinEmail.getText().toString();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot userSnaposhot : snapshot.getChildren()) {
                    String dbItrEmail = userSnaposhot.child("email").getValue().toString();

                    if(inputEmail.equals(dbItrEmail)) { //db에서 긁어온 메일과 입력 메일이 같을 때때
                        binding.emailCheck.setText("이미 존재하는 이메일 입니다.");
                       email_checked_flag = false;
                        return;
                    }
                    binding.emailCheck.setText("");
                    email_checked_flag = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

    }

    /* 이메일과 비밀번호를 입력인자로 계정 생성해주는 메소드
     * Firebase 기반
     * 성공하면 로그인 까지 완료시키고 메인화면으로
     * 실패하면 토스트메세지 출력 */
    public void createUserWithEmailAndPassword(String email, String password) {
        emailEmptyCheck();
        emailDuplicateCheck();
        samePasswordCheck(); //비밀번호와 비밀번호 확인이 같다면 flag값이 바뀐다.
        passwordValidCheck(); //비밀번호가 조건을 담은 정규식을 만족한다면 flag값이 바뀐다.
        nicknameEmptyCheck();
        genderEmptyCheck();


        if (  !(password_check_flag &&
                password_validation_flag &&
                email_checked_flag &&
                nickname_filled_flag &&
                gender_filled_flag)) {return;} //flag값이 모두 1이 아니라면 메소드 종료 이벤트 다시기다림

        //flag값이 모두 만족하면 유저 생성
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //가입 성공시점에 유저정보를 인스턴스화한다!
                            String nickname = binding.joinName.getText().toString();
                            RadioGroup radioGroup = binding.genderAll;
                            int genderId = radioGroup.getCheckedRadioButtonId();
                            RadioButton radioButton = findViewById(genderId);
                            user = new UserInfo(email,password,nickname,radioButton.getText().toString());
                            userRef.child(auth.getCurrentUser().getUid().toString()).child("email").setValue(user.getEmail());
                            userRef.child(auth.getCurrentUser().getUid().toString()).child("password").setValue(user.getPassword());
                            userRef.child(auth.getCurrentUser().getUid().toString()).child("nickname").setValue(user.getNickname());
                            userRef.child(auth.getCurrentUser().getUid().toString()).child("gender").setValue(user.getGender());




                            // Sign in success, update UI with the signed-in user's information
                            Log.d("join", "createUserWithEmail:success");
                            Toast.makeText(JoinActivity.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("join", "createUserWithEmail:failure");
                            Toast.makeText(JoinActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join);
        auth = FirebaseAuth.getInstance();







        // 회원가입 버튼을 눌렀을 때 입력된 사용자 정보를 바탕으로 회원가입 한다.
        binding.joinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String email = binding.joinEmail.getText().toString();
                String password = binding.joinPassword.getText().toString();
                String pwck = binding.joinPwck.getText().toString();
                String name = binding.joinName.getText().toString();

                createUserWithEmailAndPassword(email,password);




            }
        });


        //취소 버튼 클릭하면 로그인 화면으로 이동!
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),LoginActiyity.class);
                startActivity(intent);
            }
        });
    }
}
