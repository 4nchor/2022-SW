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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.clothes.addClothesFrag;
import com.libienz.se_2022_closet.startApp_1.clothes.readClothesFrag;
import com.libienz.se_2022_closet.startApp_1.clothes.searchOutfitActivity;
import com.libienz.se_2022_closet.startApp_1.ootd.OOTDActivity;
import com.libienz.se_2022_closet.startApp_1.util.RequestHttpUrlConnection;
import com.libienz.se_2022_closet.startApp_1.util.WeatherModel;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private addClothesFrag addClothesFrag;
    private readClothesFrag readClothesFrag;
    private boolean isFrag = false;

    public static String TAG = "["+MainActivity.class.getSimpleName() +"] ";
    Context context = MainActivity.this;
    TextView tv_temp;

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
        readClothesFrag = new readClothesFrag();


        strUrl = getString(R.string.weather_url)+"data/2.5/weather";  //Strings.xml 의 weather_url 로 통신할 URL 사용

        tv_temp = (TextView) findViewById(R.id.tv_temp);
        requestNetwork();

        Button logout_btn = (Button) findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
                startActivity(intent);
            }
        });

        Button ootd_btn = (Button) findViewById(R.id.ootd_btn);
        ootd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OOTDActivity.class);
                startActivity(intent);
            }
        });

        Button addClothes_btn = (Button) findViewById(R.id.addClothes_btn);
        addClothes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.addClothes_fg, addClothesFrag).addToBackStack(null).commit();
                isFrag = true;
            }
        });

        Button readClothes_btn = (Button) findViewById(R.id.readClothes_btn);
        readClothes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.readClothes_fg, readClothesFrag).addToBackStack(null).commit();
                isFrag = true;
            }
        });

        Button searchOutfit_btn = (Button) findViewById(R.id.searchOutfit_btn);
        searchOutfit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), searchOutfitActivity.class);
                startActivity(intent);
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

            Log.d(TAG, "NetworkTask >> doInBackground() - result : " + result);
            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "NetworkTask >> onPostExecute() - result : " + result);

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
        Log.d(TAG, "setWeatherData");
        tv_temp.setText(doubleToStrFormat(1, model.getTemp()) + " °C");  //소수점 2번째 자리까지 반올림하기
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


}