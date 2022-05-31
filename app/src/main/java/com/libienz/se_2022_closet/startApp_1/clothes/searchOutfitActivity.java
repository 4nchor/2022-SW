package com.libienz.se_2022_closet.startApp_1.clothes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.data.Clothes;
import com.libienz.se_2022_closet.startApp_1.userauth.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

//검색 결과는 검색창 아래에 프래그먼트 이용해서 띄울 생각인데 아직 프래그먼트를 안 만들어서 연결을 안 했음

public class searchOutfitActivity extends AppCompatActivity {

    private String searchKey; //사용자가 입력하는 검색어

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_outfit);

        TextView searchOutfit_tv = (TextView) findViewById(R.id.searchOutfit_tv);
        EditText searchOutfit_et = (EditText) findViewById(R.id.searchOutfit_et);
        Button search_btn = (Button) findViewById(R.id.search_btn);
        RadioGroup searchWhat_rg = (RadioGroup) findViewById(R.id.searchWhat_rg);
        RadioButton searchClothes_rb = (RadioButton) findViewById(R.id.searchClothes_rb);
        RadioButton searchCody_rb = (RadioButton) findViewById(R.id.searchCody_rb);
        Switch searchCodyforKeyword_swt = (Switch) findViewById(R.id.searchCody_swt);

        searchClothes_rb.setChecked(true);
        search_btn.setVisibility(View.GONE);

        //의류랑 코디 세트 중 무얼 검색할 건지 체크
        searchWhat_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (searchCody_rb.isChecked()) {
                    searchCodyforKeyword_swt.setVisibility(View.VISIBLE);
                    searchOutfit_tv.setText(null);
                }
                else {
                    searchCodyforKeyword_swt.setChecked(false);
                    searchOutfit_tv.setText(null);
                    searchCodyforKeyword_swt.setVisibility(View.GONE);
                }
            }
        });

        //코디 세트를 검색한다면 태그랑 키워드 중 어떤 걸로 검색할 건지 체크
        searchCodyforKeyword_swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (searchCodyforKeyword_swt.isChecked()) {
                    searchOutfit_et.setHint("키워드로 검색");
                    searchOutfit_tv.setText(null);
                }
                else {
                    searchOutfit_et.setHint("태그로 검색");
                    searchOutfit_tv.setText(null);
                }
            }
        });

        //태그로 검색하는 경우 태그를 하나씩 입력할 때마다 검색 결과를 갱신하기 위해 아래와 같이 작성함...
        //따로 검색 버튼 없이 검색어 입력 후 엔터를 누르면 검색되는 방식
        searchOutfit_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchKey = searchOutfit_et.getText().toString();
                    if (!searchCodyforKeyword_swt.isChecked()){
                        searchOutfit_tv.append("  #" + searchKey);
                        searchOutfit_et.setText(null);
                    }
                    search_btn.performClick();
                    return true;
                }
                return false;
            }
        });

        //여기 구현 필요
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchClothes_rb.isChecked()); //의류 검색하는 경우
                else if (searchCody_rb.isChecked() && !searchCodyforKeyword_swt.isChecked()); //태그로 코디 검색하는 경우
                else if (searchCody_rb.isChecked() && searchCodyforKeyword_swt.isChecked()); //키워드로 코디 검색하는 경우
            }
        });
    }

}