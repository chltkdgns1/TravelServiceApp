package com.gkftndltek.travelcourceapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String key = "l7xx713d4db3b29b418dba74f8af6162f4fb";
    // key 자기 key 바꿔주면 되요 tmap api 키

    private TMapView tMapView; // 티맵 뷰
    private DrawerLayoutClass drawerLayoutClass; // 제 코드에서 사용하고 있지 않음
    private EditText EditText_Search; // 제 맵 검색
    private DrawerLayout drawerLayout; // 드로워 레이아웃
    private ImageView ImageView_Menu, ImageView_Confirm, ImageView_Up; // 메뉴 . 검색 완료 , 왼쪽 하단
    private CardView CardView_Bottom; // 카드뷰
    private LinearLayout linearLayoutTmap, LinearLayout_Bottom, LinearLayout_Destination; // 티맵 , 바텀 카드 뷰 감싸고 있는 레이아웃 ( 아닐수잇음..)
    private Boolean check_edit = false; // 검색창을 눌렀는지 안눌렀는지
    private TMapData tmapdata; // 티맵 데이터
    private Bitmap[] bitmap; // 마커들
    private RoundedLayout Rounded_Layout; // 레이아웃의 형태를 바꿔줘요
    private LinearLayout.LayoutParams Rounded_Layout_param, CardView_Bottom_param, LinearLayout_Bottom_param, LinearLayout_Destination_param;
    // 레이아웃의 속성바꿀 수 있음
    private Boolean updown = true, start_check = false; // 업다운
    private DestinationsClass destinationsClass; // 리사이클러뷰 할때 중간과정 거처가는 클래스
    private List<DestinationDataClass> data;   // 사용하고 있지않음
    private List<naverSearchLocationData> locationData; // 미투 업데이트하면 주석처리함
    private naverLocationSearch naverlocation; // 네이버 로케이션 네이버 api 검색하면 핸드러로 받아와요 정보를


    /*

    서버를 연결하려면
        1. try catch
        2. 쓰레드

    메인쓰레드에서는 시간이 오래걸리는 과정을 거칠 수 없다.
    클릭이나 그런 실시간적인 처리를 할 수 없기 때문

    쓰레드로 처리를하는데

    메인쓰레드에서만 UI 접근 가능

    핸들러를 사용 ( 핸들러는 서브 스레드와 메인스레드의 소통을 위해서 필요)


     */

    private int pictureIndex = 0; // 해당 리사이클러뷰가 갖고 있어야할 마커의 드러블

    final int[] picture = {R.drawable.o0, R.drawable.o1, R.drawable.o2,
            R.drawable.o3, R.drawable.o4, R.drawable.o5, R.drawable.o6,
            R.drawable.o7, R.drawable.o8, R.drawable.o9}; // 마커 drawble

    //private Geocoder geocoder ; 지오코더

    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { // 네이버 검색했다.
                final naverSearchLocationData locationNaverData = (naverSearchLocationData) msg.obj;

                synchronized (this) {
                    tmapdata.findAllPOI(locationNaverData.getAddress(), new TMapData.FindAllPOIListenerCallback() { // tmap 명칭통합검색
                        // 네이버 검색으로 가져온 주소
                        @Override
                        public void onFindAllPOI(ArrayList poiItem) {
                            TMapPOIItem item = (TMapPOIItem) poiItem.get(0); // 구글로 바꾸고
                            String xypos = item.getPOIPoint().toString();
                            String[] xyData = xypos.split(" ");
                            double x = Double.parseDouble(xyData[1]), y = Float.parseFloat(xyData[3]); // 위도 경도

                            DestinationDataClass dstData = new DestinationDataClass();
                            dstData.setDrable(picture[pictureIndex]);
                            dstData.setX(x);
                            dstData.setY(y);
                            dstData.setPhone(locationNaverData.getTel());
                            dstData.setName(locationNaverData.getTitle());
                            dstData.setAddress(locationNaverData.getAddress());
                            dstData.setCategory(locationNaverData.getCategory());
                            dstData.setDescription(locationNaverData.getDescription());
                            dstData.setRoadAddress(locationNaverData.getRoadAddress());
                            dstData.setLink(locationNaverData.getLink());
                            destinationsClass.add(dstData);

                            TMapMarkerItem marker = new TMapMarkerItem();
                            TMapPoint tMapPoint1 = new TMapPoint(x, y);
                            marker.setIcon(bitmap[pictureIndex]);
                            marker.setTMapPoint(tMapPoint1);


                            tMapView.addMarkerItem(item.getPOIAddress(), marker);
                            pictureIndex++;
                        }
                    });
                }

                /*
                locationData = (List<naverSearchLocationData>) msg.obj;
                if (locationData.size() != 0) {
                    pictureIndex = 0;

                    for (int i = 0; i < locationData.size(); i++) {
                        final naverSearchLocationData naverData = locationData.get(i);
                        synchronized (this) {
                            tmapdata.findAllPOI(locationData.get(i).getAddress(), new TMapData.FindAllPOIListenerCallback() {
                                @Override
                                public void onFindAllPOI(ArrayList poiItem) {
                                    TMapPOIItem item = (TMapPOIItem) poiItem.get(0);
                                    String xypos = item.getPOIPoint().toString();
                                    String[] xyData = xypos.split(" ");
                                    double x = Double.parseDouble(xyData[1]), y = Float.parseFloat(xyData[3]);

                                    DestinationDataClass dstData = new DestinationDataClass();
                                    dstData.setDrable(picture[pictureIndex]);
                                    dstData.setX(x);
                                    dstData.setY(y);
                                    dstData.setPhone(naverData.getTel());

                                    String title = naverData.getTitle();
                                    //title = title.replaceAll("<b>","");
                                    //title = title.replaceAll("</b>","");
                                    dstData.setName(title);
                                    dstData.setAddress(naverData.getAddress());
                                    dstData.setCategory(naverData.getCategory());
                                    dstData.setDescription(naverData.getDescription());
                                    dstData.setRoadAddress(naverData.getRoadAddress());

                                    System.out.println(title);
                                    String ans = naverlocation.getLocationImage(title);
                                    dstData.setLink(ans);
                                    System.out.println(ans);


                                    destinationsClass.add(dstData);

                                    TMapMarkerItem marker = new TMapMarkerItem();
                                    TMapPoint tMapPoint1 = new TMapPoint(x, y);
                                    marker.setIcon(bitmap[pictureIndex]);
                                    marker.setTMapPoint(tMapPoint1);
                                    tMapView.addMarkerItem(item.getPOIAddress(), marker);
                                    pictureIndex++;
                                }
                            });
                        }
                    }
                }

                 */
            }
        }
    };


    // recycleView
    //




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
        String a= "시청역 <b>1</b>호선";

        a = a.replace("<br>", "");
        a = a.replace("</br>","");
        System.out.println(a);


 */

        // naverlocation = new naverLocationSearch(); 찾아지는거 확인
        // naverlocation.getLocationData("엽기떡볶이");

        /* 구글 지오코딩
        geocoder = new Geocoder(this);
        double d1 = Double.parseDouble("37.570841");
        double d2 = Double.parseDouble("126.985302");
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(d1, d2, 10); // 얻어올 값의 개수

        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (list != null) {
            if (list.size()==0) {
                System.out.println("dasda");
            }
            else {
                System.out.println(list.get(0).toString());
            }
        }

         */


        init();

        tMapView.setSKTMapApiKey(key);
        linearLayoutTmap.addView(tMapView);
    }

    void init() {

        // drawerLayoutClass = new DrawerLayoutClass(); // Drawer
        // drawerLayoutClass.init(this); // Drawer 초기화




        naverlocation = new naverLocationSearch(); //찾아지는거 확인

        bitmap = new Bitmap[10];
        for (int i = 0; i < picture.length; i++) {
            bitmap[i] = BitmapFactory.decodeResource(getResources(), picture[i]);
        }

        destinationsClass = new DestinationsClass();
        destinationsClass.execute(this, getApplicationContext());

        LinearLayout_Destination = findViewById(R.id.LinearLayout_Destination);
        LinearLayout_Destination.setVisibility(View.GONE);
        LinearLayout_Bottom = findViewById(R.id.LinearLayout_Bottom);


        tmapdata = new TMapData();

        ImageView_Confirm = findViewById(R.id.ImageView_Confirm);
        ImageView_Confirm.setClickable(true);

        ImageView_Confirm.setOnClickListener(new View.OnClickListener() { // 우리가 검색할 주소 확인 버튼 클릭시 발생하는 클릭 이벤트
            @Override
            public void onClick(View v) {
                String txt = EditText_Search.getText().toString();
               Rounded_Layout.setVisibility(View.VISIBLE);
                CardView_Bottom.setVisibility(View.VISIBLE);
                check_edit = false;
                if (txt.isEmpty()) return;

                destinationsClass.clear(); // 현재까지 제공됬던 리사이클러뷰의 모든 리스트 삭제하는 것
                pictureIndex = 0;
                naverlocation.getLocationData(handlerPushMessage, txt); // 네이버로 검색

                // 네이버 api 키를 발급 받아야함

                /*
                if (!locationData.isEmpty()) {

                }
                if (locationData.size() != 0) {
                    pictureIndex = 0;

                    for (int i = 0; i < locationData.size(); i++, pictureIndex++) {
                        synchronized (this) {
                            tmapdata.findAllPOI(locationData.get(i).getAddress(), new TMapData.FindAllPOIListenerCallback() {
                                @Override
                                public void onFindAllPOI(ArrayList poiItem) {

                                /*
                                TMapPOIItem item = (TMapPOIItem) poiItem.get(0);
                                String xypos = item.getPOIPoint().toString();
                                String[] xyData = xypos.split(" ");
                                double x = Double.parseDouble(xyData[1]), y = Float.parseFloat(xyData[3]);

                                DestinationDataClass dstData = new DestinationDataClass();
                                dstData.setDrable(picture[pictureIndex]);
                                dstData.setX(x);
                                dstData.setY(y);
                                destinationsClass.add(dstData);

                                TMapMarkerItem marker = new TMapMarkerItem();
                                TMapPoint tMapPoint1 = new TMapPoint(x, y);
                                marker.setIcon(bitmap[pictureIndex]);
                                marker.setTMapPoint(tMapPoint1);
                                tMapView.addMarkerItem(Integer.toString(pictureIndex + 1), marker);


                                }
                            });
                        }
                    }

                }
                 */
            }
        });

/*
                tmapdata.findAllPOI(txt, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList poiItem) {
                        for (int i = 0; i < poiItem.size(); i++) {
                            if(i == 10) break;
                            TMapPOIItem item = (TMapPOIItem) poiItem.get(i);


                            System.out.println(item.detailAddrName + " " + item.desc + " " +
                                    item.address + " " + item.roadName + " " + item.telNo + " " + item.upperBizName + " " + item.additionalInfo+ " " + item.bizCatName + " " +
                                    item.buildingNo1 + " " + item.buildingNo2 + " " + item.detailBizName + " " + item.detailInfoFlag + " " + item.firstNo + " " + item.lowerAddrName + " "+
                                    item.lowerBizName + " " +item.merchanFlag + " " +item.middleAddrName + " " + item.middleBizName + " " +item.secondNo + " " +item.upperAddrName);

                            String xypos = item.getPOIPoint().toString();
                            String [] xyData = xypos.split(" ");
                            double x = Double.parseDouble(xyData[1]),y = Float.parseFloat(xyData[3]);

                            DestinationDataClass dstData = new DestinationDataClass();
                            dstData.setName(item.getPOIName());
                            String dvid = item.getPOIAddress();
                            String [] divid_data = dvid.split(" ");
                            String address ="";

                            for(int k=0;k<divid_data.length;k++){
                                if(divid_data[k].equals("null")) break;
                                address += divid_data[k] + " ";
                            }

                            dstData.setDrable(picture[i]);
                            dstData.setDistinguish(item.secondNo);
                            dstData.setEx(item.upperBizName);
                            dstData.setPhone(item.telNo);
                            dstData.setAddress(address);
                            dstData.setX(x); dstData.setY(y);

                            destinationsClass.add(dstData);

                            TMapMarkerItem marker = new TMapMarkerItem();
                            TMapPoint tMapPoint1 = new TMapPoint(x, y);
                            marker.setCalloutTitle(Integer.toString(i + 1));
                            marker.setIcon(bitmap[i]);

                            // marker.setCalloutLeftImage(bitmap); # 마커 클릭 여부
                            //Rect a = new Rect(2,2,1,1);
                            // marker.setCalloutRect(a);
                            //marker.setAutoCalloutVisible(true);

                            marker.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
                            tMapView.addMarkerItem(Integer.toString(i + 1), marker); // 지도에 마커 추가
                        }

                    }
                });

                //
            }
 */
        EditText_Search = findViewById(R.id.EditText_Search);
        EditTextTouch();

        CardView_Bottom = findViewById(R.id.CardView_Bottom);

        drawerLayout = findViewById(R.id.drawer);
        ImageView_Menu = findViewById(R.id.ImageView_Menu);
        ImageView_Menu.setClickable(true);

        ImageView_Menu.setOnClickListener(new View.OnClickListener() { //  메뉴버튼 드러워아웃 나타남
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        Rounded_Layout = findViewById(R.id.Rounded_Layout);
        // 커스텀 뷰의 경우에는 직접 만든 뷰 입니다. 제공해주는 뷰가 아닙니다.
        // 따라서 이 뷰에 대해서 알고 싶다면 RoundedLayout.class 를 참고하시면 됩니다.
        Rounded_Layout.setCornerRadius(100);
        //Rounded_Layout.setShapeCircle(true); // 구글맵의 모양을 둥글게 만들어줍니다.
        Rounded_Layout.showBorder(true);  // 경계선 생성
        Rounded_Layout.setBorderWidth(3);  // 경계선 두께

        Rounded_Layout_param = (LinearLayout.LayoutParams) Rounded_Layout.getLayoutParams(); //  지도
        CardView_Bottom_param = (LinearLayout.LayoutParams) CardView_Bottom.getLayoutParams(); // 아래쪽
        LinearLayout_Bottom_param = (LinearLayout.LayoutParams) LinearLayout_Bottom.getLayoutParams(); // 아래쪽에 포함된 레이아웃
        LinearLayout_Destination_param = (LinearLayout.LayoutParams) LinearLayout_Destination.getLayoutParams(); // 레이아웃 비율 설정

        ImageView_Up = findViewById(R.id.ImageView_Up);
        ImageView_Up.setClickable(true);

        ImageView_Up.setOnClickListener(new View.OnClickListener() { // 바텀에 있는 레이아웃 위로 올림
            @Override
            public void onClick(View v) {
                if (start_check == false) {
                    start_check = true;
                    LinearLayout_Destination.setVisibility(View.VISIBLE);
                }

                if (updown == true) {
                    updown = false;
                    Rounded_Layout_param.weight = 0.5f;
                    CardView_Bottom_param.weight = 0.44f;
                    LinearLayout_Bottom_param.weight = 0.1f;
                    LinearLayout_Destination_param.weight = 0.9f;
                    Rounded_Layout.setLayoutParams(Rounded_Layout_param);
                    CardView_Bottom.setLayoutParams(CardView_Bottom_param);
                    LinearLayout_Bottom.setLayoutParams(LinearLayout_Bottom_param);
                    LinearLayout_Destination.setLayoutParams(LinearLayout_Destination_param);
                    ImageView_Up.setImageResource(R.drawable.down);
                } else {
                    updown = true;
                    Rounded_Layout_param.weight = 0.9f;
                    CardView_Bottom_param.weight = 0.04f;
                    LinearLayout_Bottom_param.weight = 1f;
                    LinearLayout_Destination_param.weight = 0f;
                    Rounded_Layout.setLayoutParams(Rounded_Layout_param);
                    CardView_Bottom.setLayoutParams(CardView_Bottom_param);
                    LinearLayout_Bottom.setLayoutParams(LinearLayout_Bottom_param);
                    LinearLayout_Destination.setLayoutParams(LinearLayout_Destination_param);
                    ImageView_Up.setImageResource(R.drawable.up);
                }
            }
        });

        linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
    }

    private void EditTextTouch() { // 텍스트 누르면 맵이 안보임
        EditText_Search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Rounded_Layout.setVisibility(View.GONE);
                    CardView_Bottom.setVisibility(View.GONE);
                    check_edit = true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() { // 뒤로가기 키 눌렀을 때
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            check_edit = false;
        } else if (check_edit == true) {
            Rounded_Layout.setVisibility(View.VISIBLE);
            CardView_Bottom.setVisibility(View.VISIBLE);
            check_edit = false;
        } else {
            check_edit = false;
            AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
            alert_ex.setMessage("정말로 종료하시겠습니까?");

            alert_ex.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert_ex.setNegativeButton("종료", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            alert_ex.setTitle("Notice");
            AlertDialog alert = alert_ex.create();
            alert.show();
        }
    }
}

