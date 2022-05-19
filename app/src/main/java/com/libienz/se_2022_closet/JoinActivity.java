package com.libienz.se_2022_closet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.libienz.se_2022_closet.databinding.ActivityJoinBinding;

public class JoinActivity extends AppCompatActivity {

    private ActivityJoinBinding binding;
    private FirebaseAuth auth;

    public void createUserWithEmailAndPassword(String email, String password) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_join);

        auth = FirebaseAuth.getInstance();
/*      Button joinBtn = (Button)findViewById(R.id.join_button);
        EditText name_area = (EditText)findViewById(R.id.join_name);
        EditText email_area = (EditText)findViewById(R.id.join_email);
        EditText password_area = (EditText) findViewById(R.id.join_password);
        EditText pwck_area = (EditText)findViewById(R.id.join_pwck);

        String name = name_area.getText().toString();
        String email = email_area.getText().toString();
        String password = password_area.getText().toString();
        String pwck =  pwck_area.getText().toString();*/

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
    }
}
