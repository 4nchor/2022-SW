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
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.databinding.ActivityJoinBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {

    private ActivityJoinBinding binding;
    private FirebaseAuth auth;
    private boolean password_check_flag = false; //비밀번호와 비밀번호 확인란의 두 패스워드가 같은지를 나타내는 flag
    private boolean password_validation_flag = false; //비밀번호가 유효한지를 보여주는 flag
    private boolean email_checked_flag = false; //이메일 중복을 확인했는지 나타내는 flag



    /* 이메일과 비밀번호를 입력인자로 계정 생성해주는 메소드
     * Firebase 기반
     * 성공하면 로그인 까지 완료시키고 메인화면으로
     * 실패하면 토스트메세지 출력 */
    public void createUserWithEmailAndPassword(String email, String password) {
        samePasswordCheck();
        passwordValidCheck();
        emailCheck();
        if (!(password_check_flag && password_validation_flag && email_checked_flag)) {return;}
        //if (password_validation_flag != 1) {return;}
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
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

    public void passwordValidCheck() {
        String userId = binding.joinEmail.getText().toString();
        String password = binding.joinPassword.getText().toString(); //비밀번호란에 입력된 문자열
        TextView pw_valid = binding.pwValidCheck;

        String pwPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";
        Matcher matcher = Pattern.compile(pwPattern).matcher(password);

        pwPattern = "(.)\\1\\1\\1";
        Matcher matcher2 = Pattern.compile(pwPattern).matcher(password);

        if(!matcher.matches()){
            pw_valid.setText("비밀번호는 8~16자리의 영문과 숫자와 특수문자의 조합이어야 합니다.");
            return;
        }

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
