package com.gkftndltek.busapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView busDataTextView;
    private EditText TextInputEdit_What,TextInputEdit_How;
    private Button button;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            BusData data = (BusData)msg.obj;
            busDataTextView.setText("버스데이터 정보 : " + data.getData());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        busDataTextView = findViewById(R.id.busDataTextView);
        button = findViewById(R.id.Buttons);
        TextInputEdit_What = findViewById(R.id.TextInputEdit_What);
        TextInputEdit_How = findViewById(R.id.TextInputEdit_How);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String what = TextInputEdit_What.getText().toString();
                String how = TextInputEdit_How.getText().toString();
                if(what.isEmpty() ||  how.isEmpty()) return;
                getData(what,how);
            }
        });
    }

    void getData(String w,String h){
        final String tw  = w, th = h;
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteInfoIem"); /*URL*/
                    urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=gCa0yyUjmoFRQ7zCUzfNHY7k9ALLNm5cVLAcR7%2FCshhr2CX8nbcskiiDPgkNOpnQ6JCt5sQIMC%2BgOkPlCdVgIQ%3D%3D"); /*Service Key*/
                    urlBuilder.append("&" + URLEncoder.encode("cityCode","UTF-8") + "=" + URLEncoder.encode(tw, "UTF-8")); /*도시코드*/
                    urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode(th, "UTF-8")); /*노선ID*/
                    URL url = new URL(urlBuilder.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type", "application/json");
                    conn.disconnect();
                    //System.out.println(sb.toString());

                    XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserFactory.newPullParser() ;
                    InputStream in = url.openStream();
                    parser.setInput(in,"UTF-8");

                    int eventType = parser.getEventType();
                    int tagType = -1;
                    BusData data = new BusData();
                    while(eventType != XmlPullParser.END_DOCUMENT){
                        if(eventType == XmlPullParser.START_TAG){
                            String tagName = parser.getName();
                            if(tagName.equals("endnodenm")) tagType = 1;
                            else if(tagName.equals("endvehicletime")) tagType = 2;
                            else if(tagName.equals("intervalsattime"))  tagType = 3;
                            else if(tagName.equals("intervalsuntime")) tagType = 4;
                            else if(tagName.equals("intervaltime")) tagType = 5;
                            else if(tagName.equals("routeid")) tagType = 6;
                            else if(tagName.equals("routeno")) tagType = 7;
                            else if(tagName.equals("routetp")) tagType = 8;
                            else if(tagName.equals("startnodenm")) tagType = 9;
                            else if(tagName.equals("startvehicletime")) tagType = 10;
                        }
                        else if(eventType == XmlPullParser.TEXT){
                            String text = parser.getText();
                            switch (tagType){
                                case 1 :
                                    data.setEndnodenm(text);
                                    break;
                                case 2 :
                                    data.setEndvehicletime(text);
                                    break;
                                case 3 :
                                    data.setIntervalsettime(text);
                                    break;
                                case 4 :
                                    data.setIntervalsuntime(text);
                                    break;
                                case 5 :
                                    data.setIntervaltime(text);
                                    break;
                                case 6 :
                                    data.setRouteid(text);
                                    break;
                                case 7 :
                                    data.setRouteno(text);
                                    break;
                                case 8 :
                                    data.setRoutetp(text);
                                    break;
                                case 9 :
                                    data.setStartnodenm(text);
                                    break;
                                case 10 :
                                    data.setStartvehicletime(text);
                                    break;
                            }

                        }
                        eventType = parser.next();
                    }
                    Message msg = Message.obtain();
                    msg.obj = data;
                    handler.sendMessage(msg);
                }
                catch(Exception e){

                }
            }
        }).start();
    }
}
