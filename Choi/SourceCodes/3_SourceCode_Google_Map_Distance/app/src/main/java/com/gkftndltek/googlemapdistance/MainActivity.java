package com.gkftndltek.googlemapdistance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private String key ="AIzaSyDLe5lfLM8o98AknG-WDPn4CjUl3mCSeds";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?origins=Seattle&destinations=San+Francisco&key=" + key); /*URL*/
                    URL url = new URL(urlBuilder.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type", "application/json");
                    conn.disconnect();

                    int responseCode = conn.getResponseCode();

                    BufferedReader br;
                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    }

                    StringBuilder searchResult = new StringBuilder();
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        searchResult.append(inputLine + "\n");
                    }

                    br.close();
                    conn.disconnect();

                    System.out.println(searchResult);
                }
                catch(Exception e){

                }
            }
        }).start();
    }
}
