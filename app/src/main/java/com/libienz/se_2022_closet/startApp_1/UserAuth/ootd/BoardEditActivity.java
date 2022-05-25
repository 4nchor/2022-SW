package com.libienz.se_2022_closet.startApp_1.UserAuth.ootd;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.libienz.se_2022_closet.R;

public class BoardEditActivity extends AppCompatActivity {
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_edit);

        imageView = findViewById(R.id.imageArea);
        progressBar = findViewById(R.id.progress_View);
        Button uploadBtn = findViewById(R.id.writeBtn);

        Intent intent = getIntent();
        String takeKeyDate = intent.getStringExtra("keyDate");
        Log.d("키값제대로 받아왔니?",takeKeyDate);

        //프로그래스바 숨기기
        progressBar.setVisibility(View.INVISIBLE);

        getImageData(takeKeyDate);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/");
                activityResult.launch(galleryIntent);
            }
        });
        //업로드버튼 클릭 이벤트
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //선택한 이미지가 있다면
                if (imageUri != null) {
                    EditText editText = findViewById(R.id.commentArea);
                    String  comment =editText.getText().toString();

                    FBRef.boardRef.child(takeKeyDate).setValue(new CommentModel("uid",comment)); //uid는 추후 합병할때 추가

                    uploadToFirebase(imageUri,takeKeyDate);

                } else {
                    Toast.makeText(BoardEditActivity.this, "사진을 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    } //onCreate
    private void getImageData(String takeKeyDate){
        FBRef.reference.child(takeKeyDate+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(imageView);
            }
        });
    }

    //사진 가져오기
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if( result.getResultCode() == RESULT_OK && result.getData() != null){

                        imageUri = result.getData().getData();
                        imageView.setImageURI(imageUri);
                    }
                }
            });

    //파이어베이스 이미지 업로드
    private void uploadToFirebase(Uri uri, String key) {

        StorageReference fileRef = FBRef.reference.child(key+".png");
        Log.w("사진저장", String.valueOf(fileRef));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //프로그래스바 숨김
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(BoardEditActivity.this, key+"수정 성공", Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                //프로그래스바 보여주기
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //프로그래스바 숨김
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(BoardEditActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

}