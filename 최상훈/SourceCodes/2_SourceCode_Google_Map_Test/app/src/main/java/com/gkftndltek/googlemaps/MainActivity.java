package com.gkftndltek.googlemaps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView Button_SetText; // 뷰들 선언
    private EditText TextInputEdit;
    private InputMethodManager im;
    private TextInputLayout TextInputEdit_Back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RoundedLayout rl = (RoundedLayout) findViewById(R.id.maplayout); // custom view
        // 커스텀 뷰의 경우에는 직접 만든 뷰 입니다. 제공해주는 뷰가 아닙니다.
        // 따라서 이 뷰에 대해서 알고 싶다면 RoundedLayout.class 를 참고하시면 됩니다.
        //rl.setCornerRadius(300);
        rl.setShapeCircle(true); // 구글맵의 모양을 둥글게 만들어줍니다.
        rl.showBorder(true);  // 경계선 생성
        rl.setBorderWidth(3);  // 경계선 두께

        im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); // 키보드창 on off
        Button_SetText = findViewById(R.id.Button_SetText); // 입력 버튼
        TextInputEdit = findViewById(R.id.TextInputEdit);   // 에딧텍스트
        TextInputEdit_Back = findViewById(R.id.TextInputEdit_Back); // 에딧텍스트를 둘러쌓고 있는 뷰

        Button_SetText.setVisibility(View.GONE); // 초기에는 에딧텍스트와 버튼은 보이지않음
        TextInputEdit.setVisibility(View.GONE);
        FragmentManager fragmentManager = getFragmentManager();

        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        cameraInit(map); // 초기 카메라  위치 잡아주면서 덤으로 서울하나 찍어줌

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() { // 맵 위에 마우스 클릭 이벤트 발생
            @Override
            public void onMapClick(LatLng latLng) {
                String text= latLng.latitude + " " + latLng.longitude;
                System.out.println(text);
                // 걍도 위도 출력

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.alpha(0.5f);
                markerOptions.title("내가클릭한곳");
                map.addMarker(markerOptions);
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.zoomTo(10));
                Button_SetText.setVisibility(View.GONE);
                TextInputEdit_Back.setVisibility(View.GONE);
                Button_SetText.setClickable(false);
                TextInputEdit.setVisibility(View.GONE);
                // 마커 추가
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) { //마커가 클릭되었을 경우에
                //.makeText(getApplicationContext(),marker.getTitle() + "\n" ,Toast.LENGTH_LONG).show();
                // 마커를 클릭했을때 토스트 메시지 출력

                marker.showInfoWindow();  // 마커 내용 출력
                Button_SetText.setVisibility(View.VISIBLE); // 각각의 뷰들 안보이던 걸 보이게 만들어줌
                Button_SetText.setClickable(true);
                TextInputEdit.setVisibility(View.VISIBLE);
                TextInputEdit_Back.setVisibility(View.VISIBLE);

                Button_SetText.setOnClickListener(new View.OnClickListener() { // 버튼 클릭시에 마커의 이름을 변경함
                    @Override
                    public void onClick(View v) {
                        String txt = TextInputEdit.getText().toString();
                        //Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG).show();
                        if(txt.isEmpty()) return;

                        marker.setTitle(txt);
                        marker.hideInfoWindow();
                        marker.showInfoWindow();

                        TextInputEdit.setText("");
                        im.hideSoftInputFromWindow(TextInputEdit.getWindowToken(),0);
                        Button_SetText.setVisibility(View.GONE);
                        TextInputEdit_Back.setVisibility(View.GONE);
                        Button_SetText.setClickable(false);
                        TextInputEdit.setVisibility(View.GONE);
                    }
                });
                return true;
            }
        });
    }

    public void cameraInit(final GoogleMap map){
        LatLng SEOUL = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

}
