package com.libienz.se_2022_closet.startApp_1.util;
import android.content.ContentValues;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class RequestHttpUrlConnection {
    public String TAG = RequestHttpUrlConnection.class.getSimpleName();

    public String request(String _url, ContentValues _params, String method) {

        String result = "";
        try{
            Log.d(TAG, "----- request() - _url : "+_url);
            Log.d(TAG, "----- request() - _params : "+_params.toString());

            StringBuffer sbParams = new StringBuffer();
            String data = "";

            if (_params == null){
                sbParams.append("");
            } else {  //파라미터가 있는 경우
                //파라미터가 2개 이상이면 파라미터를 &로 연결할 변수 생성
                boolean isAnd = false;
                //파라미터 키와 값
                String key;
                String value;

                for(Map.Entry<String, Object> parameter : _params.valueSet()){
                    key = parameter.getKey();
                    value = parameter.getValue().toString();
                    //파라미터가 두개 이상일때, 파라미터 사이에 &로 연결
                    if (isAnd){
                        sbParams.append("&");
                    }
                    sbParams.append(key).append("=").append(value);
                    if (!isAnd){
                        if (_params.size() >= 2){
                            isAnd = true;
                        }
                    }
                }
            }
            data = sbParams.toString();

            URL url = new URL(method == "POST" ? _url : _url+"?"+data);  //URL 문자열을 이용해 URL 객체 생성
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  //URL 객체를 이용해 HttpUrlConnection 객체 생성
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDefaultUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod(method);
//            conn.setRequestMethod("POST");
//            conn.setRequestMethod("GET");

            Log.d(TAG, "----- request() - url+param : "+url+"?"+data);

            // 서버로 값 전송
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(data.getBytes("UTF-8"));
            outputStream.flush();

            // Response 데이터 처리
            int responseCode = conn.getResponseCode();
            Log.d(TAG, "----- request() - responseCode : "+responseCode);
            if(responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                try {
                    InputStreamReader in = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(in);  //응답 결과를 읽기 위한 스트림 객체 생성
                    String line = "";
                    while((line = reader.readLine()) != null) {
                        builder.append(line + "\n");
                    }
                    result = builder.toString();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            } else {
                result = conn.getResponseMessage();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        Log.d(TAG, "----- request() - result : "+result.trim());
        return result.trim();
    }
}
