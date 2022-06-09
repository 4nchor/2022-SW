package com.libienz.se_2022_closet.startApp_1.userauth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.clothes.ReadAllClothesFrag;
import com.libienz.se_2022_closet.startApp_1.clothes.addClothesFrag;
import com.libienz.se_2022_closet.startApp_1.clothes.readClothesFrag;
import com.libienz.se_2022_closet.startApp_1.clothes.showDetailsActivity;
import com.libienz.se_2022_closet.startApp_1.cody.addCodyFrag;
import com.libienz.se_2022_closet.startApp_1.clothes.searchOutfitActivity;
import com.libienz.se_2022_closet.startApp_1.cody.readCodyFrag;
import com.libienz.se_2022_closet.startApp_1.ootd.OOTDActivity;
import com.libienz.se_2022_closet.startApp_1.util.RequestHttpUrlConnection;
import com.libienz.se_2022_closet.startApp_1.util.WeatherModel;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private addClothesFrag addClothesFrag;
    private com.libienz.se_2022_closet.startApp_1.clothes.readClothesFrag readClothesFrag;
    private ReadAllClothesFrag readAllClothesFrag;
    private addCodyFrag addCodyFrag;
    private readCodyFrag readCodyFrag;
    private boolean isFrag = false; //프래그먼트 백스택에 남은 것이 있는지 여부를 나타내는 변수


    public static String TAG = "["+MainActivity.class.getSimpleName() +"] ";
    Context context = MainActivity.this;
    TextView tv_temp;
    TextView tv_rcmd_outfit;

    private long backKeyPressedTime = 0;

    String strUrl = "";  //통신할 URL
    NetworkTask networkTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        fragmentManager = getSupportFragmentManager();

        addClothesFrag = new addClothesFrag();
        readAllClothesFrag = new ReadAllClothesFrag();
        addCodyFrag = new addCodyFrag();
        readCodyFrag = new readCodyFrag();



        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frag_fl, readAllClothesFrag).commit();

        strUrl = getString(R.string.weather_url)+"data/2.5/weather";  //Strings.xml 의 weather_url 로 통신할 URL 사용

        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_rcmd_outfit=(TextView) findViewById(R.id.tv_rcmd_outfit);
        requestNetwork();

        ImageButton logout_btn = (ImageButton) findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
                startActivity(intent);
            }
        });

        ImageButton ootd_btn = (ImageButton) findViewById(R.id.ootd_btn);
        ootd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OOTDActivity.class);
                startActivity(intent);
            }
        });

        ImageButton addClothes_btn = (ImageButton) findViewById(R.id.addClothes_btn);
        addClothes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frag_fl, addClothesFrag).addToBackStack(null).commit();
                isFrag = true;*/

                Intent intent = new Intent(getApplicationContext(), showDetailsActivity.class);
                startActivity(intent);

            }
        });


        ImageButton searchOutfit_btn = (ImageButton) findViewById(R.id.searchOutfit_btn);
        searchOutfit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), searchOutfitActivity.class);
                startActivity(intent);
            }
        });

        Button addCody_btn = (Button) findViewById(R.id.addCody_btn);
        addCody_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frag_fl, addCodyFrag).addToBackStack(null).commit();
                isFrag = true;
            }
        });

        Button readCody_btn = (Button) findViewById(R.id.readCody_btn);
        readCody_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frag_fl, readCodyFrag).addToBackStack(null).commit();
                isFrag = true;
            }
        });
    }

    //뒤로가기 버튼 두 번 누르면 앱 종료
    @Override
    public void onBackPressed() {
        if (isFrag == true) {
            super.onBackPressed();
            if (fragmentManager.getBackStackEntryCount() == 0) isFrag = false;
        }
        else if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        else finish();
    }


    /* NetworkTask 를 요청하기 위한 메소드 */
    private void requestNetwork() {
        ContentValues values = new ContentValues();
        values.put("q", "seoul");
        values.put("appid", getString(R.string.weather_app_id));

        networkTask = new NetworkTask(context, strUrl, values);
        networkTask.execute();
    }

    /* 비동기 처리를 위해 AsyncTask 상속한 NetworkTask 클래스 */
    public class NetworkTask extends AsyncTask<Void, Void, String> {
        Context context;
        String url = "";
        ContentValues values;

        public NetworkTask(Context context, String url, ContentValues values) {
            this.context = context;
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";

            RequestHttpUrlConnection requestHttpUrlConnection = new RequestHttpUrlConnection();
            result = requestHttpUrlConnection.request(url, values, "GET");  //HttpURLConnection 통신 요청

            //Log.d(TAG, "NetworkTask >> doInBackground() - result : " + result);
            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.d(TAG, "NetworkTask >> onPostExecute() - result : " + result);

            if (result != null && !result.equals("")) {
                JsonParser jp = new JsonParser();
                JsonObject jsonObject = (JsonObject) jp.parse(result);
                JsonObject jsonObjectMain = (JsonObject) jp.parse(jsonObject.get("main").getAsJsonObject().toString());

                WeatherModel model = new WeatherModel();
                model.setTemp(jsonObjectMain.get("temp").getAsDouble() - 273.15);

                setWeatherData(model);  //UI 업데이트

            } else {
                showFailPop();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }  //NetworkTask End


    /* 통신하여 받아온 날씨 데이터를 통해 UI 업데이트 메소드 */
    private void setWeatherData(WeatherModel model) {
        //Log.d(TAG, "setWeatherData");
        double temp=model.getTemp();
        String rcmdOutfit=null;
        if(temp<4){
            rcmdOutfit= "패딩이나 두꺼운 코트, 목도리와 장갑";
        }
        else if(temp>=5 && temp<8){
            rcmdOutfit="코트,레더자켓,니트,플리스";
        }
        else if(temp>=9 && temp<12){
            rcmdOutfit="트렌치코트, 야상점퍼, 자켓";
        }
        else if(temp>=12 && temp<17){
            rcmdOutfit="기모후드티, 가디건, 맨투맨";
        }
        else if(temp>=17 && temp<20){
            rcmdOutfit="후드티, 바람막이, 슬랙스";
        }
        else if(temp>=20 && temp<23){
            rcmdOutfit="셔츠, 얇은 바지";
        }
        else if(temp>=23 && temp<27){
            rcmdOutfit="반팔티, 반바지";
        }
        else{
            rcmdOutfit="민소매, 반바지, 샌들";
        }
        tv_temp.setText(doubleToStrFormat(1, model.getTemp()) + " °C");  //소수점 2번째 자리까지 반올림하기
        tv_rcmd_outfit.setText(rcmdOutfit);
    }


    /* 통신 실패시 AlertDialog 표시하는 메소드 */
    private void showFailPop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title").setMessage("통신실패");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    /* 소수점 n번째 자리까지 반올림하기 */
    private String doubleToStrFormat(int n, double value) {
        return String.format("%."+n+"f", value);
    }

    private Integer doubleToInt(double value){
        int intValue = (int) value;
        return intValue;
    }


}