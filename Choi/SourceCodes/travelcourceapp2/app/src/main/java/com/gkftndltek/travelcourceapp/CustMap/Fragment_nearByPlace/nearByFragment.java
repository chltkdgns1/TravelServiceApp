package com.gkftndltek.travelcourceapp.CustMap.Fragment_nearByPlace;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.CustMap.Fragment_Home.HomeFragment;
import com.gkftndltek.travelcourceapp.CustMap.Fragment_PersonalMap.NoneBitmapDestData;
import com.gkftndltek.travelcourceapp.CustMap.Fragment_PersonalMap.PersonRecycler.PersonClass;
import com.gkftndltek.travelcourceapp.CustMap.Fragment_nearByPlace.nearByRecyler.nearByRecycleClass;
import com.gkftndltek.travelcourceapp.CustMap.MainActivity;
import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationDataClass;
import com.gkftndltek.travelcourceapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class nearByFragment extends Fragment implements MainActivity.OnBackPressedListeners, OnMapReadyCallback {
    private String key = "l7xx713d4db3b29b418dba74f8af6162f4fb";
    private String placeKey = "wWnaTYGdsZQ3Re7OuX0BGHYE2dx8qBhu%2BxSRsIlWm2%2FtH%2FZa4MGt4X%2FOcfQOChyZqbjDVVOyi3TI2myUb3cw3g%3D%3D";
    private View rootView;
    private Context con;

    // 데이터 베이스
    private String token, mapName;
    private FirebaseDatabase database;
    private DatabaseReference users, logined;
    private Geocoder geocoder;

    // 프레그먼트
    HomeFragment homeFragment;

    nearByRecycleClass nearRecycleClass;
    //  액티비티
    private Activity act;

    // 뷰

    ImageView ImageView_Menu_nearBy,ImageView_Confirm_nearBy;
    EditText EditText_Search_nearBy;

    private Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                placeFindData data = (placeFindData) msg.obj;
                nearRecycleClass.add(data);
            }
            else if(msg.what == 2){
                System.out.println("여기 들어옴?");
                NoneBitmapDestData data = ( NoneBitmapDestData) msg.obj;

                if(data == null) {
                    Toast.makeText(con,"핸드러 예외 발생",Toast.LENGTH_LONG).show();
                    return;
                }

                System.out.println("여기 들어옴?");
                users.child(token).child("maps").child(mapName).child("basket").push().setValue(data);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nearbyplace_home, container, false);
        init();
        return rootView;
    }

    void init() {

        ImageView_Confirm_nearBy = rootView.findViewById(R.id.ImageView_Confirm_nearBy);
        ImageView_Menu_nearBy = rootView.findViewById(R.id.ImageView_Menu_nearBy);
        EditText_Search_nearBy = rootView.findViewById(R.id.EditText_Search_nearBy);

        act = (MainActivity) getActivity();
        con = getActivity();
        homeFragment = new HomeFragment();

        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
        logined = database.getReference("logined");

        token = (((MainActivity)act)).token;
        mapName = (((MainActivity)act)).mapName;

        nearRecycleClass = new nearByRecycleClass();
        nearRecycleClass.execute(rootView,con,handlerPushMessage);

        ImageView_Confirm_nearBy.setClickable(true);
        ImageView_Menu_nearBy.setClickable(true);

        geocoder = new Geocoder(con);

        ImageView_Menu_nearBy.setOnClickListener(new View.OnClickListener() { //  메뉴버튼 드러워아웃 나타남
            @Override
            public void onClick(View v) {
                (((MainActivity)act)).drawerLayout.openDrawer(Gravity.LEFT);
                //  drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        ImageView_Confirm_nearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronized (this) {
                    String text = EditText_Search_nearBy.getText().toString();

                    System.out.println(text);
                    if (text.isEmpty()) return;

                    List<Address> addressList = null;

                    try {
                        addressList = geocoder.getFromLocationName(
                                text, // 주소
                                1); // 최대 검색 결과 개수
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    if(addressList == null) return;
                    String[] splitStr = addressList.get(0).toString().split(",");
                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도

                    getPlace( longitude,latitude);
                }
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {}

    void getPlace(final String x,final String y){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList"); /*URL*/
                    urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + placeKey); /*Service Key*/
                    urlBuilder.append("&" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + URLEncoder.encode(placeKey, "UTF-8")); /*공공데이터포털에서 발급받은 인증키*/
                    urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("20", "UTF-8")); /*한 페이지 결과 수*/
                    urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*현재 페이지 번호*/
                    urlBuilder.append("&" + URLEncoder.encode("MobileOS","UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8")); /*IOS(아이폰),AND(안드로이드),WIN(원도우폰), ETC*/
                    urlBuilder.append("&" + URLEncoder.encode("MobileApp","UTF-8") + "=" + URLEncoder.encode("AppTest", "UTF-8")); /*서비스명=어플명*/
                    urlBuilder.append("&" + URLEncoder.encode("arrange","UTF-8") + "=" + URLEncoder.encode("A", "UTF-8")); /*(A=제목순, B=조회순, C=수정일순, D=생성일순, E=거리순)*/
                    urlBuilder.append("&" + URLEncoder.encode("contentTypeId","UTF-8") + "=" + URLEncoder.encode("14", "UTF-8")); /*관광타입(관광지, 숙박 등) ID*/
                    urlBuilder.append("&" + URLEncoder.encode("mapX","UTF-8") + "=" + URLEncoder.encode(x, "UTF-8")); /*GPS X좌표(WGS84 경도 좌표)*/
                    urlBuilder.append("&" + URLEncoder.encode("mapY","UTF-8") + "=" + URLEncoder.encode(y, "UTF-8")); /*GPS Y좌표(WGS84 위도 좌표)*/
                    urlBuilder.append("&" + URLEncoder.encode("radius","UTF-8") + "=" + URLEncoder.encode("10000", "UTF-8")); /*거리 반경(단위m), Max값 20000m=20Km*/
                    urlBuilder.append("&" + URLEncoder.encode("listYN","UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); /*목록 구분 (Y=목록, N=개수)*/
                    urlBuilder.append("&" + URLEncoder.encode("modifiedtime","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*콘텐츠 수정일*/
                    URL url = new URL(urlBuilder.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type", "application/json");

                    XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserFactory.newPullParser() ;
                    InputStream in = url.openStream();
                    parser.setInput(in,"UTF-8");

                    int eventType = parser.getEventType();
                    int tagType = -1;

                    while(eventType != XmlPullParser.END_DOCUMENT) {
                        int cnt = 0;
                        placeFindData data = new  placeFindData();
                        while (cnt < 5) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = parser.getName();
                                System.out.println(tagName);
                                if (tagName.equals("addr1")) tagType = 1;
                                else if (tagName.equals("firstimage")) tagType = 2;
                                else if (tagName.equals("mapx")) tagType = 3;
                                else if (tagName.equals("mapy")) tagType = 4;
                                else if (tagName.equals("title")) tagType = 5;
                            } else if (eventType == XmlPullParser.TEXT) {
                                String text = parser.getText();
                                switch (tagType) {
                                    case 1:
                                        cnt++;
                                        tagType = -1;
                                        data.setAddres(text);
                                        break;
                                    case 2:
                                        cnt++;
                                        tagType = -1;
                                        data.setImage(text);
                                        break;
                                    case 3:
                                        cnt++;
                                        tagType = -1;
                                        data.setX(Double.parseDouble(text));
                                        break;
                                    case 4:
                                        cnt++;
                                        tagType = -1;
                                        data.setY(Double.parseDouble(text));
                                        break;
                                    case 5:
                                        cnt++;
                                        tagType = -1;
                                        data.setTitle(text);
                                        break;
                                }

                            }
                            eventType = parser.next();
                        }

                        synchronized (this) {
                            Message msg = Message.obtain();
                            msg.obj = data;
                            msg.what = 1;
                            handlerPushMessage .sendMessage(msg);
                            data.setBit(getImageBitmap(data.image));
                            tagType = -1;
                            System.out.println("여기들어옵니까? " + cnt);
                        }
                    }
                }
                catch(Exception e){

                }
            }
        }).start();
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

    @Override
    public void onBack() {
        DrawerLayout drawer = ((MainActivity)act).drawerLayout;

        if (drawer.isDrawerOpen(Gravity.LEFT)){
            drawer.closeDrawer(Gravity.LEFT);
        }
        else getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, homeFragment).commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnBackPressedListener(this); // 프레그먼트
    }
}
