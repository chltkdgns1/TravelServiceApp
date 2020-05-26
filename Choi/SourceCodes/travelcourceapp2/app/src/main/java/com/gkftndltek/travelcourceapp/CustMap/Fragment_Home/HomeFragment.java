package com.gkftndltek.travelcourceapp.CustMap.Fragment_Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.CustMap.Fragment_PersonalMap.NoneBitmapDestData;
import com.gkftndltek.travelcourceapp.CustMap.MainActivity;
import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationDataClass;
import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationsClass;
import com.gkftndltek.travelcourceapp.DrawerLayoutClass;
import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.RoundedLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class HomeFragment extends Fragment implements MainActivity.OnBackPressedListeners {
    private String key = "l7xx713d4db3b29b418dba74f8af6162f4fb";
    // key 자기 key 바꿔주면 되요 tmap api 키

    private TMapView tMapView; // 티맵 뷰
    private DrawerLayoutClass drawerLayoutClass; // 제 코드에서 사용하고 있지 않음
    private EditText EditText_Search; // 제 맵 검색

    //private DrawerLayout drawerLayout; // 드로워 레이아웃

    private ImageView ImageView_Menu, ImageView_Confirm, ImageView_Up; // 메뉴 . 검색 완료 , 왼쪽 하단
    private CardView CardView_Bottom; // 카드뷰
    private LinearLayout linearLayoutTmap, LinearLayout_Bottom, LinearLayout_Destination; // 티맵 , 바텀 카드 뷰 감싸고 있는 레이아웃 ( 아닐수잇음..)
    private Boolean check_edit = false; // 검색창을 눌렀는지 안눌렀는지
    private TMapData tmapdata; // 티맵 데이터
    private RoundedLayout Rounded_Layout; // 레이아웃의 형태를 바꿔줘요
    private LinearLayout.LayoutParams Rounded_Layout_param, CardView_Bottom_param, LinearLayout_Bottom_param, LinearLayout_Destination_param;
    // 레이아웃의 속성바꿀 수 있음
    private Boolean updown = true, start_check = false; // 업다운

    private DestinationsClass destinationsClass; // 리사이클러뷰 할때 중간과정 거처가는 클래스

    private List<DestinationDataClass> data;   // 사용하고 있지않음
    private List<naverSearchLocationData> locationData; // 미투 업데이트하면 주석처리함
    private naverLocationSearch naverlocation; // 네이버 로케이션 네이버 api 검색하면 핸드러로 받아와요 정보를
    private InputMethodManager imm;
    private GestureDetector gestureDetector;
    private Boolean outCheck = false;

    private Context con ;
    private View rootView;
    AppCompatDialog progressDialog = null;

    private Activity act;

    // 핸들러

    private DestinationDataClass destData = null;

    // 메인 액티비티



    // 데이터 베이스

    private NoneBitmapDestData noneBitmapDestData = null; // 비트맵 때문에 firebase에 안올라감 ..
    private FirebaseDatabase database;
    private DatabaseReference users,logined;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    // 마커 drawble

    //private Geocoder geocoder ; 지오코더

    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { // 네이버 검색했다
                synchronized (this) {
                final naverSearchLocationData locationNaverData = (naverSearchLocationData) msg.obj;

                    tmapdata.findAllPOI(locationNaverData.getAddress(), new TMapData.FindAllPOIListenerCallback() { // tmap 명칭통합검색
                        // 네이버 검색으로 가져온 주소
                        @Override
                        public void onFindAllPOI(ArrayList poiItem) {
                            TMapPOIItem item = (TMapPOIItem) poiItem.get(0); // 구글로 바꾸고
                            String xypos = item.getPOIPoint().toString();
                            String[] xyData = xypos.split(" ");
                            double x = Double.parseDouble(xyData[1]), y = Float.parseFloat(xyData[3]); // 위도 경도

                            DestinationDataClass dstData = new DestinationDataClass();
                            dstData.setDrable((((MainActivity)act)).picture[locationNaverData.getIndex()]);
                            dstData.setX(x);
                            dstData.setY(y);
                            dstData.setPhone(locationNaverData.getTel());
                            dstData.setName(locationNaverData.getTitle());
                            dstData.setAddress(locationNaverData.getAddress());
                            dstData.setCategory(locationNaverData.getCategory());
                            dstData.setDescription(locationNaverData.getDescription());
                            dstData.setRoadAddress(locationNaverData.getRoadAddress());
                            dstData.setLink(locationNaverData.getLink());
                            dstData.setIndex(locationNaverData.getIndex());
                            dstData.setUrl(locationNaverData.getUrl());
                            destinationsClass.add(dstData); // 리사이클러뷰

                            TMapMarkerItem marker = new TMapMarkerItem();
                            TMapPoint tMapPoint1 = new TMapPoint(x, y);
                            marker.setIcon((((MainActivity)act)).bitmap[locationNaverData.getIndex()]);
                            marker.setTMapPoint(tMapPoint1);

                            tMapView.addMarkerItem(Integer.toString(locationNaverData.getIndex()), marker);

                            if(locationNaverData.getIndex() == 0){
                                tMapView.setZoomLevel(14);
                                tMapView.setCenterPoint(dstData.getY(),dstData.getX());
                            }

                            if(locationNaverData.getEndPoint() == true){ // 네이버에서 찾은 검색 결과의 마지막번째 인지 확인
                                outCheck = true;
                            }
                        }
                    });
                }
            }
            else if (msg.what == 2) {
                synchronized (this) {
                    DestinationDataClass data = (DestinationDataClass) msg.obj;
                    noneBitmapDestData = new NoneBitmapDestData();
                    noneBitmapDestData.setIndex(data.getIndex());
                    noneBitmapDestData.setX(data.getX());
                    noneBitmapDestData.setPhone(data.getPhone());
                    noneBitmapDestData.setRoadAddress(data.getRoadAddress());
                    noneBitmapDestData.setAddress(data.getAddress());
                    noneBitmapDestData.setCategory(data.getCategory());
                    noneBitmapDestData.setDescription(data.getDescription());
                    noneBitmapDestData.setDrable(data.getDrable());
                    noneBitmapDestData.setName(data.getName());
                    noneBitmapDestData.setUrl(data.getUrl());
                    noneBitmapDestData.setY(data.getY());
                    tMapView.setCenterPoint(data.getY(), data.getX(), true);
                }

            }

            else if (msg.what == 3) {
                synchronized (this) {
                    TMapMarkerItem item = (TMapMarkerItem) msg.obj;
                    tMapView.setCenterPoint(item.longitude, item.latitude, true);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment_layout, container, false);
        // 리니어레이아웃

        init();

        tMapView.setSKTMapApiKey(key);
        linearLayoutTmap.addView(tMapView);

        return rootView;
    }

    void init() {

        //System.out.println("Eqweqwqeqeq");
        // drawerLayoutClass = new DrawerLayoutClass(); // Drawer
        // drawerLayoutClass.init(this); // Drawer 초기화

        // 프레그먼트도 액티비티로 존재한다.
       // storage = FirebaseStorage.getInstance("gs://travelcourceapp.appspot.com/");
     //   storageRef = storage.getReference();

        act = (MainActivity)getActivity();
        database = FirebaseDatabase.getInstance();

        users = database.getReference("users");
        logined = database.getReference("logined");


        con = getActivity(); // 현재 프레그먼트에 액티비티를 가져오는 것이다. context 액티비티의 전유물이고
        // context 를 반환한다.

        gestureDetector = new GestureDetector(con, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }
            @Override
            public boolean onSingleTapUp(MotionEvent e) { // 한번 눌렀을 때

                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) { // 길게 눌렀을 때
                AlertDialog.Builder alert_ex = new AlertDialog.Builder(con);
                alert_ex.setMessage("경로를 장바구니에 넣으시겠습니까?");

                alert_ex.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("취소 눌렸냐? 개새이야");
                    }
                });

                alert_ex.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 버그 발생할 수 있음.
                        if(noneBitmapDestData != null) {
                            String token = (((MainActivity)act)).token;
                            String name = (((MainActivity)act)).mapName;
                            System.out.println("토큰 출력되냐? : " + token);
                            System.out.println("토큰 출력되냐고?"  +  name);
                            users.child(token).child("maps").child(name).child("basket").push().setValue(noneBitmapDestData);
                            noneBitmapDestData  = null;
                        }
                        System.out.println("확인눌렸냐?");
                    }
                });

                alert_ex.setTitle("Notice");
                AlertDialog alert = alert_ex.create();
                alert.show();
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });


        imm = (InputMethodManager) con.getSystemService(INPUT_METHOD_SERVICE);

        naverlocation = new naverLocationSearch(); //찾아지는거 확인



        destinationsClass = new DestinationsClass();
        destinationsClass.execute(rootView, con.getApplicationContext(),handlerPushMessage,gestureDetector);

        LinearLayout_Destination = rootView.findViewById(R.id.LinearLayout_Destination);
        LinearLayout_Destination.setVisibility(View.GONE);
        LinearLayout_Bottom = rootView.findViewById(R.id.LinearLayout_Bottom);
        // 그냥썻잖아요. 프레그먼트 내부에서 쓸 때는 inflator 로 받은 rootView 를 이용해서 사용한다.


        tmapdata = new TMapData();

        ImageView_Confirm = rootView.findViewById(R.id.ImageView_Confirm);
        ImageView_Confirm.setClickable(true);

        ImageView_Confirm.setOnClickListener(new View.OnClickListener() { // 우리가 검색할 주소 확인 버튼 클릭시 발생하는 클릭 이벤트
            @Override
            public void onClick(View v) {
                String txt = EditText_Search.getText().toString();
                EditText_Search.setText("");

                if(Rounded_Layout.getVisibility() != View.VISIBLE) Rounded_Layout.setVisibility(View.VISIBLE);
                if(CardView_Bottom.getVisibility() != View.VISIBLE) CardView_Bottom.setVisibility(View.VISIBLE);

                imm.hideSoftInputFromWindow(EditText_Search.getWindowToken(), 0);
                check_edit = false;
                if (txt.isEmpty()) return;

                tMapView.removeAllMarkerItem();
                destinationsClass.clear(); // 현재까지 제공됬던 리사이클러뷰의 모든 리스트 삭제하는 것
                System.out.println("여기가지전엥 터지는건가?");
                naverlocation.getLocationData(handlerPushMessage, txt); // 네이버로 검색



                tMapView.setZoomLevel(10);

            }
        });


        EditText_Search = rootView.findViewById(R.id.EditText_Search);
        EditTextTouch();

        CardView_Bottom = rootView.findViewById(R.id.CardView_Bottom);

       // drawerLayout = rootView.findViewById(R.id.drawer);

        ImageView_Menu = rootView.findViewById(R.id.ImageView_Menu);
        ImageView_Menu.setClickable(true);

        ImageView_Menu.setOnClickListener(new View.OnClickListener() { //  메뉴버튼 드러워아웃 나타남
            @Override
            public void onClick(View v) {
                (((MainActivity)act)).drawerLayout.openDrawer(Gravity.LEFT);
              //  drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        Rounded_Layout = rootView.findViewById(R.id.Rounded_Layout);
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

        ImageView_Up = rootView.findViewById(R.id.ImageView_Up);
        ImageView_Up.setClickable(true);

        ImageView_Up.setOnClickListener(new View.OnClickListener() { // 바텀에 있는 레이아웃 위로 올림
            @Override
            public void onClick(View v) {
                upErrorClear();
            }
        });

        linearLayoutTmap = (LinearLayout) rootView.findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(con);

        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() { // tmapView 클릭하면 발생하는 이벤트를 잡아준다
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                // 지도를 클릭했을 때
                System.out.println(arrayList.size());

                for(int i=0;i<arrayList.size();i++) { // arrayList 안에 클릭된 마커가 들어온다.
                    int position = Integer.parseInt(arrayList.get(i).getID());
                    destinationsClass.toPosition(position);

                    Message msg = Message.obtain();
                    msg.obj = arrayList.get(i);
                    msg.what = 3;
                    handlerPushMessage.sendMessage(msg);
                }
                return true;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return true;
            }
        });

    }

    void upErrorClear() {
        if (LinearLayout_Destination.getVisibility() == View.GONE)
            LinearLayout_Destination.setVisibility(View.VISIBLE);

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

    private void EditTextTouch() { // 텍스트 누르면 맵이 안보임
        EditText_Search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if( Rounded_Layout.getVisibility() == View.VISIBLE)
                        Rounded_Layout.setVisibility(View.GONE);
                    if( CardView_Bottom.getVisibility() == View.VISIBLE)
                        CardView_Bottom.setVisibility(View.GONE);
                    check_edit = true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBack() {

        // 메인액티비티에 접근
        // 한번 뒤로가기 버튼을 눌렀다면 Listener 를 null 로 해제해줍니다.
        //mBackListener = (MainActivity.OnBackPressedListener)((MainActivity) getActivity()).mBackListener;

        //if( mBackListener == null) System.out.println("날아오면서 null 됫네 슈벌;");

        DrawerLayout drawer = ((MainActivity)act).drawerLayout;

        // 프레그먼트는 감싸고 있는 액티비티의 UI 의 변경 시킬 수 있다.

        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
            check_edit = false;
        } else if (check_edit == true) {
            Rounded_Layout.setVisibility(View.VISIBLE);
            CardView_Bottom.setVisibility(View.VISIBLE);
            check_edit = false;
        }
        else if(updown == false){ // 뒤로 가기 클릭시 바텀 부분 내려감
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

        else {
            ((MainActivity)act).finish();
//            Intent intent = new Intent((MainActivity)act, MakeMapAct.class);
//            intent.putExtra("token", ((MainActivity)act).token);
//            intent.putExtra("name",((MainActivity)act).mapName);
//            startActivity(intent);
        }
    }

    // 프레그먼트가 실행이 되면 실행이 같이되

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressedListener(this); // 프레그먼트
    }

    // Fragment 호출 시 반드시 호출되는 오버라이드 메소드입니다.

    private void startProgress() {

        progressON("Loading...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressOFF();
            }
        }, 3500);
    }

    public void progressON(String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        }
        else {
            progressDialog = new AppCompatDialog(con);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.dialog_layout);
            progressDialog.show();
            onPause();
        }

        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();

        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }
    }

    public void progressSET(String message) {

        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }


        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }

    }

    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
