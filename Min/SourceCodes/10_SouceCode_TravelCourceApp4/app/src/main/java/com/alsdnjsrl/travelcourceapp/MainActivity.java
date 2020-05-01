package com.alsdnjsrl.travelcourceapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.material.navigation.NavigationView;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String key = "l7xx78ce6244fbb34c96b88e96ad54e4074e";
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
    private GestureDetector gestureDetector;
    private Context con;

    private int cnt = 0;
    private double MarkerLatitude, MarkerLongtitude;
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


    //  6 이
    private int pictureIndex = 0; // 해당 리사이클러뷰가 갖고 있어야할 마커의 드러블


    //
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
//                            System.out.println("X좌표");
//                            System.out.println(x);
//                            System.out.println("Y좌표");
//                            System.out.println(y);
                            marker.setCanShowCallout(true); // 클릭되는 부분
//                          marker.setAutoCalloutVisible(true);
                            TMapPoint tMapPoint1 = new TMapPoint(x, y);
                            marker.setIcon(bitmap[pictureIndex]);
                            marker.setTMapPoint(tMapPoint1);
//                          marker.setCalloutTitle("나 클릭했엉?");


                            tMapView.addMarkerItem(Integer.toString(pictureIndex), marker);
                            pictureIndex++;
                            MarkerLatitude = dstData.getX();
                            MarkerLongtitude = dstData.getY();
                            if (cnt == 1) {
                                tMapView.setCenterPoint(MarkerLongtitude, MarkerLatitude, true);
                                cnt++;
                            }
                        }
                    });
                }
            } else if (msg.what == 2) {
                DestinationDataClass destData = (DestinationDataClass) msg.obj;
                System.out.println(destData.getX() + " " + destData.getY());
                tMapView.setCenterPoint(destData.getY(), destData.getX(), true);
                // 뭘까요?? ㅋ 뭐긴 시발 반대지 ㅋ
            } else if (msg.what == 3) {
                TMapMarkerItem item = (TMapMarkerItem) msg.obj;
                tMapView.setCenterPoint(item.longitude, item.latitude, true);
            }

            /*
            else if(msg.what == 4){
                AlertDialog.Builder alert_ex = new AlertDialog.Builder(getApplicationContext());
                alert_ex.setMessage("경로에 추가하시겠습니까?");

                alert_ex.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("확인눌렸냐?");
                    }
                });
                alert_ex.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("취소 눌렸냐? 개새이야");
                    }
                });

                alert_ex.setTitle("Notice");
                AlertDialog alert = alert_ex.create();
                alert.show();
            }

             */
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        tMapView.setSKTMapApiKey(key);
        linearLayoutTmap.addView(tMapView);

    }

    void init() {

        // drawerLayoutClass = new DrawerLayoutClass(); // Drawer
        // drawerLayoutClass.init(this); // Drawer 초기화

        // 검색을 해서 comfirm 버튼 클릭햇을 때 확인
        // 마커랑 리사이클러뷰에 출력되잖아.

        // 중요한 맵 은 가만히 있어
        //





        con = this;

        gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {

                return true;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {

                return true;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

                return true;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                /*
                System.out.println("들어오나?");
                Message msg = Message.obtain();
                msg.what = 4;
                handlerPushMessage.sendMessage(msg);

                 */
                AlertDialog.Builder alert_ex = new AlertDialog.Builder(con);
                alert_ex.setMessage("경로에 추가하시겠습니까?");

                alert_ex.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("취소 눌렸냐? 개새이야");
                    }
                });

                alert_ex.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("확인눌렸냐?");
                    }
                });

                alert_ex.setTitle("Notice");
                AlertDialog alert = alert_ex.create();
                alert.show();
            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

                return true;
            }
        });


        naverlocation = new naverLocationSearch(); //찾아지는거 확인

        bitmap = new Bitmap[10];
        for (int i = 0; i < picture.length; i++) {
            bitmap[i] = BitmapFactory.decodeResource(getResources(), picture[i]);
        }

        destinationsClass = new DestinationsClass();
        //destinationsClass.execute(this, getApplicationContext());

        LinearLayout_Destination = findViewById(R.id.LinearLayout_Destination);
        LinearLayout_Destination.setVisibility(View.GONE);
        LinearLayout_Bottom = findViewById(R.id.LinearLayout_Bottom);


        tmapdata = new TMapData();

        ImageView_Confirm = findViewById(R.id.ImageView_Confirm);
        ImageView_Confirm.setClickable(true);

        ImageView_Confirm.setOnClickListener(new View.OnClickListener() { // 우리가 검색할 주소 확인 버튼 클릭시 발생하는 클릭 이벤트
            @Override
            public void onClick(View v) {
                cnt = 1;
                String txt = EditText_Search.getText().toString();
                if (Rounded_Layout.getVisibility() != View.VISIBLE)
                    Rounded_Layout.setVisibility(View.VISIBLE);
                if (CardView_Bottom.getVisibility() != View.VISIBLE)
                    CardView_Bottom.setVisibility(View.VISIBLE);
                check_edit = false;
                if (txt.isEmpty()) return;
                tMapView.setZoomLevel(14);
                destinationsClass.clear(); // 현재까지 제공됬던 리사이클러뷰의 모든 리스트 삭제하는 것
                pictureIndex = 0;
                naverlocation.getLocationData(handlerPushMessage, txt); // 네이버로 검색
            }
        });

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
        destinationsClass.execute(this, getApplicationContext(), handlerPushMessage, gestureDetector);


        // 큐가 있어 보통 메인큐 99.9999 메인큐 492
        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() { // tmapView 클릭하면 발생하는 이벤트를 잡아준다
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                // 지도를 클릭했을 때

                for (int i = 0; i < arrayList.size(); i++) { // arrayList 안에 클릭된 마커가 들어온다.
                    int position = Integer.parseInt(arrayList.get(i).getID());
                    destinationsClass.toPosition(position);
                    double x = arrayList.get(i).longitude;
                    double y = arrayList.get(i).latitude;

                    Message msg = Message.obtain();
                    msg.obj = arrayList.get(i);
                    msg.what = 3;
                    handlerPushMessage.sendMessage(msg);


                    // 리사이클러뷰랑 예랑 일치시켜야되는거아님?
                    // 마커 눌럿는데 어떤 리사이클러뷰? 어떻게 해야될까? 형 어떻게 해야할까요?
                }
                return true;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return true;
            }
        });


/*
        NavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if(id == R.id.Nav_Home){

                }
                else if(id == R.id.Nav_Map){
                    FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                    // 프레그먼트 클랙스 하나 생성하고
                    FragmentMap fragment2 = new FragmentMap();
                    transaction.replace(R.id.f,fragment2);
                    transaction.commit();
                }
                else if(id == R.id.Nav_Cource){

                }
                else if(id == R.id.Nav_Survey){

                }
                return true;
            }
        });
    }
*/

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

