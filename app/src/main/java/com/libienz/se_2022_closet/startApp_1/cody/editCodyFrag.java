package com.libienz.se_2022_closet.startApp_1.cody;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.libienz.se_2022_closet.R;

public class editCodyFrag extends Fragment {

    private String CodyKey; //수정할 코디 세트의 key

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_cody, container, false);

        //수정할 코디 세트의 key를 readCodyFrag로부터 받아옴
        CodyKey = getArguments().getString("CodyKey");


        //TODO : 코디 세트 수정 기능 만들기...
        //TODO : Layout도 만들기...

        return view;
    }
}