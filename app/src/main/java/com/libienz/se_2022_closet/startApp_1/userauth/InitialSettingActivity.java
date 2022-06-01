package com.libienz.se_2022_closet.startApp_1.userauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.libienz.se_2022_closet.R;

public class InitialSettingActivity extends AppCompatActivity {

    Button btnOK;

    TextView text1;
    TextView text2;
    TextView text3;

    RadioGroup preferredSeason;
    RadioButton rdoSpring;
    RadioButton rdoSummer;
    RadioButton rdoFall;
    RadioButton rdoWinter;

    RadioGroup preferredStyle;
    RadioButton rdoLovely;
    RadioButton rdoHip;
    RadioButton rdoCasual;
    RadioButton rdoModern;
    RadioButton rdoUnique;
    RadioButton rdoSimple;

    RadioGroup preferredClothes;
    RadioButton rdoTop;
    RadioButton rdoBottoms;
    RadioButton rdoOuter;
    RadioButton rdoShoes;
    RadioButton rdoAccessory;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setting);
        //setTitle("취향수집");

        text1 = (TextView) findViewById(R.id.textView1);
        preferredSeason = (RadioGroup) findViewById(R.id.rGroup1);
        //radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        rdoSpring = (RadioButton) findViewById(R.id.rdo_spring);
        rdoSummer = (RadioButton) findViewById(R.id.rdo_summer);
        rdoFall = (RadioButton) findViewById(R.id.rdo_fall);
        rdoWinter = (RadioButton) findViewById(R.id.rdo_winter);
        //r_btn1.setOnClickListener(radioButtonClickListener);
        //r_btn2.setOnClickListener(radioButtonClickListener);


        text2 = (TextView) findViewById(R.id.textView2);
        preferredStyle = (RadioGroup) findViewById(R.id.rGroup2);
        rdoLovely = (RadioButton) findViewById(R.id.rdo_lovely);
        rdoHip = (RadioButton) findViewById(R.id.rdo_hip);
        rdoCasual = (RadioButton) findViewById(R.id.rdo_casual);
        rdoModern = (RadioButton) findViewById(R.id.rdo_modern);
        rdoUnique = (RadioButton) findViewById(R.id.rdo_unique);
        rdoSimple = (RadioButton) findViewById(R.id.rdo_simple);

        text3 = (TextView) findViewById(R.id.textView3);
        preferredClothes = (RadioGroup) findViewById(R.id.rGroup3);
        rdoTop = (RadioButton) findViewById(R.id.rdo_top);
        rdoBottoms = (RadioButton) findViewById(R.id.rdo_bottoms);
        rdoOuter = (RadioButton) findViewById(R.id.rdo_outer);
        rdoShoes = (RadioButton) findViewById(R.id.rdo_shoes);
        rdoAccessory = (RadioButton) findViewById(R.id.rdo_accessory);

        btnOK = (Button) findViewById(R.id.ok_btn);
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                Toast.makeText(InitialSettingActivity.this, "고객님의 취향을 반영합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //preferredClothes.getCheckedRadioButtonId(); Group 내에서 체크된 라디오 버튼의 id 값을 반환

    }






}
