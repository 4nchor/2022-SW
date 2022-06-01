package com.libienz.se_2022_closet.startApp_1.clothes;

import static android.app.Activity.RESULT_OK;
import static com.libienz.se_2022_closet.startApp_1.util.FirebaseReference.userRef;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;

import java.util.ArrayList;

public class addClothesFrag extends Fragment {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Uri imguri;
    private ArrayList<String> hashtag = new ArrayList<>(10);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_clothes, container, false);

        //갤러리에서 사진을 선택하면 해당 사진을 앱으로 불러와 이미지뷰에 띄움
        ImageView imageView = view.findViewById(R.id.addclothes_iv);
        ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null){
                            imguri = result.getData().getData();
                            imageView.setImageURI(imguri);
                        }
                    }
                });

        //이미지 업로드 버튼을 클릭하면 갤러리에 접근하는 메소드를 호출하도록 함, 성민님 코드 참고했음 (BoardAddActivity.java 57:60)
        Button addClothesImg_btn = (Button) view.findViewById(R.id.addClothesImg_btn);
        addClothesImg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/");
                activityResult.launch(galleryIntent);
            }
        });

        //태그 정보를 입력받음
        EditText addTag_et = view.findViewById(R.id.addTag_et);
        TextView showAddedTag_tv = view.findViewById(R.id.showAddedTag_tv);
        addTag_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    hashtag.add(addTag_et.getText().toString());
                    showAddedTag_tv.append("#" + hashtag.get(hashtag.size() - 1) + " ");
                    addTag_et.setText(null);
                    return true;
                }
                return false;
            }
        });

        //의류 정보를 입력받음
        EditText addInfo_et = view.findViewById(R.id.addInfo_et);

        //등록하기 버튼을 눌러 의류를 등록함
        Button add_btn = (Button) view.findViewById(R.id.doneAddClothes_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //유저 정보 및 이미지 삽입 확인 후 의류 등록
                if (user != null && imguri != null){
                    //addClothes(user.getUid(), imguri.toString(), tag.getText().toString(), info.getText().toString(), container);
                    addClothes(user.getUid(), imguri.toString(), hashtag, addInfo_et.getText().toString(), container);
                    Toast successmsg = Toast.makeText(container.getContext(), "의류 정보가 등록되었습니다", Toast.LENGTH_SHORT);
                    successmsg.show();
                }
                else {
                    Toast.makeText(container.getContext(), "Failed to Add", Toast.LENGTH_SHORT).show();
                }

                hashtag.clear();
                addInfo_et.setText(null);
                //프래그먼트 종료, 추가하기 전 화면으로 돌아감
                getParentFragmentManager().beginTransaction().remove(addClothesFrag.this).commit();

            }
        });

        return view;
    }

    //파이어베이스 이미지 업로드 메소드
    private void uploadToFirebase(Uri uri, String idToken, ViewGroup container){
        StorageReference imgref = storage.getReference().child("clothes").child(idToken).child(uri.toString().substring(uri.toString().lastIndexOf("/") + 1) + ".png");
        UploadTask uploadTask = imgref.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(container.getContext(), "Failed to Add Img", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //의류 추가 메소드
    public void addClothes(String idToken, String Img, ArrayList<String> Tag, String Info, ViewGroup container) {
        Clothes clothes = new Clothes(Img, Tag, Info);

        //파이어베이스 리얼타임 데이터베이스에 의류 정보 저장
        userRef.child(idToken).child("Clothes").child(Img.substring(Img.lastIndexOf("/") + 1)).setValue(clothes);

        //파이어베이스 스토리지에 의류 사진 저장
        uploadToFirebase(imguri, idToken, container);
    }
}