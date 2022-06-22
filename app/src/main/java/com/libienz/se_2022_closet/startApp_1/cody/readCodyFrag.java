package com.libienz.se_2022_closet.startApp_1.cody;

import static com.libienz.se_2022_closet.startApp_1.util.FirebaseReference.userRef;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.clothes.ClothesAdapter;
import com.libienz.se_2022_closet.startApp_1.clothes.RecyclerViewAdapter;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;
import com.libienz.se_2022_closet.startApp_1.data.Cody;

import java.util.ArrayList;
import java.util.Iterator;

public class readCodyFrag extends Fragment {

    private String clothesKey = null;
    private ClothesAdapter adapter;
    private ArrayList<Clothes> clothes;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference().child("clothes").child(user.getUid());
    Context context;

    //TODO : searchOutfitActivity에서 검색 결과를 클릭하면 readCodyFrag로 넘어오는 리스너가 먹통이라 지금은 CodyKey가 정적으로 초기화되어 있습니다. searchOutfitActivity를 해결하면 아래 CodyKey = "코디세트1"로 초기화돼 있는 부분을 삭제합니다.
    private String CodyKey = "cody1";

    private ArrayList<String> codyComp = new ArrayList<String>();
    private ArrayList<String> tag = new ArrayList<String>();

    private ArrayList<String> allClothes = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_read_cody, container, false);

        //다른 프래그먼트나 액티비티에서 넘겨 준 CodyKey 받아옴
        if (getArguments() != null) {
            CodyKey = getArguments().getString("CodyKey");
        }

        //즐겨찾기 상태
        Button isfavorite_btn = (Button) view.findViewById(R.id.isfavorite_btn);
        userRef.child(user.getUid()).child("Cody").child(CodyKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Cody cody = snapshot.getValue(Cody.class);

                if (!cody.getIsFavoriteCody()){ //즐겨찾기 추가
                    isfavorite_btn.setText("즐겨찾기");
                }
                else{ //즐겨찾기 해제
                    isfavorite_btn.setText("즐겨찾기 해제");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //코디 정보를 띄우는 코드
        userRef.child(user.getUid()).child("Cody").child(CodyKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Cody cody = snapshot.getValue(Cody.class);

                codyComp = cody.getCodyComp();
                tag = cody.getCodyTag();

                updateCody(cody);

                TextView readCodyKey_tv = (TextView) view.findViewById(R.id.readCodyKey_tv);
                TextView readCodyTag_tv = (TextView) view.findViewById(R.id.readCodyTag_tv);

                //코디 이름 출력
                readCodyKey_tv.setText("  " + CodyKey);

                //태그 정보 출력
                for (int i = 0; i < tag.size(); i++)
                    readCodyTag_tv.append("#" + tag.get(i) + " ");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(container.getContext(), "의류 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //완료 : 구성 의류 목록을 출력합니다 (구성 의류들의 Key는 ArrayList<String> codyComp에 저장되어 있습니다. Line 71의 updateCody() 참고)
        //리사이클러뷰와 어댑터 세팅

        clothes = new ArrayList<>();
        //context = container.getContext();

        for(int key = 0; key < codyComp.size(); key++) {
            userRef.child(user.getUid()).child("Clothes").child(codyComp.get(key)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    clothes.add(snapshot.getValue(Clothes.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(container.getContext(), "의류 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        RecyclerView readCodycomp_rv = (RecyclerView) view.findViewById(R.id.readCodycomp_rv);
        readCodycomp_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        readCodycomp_rv.setHasFixedSize(true);
        readCodycomp_rv.scrollToPosition(0);
        //출력
        ClothesAdapter adapter = new ClothesAdapter(clothes);
        readCodycomp_rv.setAdapter(adapter);
        readCodycomp_rv.setItemAnimator(new DefaultItemAnimator());



        //즐겨찾기 추가 버튼 클릭
        isfavorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child(user.getUid()).child("Cody").child(CodyKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Cody cody = snapshot.getValue(Cody.class);

                        if (!cody.getIsFavoriteCody()){ //즐겨찾기 추가 여기가 문제...왜지
                            userRef.child(user.getUid()).child("Cody").child(CodyKey).child("isFavoriteCody").setValue(true);
                            Log.d("addFavoriteCody", "isFavorite :"+cody.getIsFavoriteCody());
                            isfavorite_btn.setText("즐겨찾기 해제");
                            Toast.makeText(container.getContext(), "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show();
                        }
                        else{ //즐겨찾기 해제
                            userRef.child(user.getUid()).child("Cody").child(CodyKey).child("isFavoriteCody").setValue(false);
                            Log.d("removeFavoriteCody", "isFavorite :"+cody.getIsFavoriteCody());
                            isfavorite_btn.setText("즐겨찾기");
                            Toast.makeText(container.getContext(), "즐겨찾기 해제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        //코디 수정 버튼을 클릭했을 때
        Button editCody_btn = (Button) view.findViewById(R.id.editCody_btn);
        editCody_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //열람 중이었던 코디의 키값을 수정 프래그먼트에 넘김
                Bundle bundle = new Bundle();
                bundle.putString("CodyKey", CodyKey);

                editCodyFrag editCodyFrag = new editCodyFrag();
                editCodyFrag.setArguments(bundle);

                //열람 중이었던 코디를 수정하도록 함
                getParentFragmentManager().beginTransaction().replace(R.id.frag_fl , editCodyFrag).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        //코디 삭제 버튼을 클릭했을 때
        Button deleteCody_btn = (Button) view.findViewById(R.id.deleteCody_btn);
        deleteCody_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                deleteCody(readCodyFrag.this);
            }
        });

        return view;
    }

    //코디 삭제 메소드
    void deleteCody(Fragment fragment) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext()).setTitle("코디 삭제").setMessage("정말로 삭제하시겠습니까?")
                //삭제 버튼을 눌렀을 때
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //실시간 데이터베이스에서 데이터 삭제
                        userRef.child(user.getUid()).child("Cody").child(CodyKey).setValue(null);

                        Toast.makeText(fragment.getContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                        //열람 이전에 보고 있던 화면으로 돌아감
                        getParentFragmentManager().beginTransaction().remove(fragment).commit();
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

    //코디 세트의 구성 의류를 업데이트 해 주는 메소드
    //코디 세트를 구성하고 있던 의류가 삭제되었을 수 있기 때문에, 그 사실을 점검해 줌
    public void updateCody(Cody cody) {
        userRef.child(user.getUid()).child("Clothes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot cloth_snapshot : snapshot.getChildren()) {
                    String ClothesKey = cloth_snapshot.getValue(Clothes.class).getClothesKey();
                    allClothes.add(ClothesKey);
                }
                Log.d("All Clothes", allClothes.toString());

                Log.d("수정 전 원본 Codycomp", codyComp.toString());
                for (Iterator<String> itr = codyComp.iterator(); itr.hasNext();){
                    String codycomp = itr.next();
                    if (!allClothes.contains(codycomp)) itr.remove();
                }
                Log.d("수정 후 Codycomp", codyComp.toString());

                userRef.child(user.getUid()).child("Cody").child(CodyKey).child("codyComp").setValue(codyComp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
