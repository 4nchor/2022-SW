package com.libienz.se_2022_closet.startApp_1.userauth;

import static com.libienz.se_2022_closet.startApp_1.util.FirebaseReference.userRef;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.clothes.readClothesFrag;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;

import java.util.ArrayList;

public class ReadAllClothesFrag extends Fragment {



    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference().child("Clothes").child(user.getUid());

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Clothes> mClothesList;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_read_all_clothes, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.clothes_recyclerview);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager= new GridLayoutManager(getActivity(),3);
        //mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        mClothesList=new ArrayList<>();

        context=container.getContext();
        mRecyclerAdapter = new RecyclerViewAdapter(mClothesList, context);




        userRef.child(user.getUid()).child("Clothes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mClothesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //여러값을 하나씩 불러옴
                    Clothes clothes = snapshot.getValue(Clothes.class);
                    mClothesList.add(clothes);

                    Log.d("ReadAllClothesFrag", "Single ValueEventListener : " + snapshot.getValue());
                }
                mRecyclerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRecyclerAdapter.setOnItemClickListener (new RecyclerViewAdapter.OnItemClickListener() {

            //아이템 클릭시
            @Override
            public void onItemClick(View view, int position) {
                //의류 열람 페이지로 넘어감
                /*Intent intent = new Intent(getActivity(), readClothesFrag.class);
                startActivity(intent);*/
                Toast.makeText(view.getContext(),"클릭아이템: "+mClothesList.get(position).getClothesKey(),Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle(); // 번들을 통해 값 전달
                bundle.putString("ClothesKey",mClothesList.get(position).getClothesKey());//번들에 넘길 값 저장
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                readClothesFrag fragment2 = new readClothesFrag();//프래그먼트2 선언
                fragment2.setArguments(bundle);//번들을 프래그먼트2로 보낼 준비
                transaction.replace(R.id.frag_fl, fragment2);
                transaction.commit();

            }

        });


        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view; //super.onCreateView(inflater, container, savedInstanceState)
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}