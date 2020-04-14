package com.example.myapplicationdw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.enableDefaults();



        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{

                    StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getCtyCodeList"); /*URL*/
                    urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=xWnNNGe92sFXphicn%2BULRMC%2B47oQpNO3c6xlPcn52hD2IMovPgiJDwbEb6HcNb2hQPFZip5ctSr1Lz3hw6U%2FsA%3D%3D"); /*Service Key*/
                    urlBuilder.append("&" + URLEncoder.encode("파라미터영문명","UTF-8") + "=" + URLEncoder.encode("파라미터기본값", "UTF-8")); /*파라미터설명*/
                    URL url = new URL(urlBuilder.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type", "application/json");
                    System.out.println("Response code: " + conn.getResponseCode());
                    BufferedReader rd;
                    if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    } else {
                        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    }
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                    rd.close();
                    conn.disconnect();
                    System.out.println(sb.toString());

                    XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserFactory.newPullParser() ;

                    final int STEP_NONE = 0 ;
                    final int STEP_citycode = 1 ;
                    final int STEP_cityname = 2 ;

                    FileInputStream fis = new FileInputStream("file.xml") ;
                    parser.setInput(fis, null) ;
                    String CityCode = null ;
                    String CityName = null ;
                    int step = STEP_NONE ;


                    int eventType = parser.getEventType() ;
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                        } else if (eventType == XmlPullParser.START_TAG) {
                            String startTag = parser.getName() ;
                            if (startTag.equals("citycode")) {
                                step = STEP_citycode ;
                            } else if (startTag.equals("cityname")) {
                                step = STEP_citycode ;
                            } else {
                                step = STEP_NONE ;
                            }
                        } else if (eventType == XmlPullParser.END_TAG) {
                            String endTag = parser.getName() ;
                            if ((endTag.equals("NO") && step != STEP_citycode) ||
                                    endTag.equals("NAME") && step != STEP_cityname))
                            {

                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            String text = parser.getText() ;
                            if (step == STEP_citycode) {
                                CityCode = text ;
                            } else if (step == STEP_cityname) {
                                CityName = text ;
                            }
                        }

                        eventType = parser.next();
                    }


                }
                catch(Exception e) {


                }

            }
        }).start();

    }
}
