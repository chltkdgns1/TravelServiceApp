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
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

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
    private TMapView tMapView;
    private DrawerLayoutClass drawerLayoutClass;
    private EditText EditText_Search;
    private DrawerLayout drawerLayout;
    private ImageView ImageView_Menu, ImageView_Confirm,ImageView_Up;
    private CardView CardView_Bottom;
    private LinearLayout linearLayoutTmap,LinearLayout_Bottom,LinearLayout_Destination;
    private Boolean check_edit = false;
    private TMapData tmapdata;
    private Bitmap []bitmap;
    private RoundedLayout Rounded_Layout;
    private LinearLayout.LayoutParams Rounded_Layout_param ,CardView_Bottom_param,LinearLayout_Bottom_param,LinearLayout_Destination_param;
    private Boolean updown = true, start_check= false;
    private DestinationsClass destinationsClass;
    private List<DestinationDataClass> data;

    private Geocoder geocoder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        init();

        tMapView.setSKTMapApiKey(key);
        linearLayoutTmap.addView(tMapView);


    }

    void init() {
        // drawerLayoutClass = new DrawerLayoutClass(); // Drawer
        // drawerLayoutClass.init(this); // Drawer 초기화

        int []picture = {R.drawable.zero,R.drawable.one,R.drawable.two,
                R.drawable.three,R.drawable.four,R.drawable.five,R.drawable.six,
                R.drawable.seven,R.drawable.eight,R.drawable.nine};

        bitmap = new Bitmap[10];
        for(int i=0;i<picture.length; i++){
            bitmap[i] = BitmapFactory.decodeResource(getResources(), picture[i]);
        }

        destinationsClass = new DestinationsClass();
        destinationsClass.execute(this,getApplicationContext());

        LinearLayout_Destination = findViewById(R.id.LinearLayout_Destination);
        LinearLayout_Destination.setVisibility(View.GONE);
        LinearLayout_Bottom = findViewById(R.id.LinearLayout_Bottom);


        tmapdata = new TMapData();

        ImageView_Confirm = findViewById(R.id.ImageView_Confirm);
        ImageView_Confirm.setClickable(true);

        ImageView_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = EditText_Search.getText().toString();
                Rounded_Layout.setVisibility(View.VISIBLE);
                CardView_Bottom.setVisibility(View.VISIBLE);
                check_edit =false;
                if (txt.isEmpty()) return;

                destinationsClass.clear();
                tmapdata.findAllPOI(txt, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList poiItem) {
                        for (int i = 0; i < poiItem.size(); i++) {
                            if(i == 10) break;
                            TMapPOIItem item = (TMapPOIItem) poiItem.get(i);

                            /*
                            Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                    "Point: " + item.getPOIPoint().toString());

                             */

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
                            // marker.setCalloutLeftImage(bitmap);
                            //Rect a = new Rect(2,2,1,1);
                            // marker.setCalloutRect(a);
                            //marker.setAutoCalloutVisible(true);
                            marker.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
                            tMapView.addMarkerItem(Integer.toString(i + 1), marker); // 지도에 마커 추가
                        }

                    }
                });
            }
        });

        EditText_Search = findViewById(R.id.EditText_Search);
        EditTextTouch();

        CardView_Bottom = findViewById(R.id.CardView_Bottom);

        drawerLayout = findViewById(R.id.drawer);
        ImageView_Menu = findViewById(R.id.ImageView_Menu);
        ImageView_Menu.setClickable(true);

        ImageView_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        Rounded_Layout = findViewById(R.id.Rounded_Layout);

        // 커스텀 뷰의 경우에는 직접 만든 뷰 입니다. 제공해주는 뷰가 아닙니다.
        // 따라서 이 뷰에 대해서 알고 싶다면 RoundedLayout.class 를 참고하시면 됩니다.
        Rounded_Layout.setCornerRadius(100);
        // rl.setShapeCircle(true); // 구글맵의 모양을 둥글게 만들어줍니다.
        Rounded_Layout.showBorder(true);  // 경계선 생성
        Rounded_Layout.setBorderWidth(3);  // 경계선 두께

        Rounded_Layout_param = (LinearLayout.LayoutParams) Rounded_Layout.getLayoutParams(); //  지도
        CardView_Bottom_param = (LinearLayout.LayoutParams) CardView_Bottom.getLayoutParams(); // 아래쪽
        LinearLayout_Bottom_param = (LinearLayout.LayoutParams) LinearLayout_Bottom.getLayoutParams(); // 아래쪽에 포함된 레이아웃
        LinearLayout_Destination_param = (LinearLayout.LayoutParams) LinearLayout_Destination.getLayoutParams();

        ImageView_Up = findViewById(R.id.ImageView_Up);
        ImageView_Up.setClickable(true);

        ImageView_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start_check == false) {
                    start_check = true;
                    LinearLayout_Destination.setVisibility(View.VISIBLE);
                }

                if(updown == true){
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
                }
                else{
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

    private void EditTextTouch() {
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
