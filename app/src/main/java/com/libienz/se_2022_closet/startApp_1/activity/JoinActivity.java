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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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



    /* 이메일과 비밀번호를 입력인자로 계정 생성해주는 메소드
     * Firebase 기반
     * 성공하면 로그인 까지 완료시키고 메인화면으로
     * 실패하면 토스트메세지 출력 */
    public void createUserWithEmailAndPassword(String email, String password) {
        samePasswordCheck(); //비밀번호와 비밀번호 확인이 같다면 flag값이 바뀐다.
        passwordValidCheck(); //비밀번호가 조건을 담은 정규식을 만족한다면 flag값이 바뀐다.
        emailCheck(); //이메일 중복체크를 진행해서 사용가능한 아이디임을 확인했다면 flag값이 바뀐다.

        if (!(password_check_flag && password_validation_flag && email_checked_flag)) {return;} //flag값이 모두 1이 아니라면 메소드 종료 다시 적절한 조건에서 이벤트가 일어나길 기다리게 됨

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
                            userRef.push().setValue(user.toString());



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
            isSame.setText("비밀번호가 일치하지 않습니다.");
        }
    }

    //비밀번호의 유효성 검사하는 메소드
    //비밀번호는 8~16자리의 영문, 숫자, 특수문자의 조합으로 이루어지고 공백문자나 아이디포함여부, 같은 문자를 4번이상
    //사용하지 않았는지에 대한 조건을 검사한다.
    public void passwordValidCheck() {
        String userId = binding.joinEmail.getText().toString();
        String password = binding.joinPassword.getText().toString(); //비밀번호란에 입력된 문자열
        TextView pw_valid = binding.pwValidCheck;

        String pwPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";
        Matcher matcher = Pattern.compile(pwPattern).matcher(password);

        pwPattern = "(.)\\1\\1\\1";
        Matcher matcher2 = Pattern.compile(pwPattern).matcher(password);

        if(password.contains(userId)){
            pw_valid.setText("비밀번호에 아이디가 포함될 수 없습니다.");
            return;
        }

        if(password.contains(" ")){
            pw_valid.setText("비밀번호는 공백문자를 포함할 수 없습니다.");
            return;
        }

        if(matcher2.find()){
            pw_valid.setText("비밀번호는 같은 문자를 4개 이상 사용할 수 없습니다.");
            return;
        }

        if(!matcher.matches()){
            pw_valid.setText("비밀번호는 8~16자리의 영문과 숫자와 특수문자의 조합이어야 합니다.");
            return;
        }



        pw_valid.setText("");
        password_validation_flag = true;

    }

    public void emailCheck() {
        email_checked_flag=true;
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
