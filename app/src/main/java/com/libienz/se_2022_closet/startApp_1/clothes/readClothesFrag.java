package com.libienz.se_2022_closet.startApp_1.clothes;

import static com.libienz.se_2022_closet.startApp_1.util.FirebaseReference.userRef;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;
import com.libienz.se_2022_closet.startApp_1.userauth.MainActivity;

import java.util.ArrayList;

public class readClothesFrag extends Fragment {

    private View header;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference().child("clothes").child(user.getUid());


    //TODO : searchOutfitActivity에서 검색 결과를 클릭하면 readClothesFrag로 넘어오는 리스너가 먹통이라 지금은 ClothesKey가 정적으로 초기화되어 있습니다. searchOutfitActivity를 해결하면 아래 ClotheyKey = "178809003"으로 초기화돼 있는 부분을 삭제합니다.
    private String ClothesKey; //= "178809003"
    private ArrayList<String> tag;
    private String info;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_read_clothes, container, false);

        //다른 프래그먼트나 액티비티에서 넘겨 준 ClothesKey 받아옴
        if (getArguments() != null) {
            ClothesKey = getArguments().getString("ClothesKey");
            Log.d("clotheskey", "clotheskey: "+ClothesKey);
        }

        //의류 정보를 띄우는 코드
        userRef.child(user.getUid()).child("Clothes").child(ClothesKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Clothes clothes = snapshot.getValue(Clothes.class);

                Log.d("cloth",clothes.toString());
                tag = clothes.getClothesTag();
                info = clothes.getClothesInfo();

                ImageView readimg_iv = (ImageView) view.findViewById(R.id.readClothes_iv);
                TextView readTag_tv = (TextView) view.findViewById(R.id.readTag_tv);
                TextView readInfo_tv = (TextView) view.findViewById(R.id.readInfo_tv);

                //태그 정보 출력
                for(int i = 0; i < tag.size(); i++)
                    readTag_tv.append("#" + tag.get(i) + " ");

                //의류 정보 출력
                readInfo_tv.setText("  " + info);

                //이미지 정보 출력
                //저장된 사진이 없을 경우 실패 메시지 출력, 있을 경우 사진 출력
                if (storageReference == null) {
                    Toast.makeText(container.getContext(), "사진 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    StorageReference submitReference = storageReference.child(ClothesKey + ".png");
                    submitReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (getActivity() == null) return;
                            Glide.with(readClothesFrag.this).load(uri).into(readimg_iv);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(container.getContext(), "의류 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //즐겨찾기 버튼을 클릭했을 때
        Button favorite_btn = (Button) view.findViewById(R.id.favorite_btn);
        favorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //즐겨찾기에 추가
                userRef.child(user.getUid()).child("Clothes").child(ClothesKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override

                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Clothes clothes = snapshot.getValue(Clothes.class);

                        if (!clothes.getIsFavoriteClothes()){ //즐겨찾기 추가
                            userRef.child(user.getUid()).child("Clothes").child(ClothesKey).child("isFavoriteClothes").setValue(true);
                            Log.d("addFavorite", "isFavorite :"+clothes.getIsFavoriteClothes());
                            favorite_btn.setText("즐겨찾기 해제");
                            Toast.makeText(container.getContext(), "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show();
                        }
                        else{ //즐겨찾기 해제
                            //clothes.setIsFavoriteClothes(false);
                            userRef.child(user.getUid()).child("Clothes").child(ClothesKey).child("isFavoriteClothes").setValue(false);
                            Log.d("removeFavorite", "isFavorite :"+clothes.getIsFavoriteClothes());
                            favorite_btn.setText("즐겨찾기");
                            Toast.makeText(container.getContext(), "즐겨찾기 해제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


        //의류 수정 버튼을 클릭했을 때
        Button editClothes_btn = (Button) view.findViewById(R.id.editClothes_btn);
        editClothes_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //열람 중이었던 의류의 키값을 수정 프래그먼트에 넘김
                Bundle bundle = new Bundle();
                bundle.putString("ClothesKey", ClothesKey);

                editClothesFrag editClothesFrag = new editClothesFrag();
                editClothesFrag.setArguments(bundle);

                //열람 중이었던 의류를 수정하도록 함
                getParentFragmentManager().beginTransaction().replace(R.id.frag_fl, editClothesFrag).addToBackStack(null).commit();
            }
        });

        //의류 삭제 버튼을 클릭했을 때
        Button deleteClothes_btn = (Button) view.findViewById(R.id.deleteClothes_btn);
        deleteClothes_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                deleteClothes(readClothesFrag.this);
            }
        });

        //확인 버튼을 클릭했을 때 열람 이전에 보고 있던 화면으로 돌어감
        Button readtomain_btn = (Button) view.findViewById(R.id.readtomain_btn);
        readtomain_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ReadAllClothesFrag readAllClothesFrag = new ReadAllClothesFrag();
                getParentFragmentManager().beginTransaction().replace(R.id.frag_fl, readAllClothesFrag).addToBackStack(null).commit();
                //getParentFragmentManager().beginTransaction().remove(readClothesFrag.this).commit();
                MainActivity mainActivity = (MainActivity) getActivity();
                LinearLayout weatherLayout= mainActivity.findViewById(R.id.weatherLayout);
                weatherLayout.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    //의류 삭제 메소드
    void deleteClothes(Fragment fragment) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext()).setTitle("의류 삭제").setMessage("정말로 삭제하시겠습니까?")
                //삭제 버튼을 눌렀을 때
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //실시간 데이터베이스에서 데이터 삭제
                        userRef.child(user.getUid()).child("Clothes").child(ClothesKey).setValue(null);

                        //스토리지에서 사진 삭제
                        storageReference.child(ClothesKey + ".png").delete();

                        Toast.makeText(fragment.getContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                        //열람 이전에 보고 있던 화면으로 돌아감
                        MainActivity mainActivity = (MainActivity) getActivity();
                        LinearLayout weatherLayout= mainActivity.findViewById(R.id.weatherLayout);
                        weatherLayout.setVisibility(View.VISIBLE);
                        ReadAllClothesFrag readAllClothesFrag = new ReadAllClothesFrag();
                        getParentFragmentManager().beginTransaction().replace(R.id.frag_fl, readAllClothesFrag).addToBackStack(null).commit();
                        //getParentFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                })
                //취소 버튼을 눌렀을 때
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        AlertDialog dialog = alert.create();
        dialog.show();
    }

}
