package com.libienz.se_2022_closet.startApp_1.userauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.databinding.ActivityLoginBinding;

public class LoginActiyity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;


    /*
    * 이메일과 비밀번호 기반 로그인 수행하는 메소드
    * Firebase 기반 동작
    * 성공하면 메인액티비티로 이동 실패하면 토스트 메세지 띄움 */
    public void signInWithEmailAndPassword(String email, String password) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("logincheck",email.toString() + password.toString());
                            Log.d("login", "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //로그인 성공 메인액티비티로 이동!
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("logincheck",email.toString() + password.toString());
                            Log.w("login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActiyity.this, "로그인 실패",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        binding.login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = binding.enterID.getText().toString();
                String password = binding.enterPW.getText().toString();
                Log.d("logincheck",email.toString() + password.toString());

                //빈칸으로 로그인하려고 하면 입력하라고 토스트 메세지 출력
                if (email.equals("") || password.equals("")) {
                    Toast.makeText(LoginActiyity.this, "이메일, 비밀번호를 입력하세요!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                signInWithEmailAndPassword(email,password);
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),JoinActivity.class);
                startActivity(intent);
            }
        });

        binding.lostId.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FindEmailActivity.class);
                startActivity(intent);
            }
        });
        binding.lostPw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FindPwActivity.class);
                startActivity(intent);
            }
        });
    }
}