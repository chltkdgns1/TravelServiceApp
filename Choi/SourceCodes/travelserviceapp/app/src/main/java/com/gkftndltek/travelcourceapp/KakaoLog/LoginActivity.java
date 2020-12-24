package com.gkftndltek.travelcourceapp.KakaoLog;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.RealHome.HomeActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback callback;
    // private RequestClass requestClass;
  //  private TokenClass tokenClass;


    private LinearLayout LinearLayout_Google_Button, LinearLayout_Logout;

    // 데이터 베이스
    private FirebaseDatabase database;
    private DatabaseReference users, logined,signuped;

    // Alert
    private AlertDialog.Builder alt_bld;
    private EditText et;
    private AlertDialog alert;
    private String nickName = "";
    private boolean okCheck = false;

    // 로그아웃체크

    private String devicetoken;

    private LoginButton Button_Login_Real;
    private LinearLayout LinearLayout_KakaoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);//



        if(!Session.getCurrentSession().checkAndImplicitOpen()){
            setContentView(R.layout.login_layout);

            Button_Login_Real = findViewById(R.id.Button_Login_Real);

            LinearLayout_KakaoLogin = findViewById(R.id.LinearLayout_KakaoLogin);
            LinearLayout_KakaoLogin.setClickable(true);
            LinearLayout_KakaoLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button_Login_Real.performClick();
                }
            });

        }
    }

    private void init(){
        database = FirebaseDatabase.getInstance();
        users = database.getReference();
        signuped = database.getReference("signuped");
//        logined = database.getReference("logined");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() { // 로그인 성공했을 때

            AuthService.getInstance()
                    .requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "토큰 정보 요청 실패: " + errorResult);
                        }

                        @Override
                        public void onSuccess(AccessTokenInfoResponse result) { // 로그인이 승인되었을 경우

                       //     Log.i("KAKAO_API", "사용자 아이디: " + result.getUserId());
                       //     Log.i("KAKAO_API", "남은 시간(s): " + result.getExpiresInMillis());

                            final String token = Long.toString(result.getUserId()); // 카카오톡에서 임의로 준 토큰 값

                            users.child("ban").child(token).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            if(token != null) { // 해당 토큰값이 널이 아닐 경우에

                                signuped.child(token).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final UserData w = dataSnapshot.getValue(UserData.class);

                                        if (w != null) { // 회원가입을 한 경우
                                            String nick = w.getNickName();
                                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                            intent.putExtra("nick", nick);
                                            intent.putExtra("token", token);
                                            startActivity(intent);//액티비티
                                            finish();
                                        }
                                        else {  // 회원 가입을 해야하는 경우
                                            Intent intent = new Intent(getApplicationContext(), LoginDataClass.class); // 닉네임을 설정한다.
                                            intent.putExtra("token", token);
                                            startActivity(intent);//액티비티 띄우
                                            finish();
                                        }
                                        return;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            else{
//                                System.out.println("널이라고 이게 말이되냐? : " + token);
                            }
                        }
                    });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) { // 실패
//            System.out.println("222222222");
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }
}