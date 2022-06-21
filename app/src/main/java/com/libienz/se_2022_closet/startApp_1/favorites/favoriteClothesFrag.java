package com.libienz.se_2022_closet.startApp_1.favorites;

import static com.libienz.se_2022_closet.startApp_1.util.FirebaseReference.userRef;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.clothes.RecyclerViewAdapter;
import com.libienz.se_2022_closet.startApp_1.clothes.readClothesFrag;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;

import java.util.ArrayList;

public class favoriteClothesFrag extends Fragment {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference().child("clothes").child(user.getUid());

    private RecyclerView mRecyclerView;
    private FavoriteClothesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Clothes> mFavClothesList;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites_recyclerview, container, false);

        Log.d("oncre", "onCreateView성공: ");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.favorite_rv);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager= new GridLayoutManager(getActivity(),3);
        //mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        mFavClothesList=new ArrayList<>();

        context=container.getContext();
        mAdapter = new FavoriteClothesAdapter(mFavClothesList, context);



        userRef.child(user.getUid()).child("Clothes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFavClothesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //여러값을 하나씩 불러옴
                    Clothes clothes = snapshot.getValue(Clothes.class);
                    if (clothes.getIsFavoriteClothes()){
                        mFavClothesList.add(clothes);
                        Log.d("fav clothes", "개수: "+ mFavClothesList.size());
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
