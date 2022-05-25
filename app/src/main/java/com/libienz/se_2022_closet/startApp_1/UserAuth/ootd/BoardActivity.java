package com.libienz.se_2022_closet.startApp_1.UserAuth.ootd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.libienz.se_2022_closet.R;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener {
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fabMain, fabSub1, fabSub2, fabSub3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent intent = getIntent();
        String takeDate = intent.getStringExtra("boardDate");
        TextView textView = findViewById(R.id.boardDate);
        textView.setText(takeDate);

        String takeKeyDate = intent.getStringExtra("dateKey");


        getBoardData(takeKeyDate);
        getImageData(takeKeyDate);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fabMain = (FloatingActionButton) findViewById(R.id.fabMain);
        fabSub1 = (FloatingActionButton) findViewById(R.id.fabSub1);
        fabSub2 = (FloatingActionButton) findViewById(R.id.fabSub2);
        fabSub3 = (FloatingActionButton) findViewById(R.id.fabSub3);


        fabMain.setOnClickListener(this);
        fabSub1.setOnClickListener(this);
        fabSub2.setOnClickListener(this);
        fabSub3.setOnClickListener(this);

    }

    private void getBoardData(String takeKeyDate){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TextView commentTextView = findViewById(R.id.getTextArea);
                Log.w("데이터받아오기",snapshot.toString());
                try {
                    int 데이터받아오기 = Log.w("데이터받아오기", snapshot.getValue().toString());
                } catch (Exception e) {
                    FBRef.boardRef.child(takeKeyDate).setValue(new CommentModel("uid",""));
                }
                CommentModel commentModel =snapshot.getValue(CommentModel.class);
                try {
                    commentTextView.setText(commentModel.comment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        };
        FBRef.boardRef.child(takeKeyDate).addValueEventListener(postListener);
    }
    private void getImageData(String takeKeyDate) {
        try {
            ImageView imageView = findViewById(R.id.getImageArea);
            FBRef.reference.child(takeKeyDate+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .into(imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "저장된 사진이 없네용", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void checkImageData(String takeKeyDate){
        try {
            ImageView imageView = findViewById(R.id.getImageArea);
            FBRef.reference.child(takeKeyDate+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Intent editIntent = new Intent(BoardActivity.this, BoardEditActivity.class);
                    editIntent.putExtra("keyDate",takeKeyDate);
                    startActivityForResult(editIntent,100);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "수정할 내용이 없네용", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = getIntent();
        String takeKeyDate = intent.getStringExtra("dateKey");
        switch (id) {
            case R.id.fabMain:
                anim();
                //Toast.makeText(this, "Floating Action Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fabSub1:
                anim();
                Toast.makeText(this, "Button1 삭제버튼", Toast.LENGTH_SHORT).show();
                onDeleteComment(takeKeyDate);
                onDeleteImage(takeKeyDate);
                break;
            case R.id.fabSub2:
                anim();
                Toast.makeText(this, "Button2 수정버튼", Toast.LENGTH_SHORT).show();
                checkImageData(takeKeyDate);
                break;
            case R.id.fabSub3:
                anim();
                Toast.makeText(this, "Button3 추가버튼", Toast.LENGTH_SHORT).show();
                Intent addIntent = new Intent(this, BoardAddActivity.class);
                addIntent.putExtra("keyDate",takeKeyDate);
                startActivityForResult(addIntent,0);
                break;
        }

    }

    private void onDeleteImage(String key)
    {
        FBRef.reference.child(key+".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(BoardActivity.this, key+"이미지삭제 성공", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BoardActivity.this,"삭제할이미지가 없나봐",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void onDeleteComment(String key) {
        FBRef.boardRef.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(BoardActivity.this, "코멘트삭제 성공", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BoardActivity.this,"삭제할 comment가 없나봐",Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0){
            if (resultCode==RESULT_OK) {
                finish();
            }else{
                Toast.makeText(this, "추가 중 나감!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode==100) {
            if (resultCode == RESULT_OK) {
                finish();
            } else {
                Toast.makeText(this, "수정 중 나감!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void anim() {

        if (isFabOpen) {
            fabMain.animate().rotation(0.0F).withLayer().setDuration(300L).setInterpolator(new OvershootInterpolator(10.0F)).start();
            fabMain.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF03DAC5")));

            fabSub1.startAnimation(fab_close);
            fabSub2.startAnimation(fab_close);
            fabSub3.startAnimation(fab_close);
            fabSub1.setClickable(false);
            fabSub2.setClickable(false);
            fabSub3.setClickable(false);
            isFabOpen = false;
        } else {
            fabMain.animate().rotation(135.0F).withLayer().setDuration(300L).setInterpolator(new OvershootInterpolator(10.0F)).start();
            fabMain.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFBB86FC")));

            fabSub1.startAnimation(fab_open);
            fabSub2.startAnimation(fab_open);
            fabSub3.startAnimation(fab_open);
            fabSub1.setClickable(true);
            fabSub2.setClickable(true);
            fabSub3.setClickable(true);
            isFabOpen = true;
        }
    }

}