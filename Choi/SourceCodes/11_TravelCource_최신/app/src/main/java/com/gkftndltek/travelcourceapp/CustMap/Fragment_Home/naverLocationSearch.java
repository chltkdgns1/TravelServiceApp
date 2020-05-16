package com.gkftndltek.travelcourceapp.CustMap.Fragment_Home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class naverLocationSearch {
    final String clientId = "l1_onti1HCxDDYV7MlsF";//애플리케이션 클라이언트 아이디값";
    final String clientSecret = "PUVE0J9gxf";//애플리케이션 클라이언트 시크릿값";

    // 네이버 api

    private String ans = "";

    private int min(int a,int b){
        if(a < b) return a;
        return b;
    }

    public synchronized void getLocationData(final Handler handlerPushMessage, final String searchObject) { // 검색어 = searchObject로 ;
        // 네트워크 연결은 Thread 생성 필요
        new Thread() {
            @Override
            public void run() {
                try {
                    String text = URLEncoder.encode(searchObject, "UTF-8");
                    String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + text;
                    // String apiURL = "https://openapi.naver.com/v1/search/news.json?query=" + text;
                    // Json 형태로 결과값을 받아옴.

                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    con.connect();

                    int responseCode = con.getResponseCode();

                    BufferedReader br;
                    if (responseCode == 200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }

                    StringBuilder searchResult = new StringBuilder();
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        searchResult.append(inputLine + "\n");
                    }

                    br.close();
                    con.disconnect();

                    String data = searchResult.toString(); // 네이버에서 받아온 데이터
                    System.out.println(data);

                    if (!data.isEmpty()) { // 제이슨 형태로 받아서 해결해줍니다.
                        JSONObject jobj = new JSONObject(data);
                        JSONArray objArr = jobj.getJSONArray("items");

                        int k = objArr.length();
                        int minNumber = min(k - 1,9);
                        for (int i = 0; i < k && i < 10; i++) {
                            naverSearchLocationData naverData = new naverSearchLocationData();
                            JSONObject locationObj = objArr.getJSONObject(i);
                            String title = locationObj.getString("title").replaceAll("<b>", "");
                            title = title.replaceAll("</b>", ""); // 소스코드 <b>
                            naverData.setTitle(title);
                            naverData.setCategory(locationObj.getString("category"));
                            naverData.setDescription(locationObj.getString("description"));
                            naverData.setTel(locationObj.getString("telephone"));
                            naverData.setAddress(locationObj.getString("address"));
                            naverData.setRoadAddress(locationObj.getString("roadAddress"));
                            naverData.setLink(getImageBitmap(getLocationImage(title))); // getLocationImage(title)
                            naverData.setIndex(i);
                            naverData.setUrl(getLocationImage(title));
                            naverData.setEndPoint((i == minNumber ? true : false));
                            // 핸들러로 보내주는거에요

                            Message msg = Message.obtain();
                            msg.obj = naverData;
                            msg.what = 1;
                            handlerPushMessage.sendMessage(msg);
                        }

                    }
                } catch (Exception e) {
                }
            }
        }.start();
    }

    public String getLocationImage(final String searchObject) { // 검색어 = searchObject로
        try {
            String text = URLEncoder.encode(searchObject, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/image?query=" + text;
            // String apiURL = "https://openapi.naver.com/v1/search/webkr.json?query=" + text;
            // Json 형태로 결과값을 받아옴.
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            con.connect();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            StringBuilder searchResult = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                searchResult.append(inputLine + "\n");
            }
            br.close();
            con.disconnect();
            String data = searchResult.toString();
            //System.out.println(data);

            if (!data.isEmpty()) {
                List<naverSearchLocationData> locationData = new ArrayList<>();
                JSONObject jobj = new JSONObject(data);
                JSONArray objArr = jobj.getJSONArray("items");

                JSONObject locationObj = objArr.getJSONObject(0);
                return locationObj.getString("link");
            }
        } catch (Exception e) {

        }
        return null;
    }


    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {

        }
        return bm;
    }
}
