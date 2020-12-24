package com.gkftndltek.travelcourceapp.CustMap.Calender;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.gkftndltek.travelcourceapp.CustMap.Calender.Recycler.CalenderDataClass;
import com.gkftndltek.travelcourceapp.CustMap.Fragment_PersonalMap.NoneBitmapDestData;
import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationDataClass;
import com.gkftndltek.travelcourceapp.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalenderInsertActivity  extends AppCompatActivity {

    private EditText EditText_Calender_Name,EditText_Calender_Content,EditText_Calender_Adress,EditText_Calender_Category;
    private Button Button_Calender_Insert,Button_Calender_Search;

    private ImageView ImageView_Calender_Image;

    private String token,name,address = "",category ="",image="";


    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { // 네이버 검색했다
                ImageView_Calender_Image.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_insert);

        init();
    }

    void init(){

        Intent it = getIntent();
        Bundle bun = it.getExtras();
        token = (String) bun.get("token"); // 유저네임
        name = (String)bun.get("name");   // 맵 네임

        EditText_Calender_Adress = findViewById(R.id.EditText_Calender_Adress);
        EditText_Calender_Category= findViewById(R.id.EditText_Calender_Category);
        EditText_Calender_Name = findViewById(R.id.EditText_Calender_Name);
        EditText_Calender_Content = findViewById(R.id.EditText_Calender_Content);
        Button_Calender_Insert = findViewById(R.id.Button_Calender_Insert);
        Button_Calender_Search = findViewById(R.id.Button_Calender_Search);
        ImageView_Calender_Image = findViewById(R.id.ImageView_Calender_Image);

        Button_Calender_Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText_Calender_Name.getText();
                EditText_Calender_Content.getText();

                Intent intent = new Intent();
                intent.putExtra("name",EditText_Calender_Name.getText().toString() );
                intent.putExtra("content",EditText_Calender_Content.getText().toString() );
                intent.putExtra("address",address);
                intent.putExtra("category",category);
                intent.putExtra("image",image);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        Button_Calender_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CalenderMapConnectActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("token",token);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("여기 들어옴????");
        if(requestCode == 1){ // 추가ㅣ

            System.out.println("여기 들어옴????");

            if(data != null) {
                Bundle bun = data.getExtras();
                NoneBitmapDestData destData = (NoneBitmapDestData) bun.get("data");

                address = destData.getAddress();
                category  = destData.getCategory();
                image = destData.getUrl();

                EditText_Calender_Adress.setText(address);
                EditText_Calender_Category.setText(category);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bit = getImageBitmap(image);
                        Message msg = Message.obtain();
                        msg.obj = bit;
                        msg.what = 1;
                        handlerPushMessage.sendMessage(msg);
                    }
                }).start();
            }
        }
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
