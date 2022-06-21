package com.libienz.se_2022_closet.startApp_1.favorites;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.clothes.ClothesAdapter;
import com.libienz.se_2022_closet.startApp_1.clothes.readClothesFrag;
import com.libienz.se_2022_closet.startApp_1.cody.CodyAdapter;
import com.libienz.se_2022_closet.startApp_1.cody.readCodyFrag;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;
import com.libienz.se_2022_closet.startApp_1.data.Cody;

import java.util.ArrayList;

public class favoritesActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("user");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference().child("clothes").child(user.getUid());

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private favoriteClothesFrag favClothesFrag;
    private favoriteCodyFrag favCodyFrag;
    private boolean isFrag = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        RadioGroup show_what_rg = (RadioGroup) findViewById(R.id.show_what_rg);
        RadioButton show_favoriteClothes_rb = (RadioButton) findViewById(R.id.show_favoriteClothes_rb);
        RadioButton show_favoriteCodySets_rb = (RadioButton) findViewById(R.id.show_favoriteCodySets_rb);
        favClothesFrag= new favoriteClothesFrag();
        favCodyFrag = new favoriteCodyFrag();
        fragmentManager = getSupportFragmentManager();


        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frag_fl, favClothesFrag).commit();


        //의류랑 코디 세트 중 어느 것을 보여줄 것인지 체크
        show_what_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (show_favoriteClothes_rb.isChecked()) { //옷
                    //clearFavoriteListArray();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frag_fl, favClothesFrag).addToBackStack(null).commit();
                    isFrag = true;

                }
                else { //코디세트
                    //clearFavoriteListArray();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frag_fl, favCodyFrag).addToBackStack(null).commit();
                    isFrag = true;
                }
            }
        });





        /*
        //어댑터
        FavoriteClothesAdapter favoriteClothesAdapter = new FavoriteClothesAdapter(favoriteClothes, getApplicationContext());
        favoriteClothesAdapter.setOnItemClickListener(new FavoriteClothesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                readClothesFrag fragment = new readClothesFrag();

                //열람 프래그먼트 띄움, 프래그먼트 띄울 프레임 추가가 필요함
                //getSupportFragmentManager().beginTransaction().replace("프래그먼트 들어갈 프레임 이름", fragment).commit();

                //ClothesKey를 열람 프래그먼트에 번들로 전달
                String ClothesKey = favoriteClothes.get(pos).getClothesKey();

                Bundle bundle = new Bundle();
                bundle.putString("ClothesKey", ClothesKey);

                fragment.setArguments(bundle);

                Log.d("Clicked", favoriteClothes.get(pos).getClothesKey());
            }
        });

        //검색 결과 클릭했을 때 열람 페이지를 띄움 (코디)
        FavoriteCodySetsAdapter favoriteCodySetsAdapter = new FavoriteCodySetsAdapter(favoriteCodySets);
        favoriteCodySetsAdapter.setOnItemClickListener(new FavoriteCodySetsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                readCodyFrag fragment2 = new readCodyFrag();

                String CodyKey = favoriteCodySets.get(pos).getCodyKey();

                Bundle bundle = new Bundle();
                bundle.putString("CodyKey", CodyKey);

                fragment2.setArguments(bundle);

                Log.d("Clicked", favoriteCodySets.get(pos).getCodyKey());



            }
        }); */



    }
}
