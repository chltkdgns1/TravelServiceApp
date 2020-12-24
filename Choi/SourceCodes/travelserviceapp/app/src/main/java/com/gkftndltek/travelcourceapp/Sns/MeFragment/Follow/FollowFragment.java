package com.gkftndltek.travelcourceapp.Sns.MeFragment.Follow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.MainActivity;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.Follow.Recycler.FriendRecyclerClass;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.Follow.Recycler.FriendsDataClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.AppFriendOrder;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FollowFragment extends Fragment {

    private FriendRecyclerClass friendRecyclerClass;

    private View rootView;
    private Context con;
    // 닉네임 토큰

    private String nick,token;

    private FirebaseDatabase database;
    private DatabaseReference fref;

    private String [] imageName = {"image01","image02","image03","image04","image05","image06","image07","image08","image09","image10"};

    // 핸들러
    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                FriendsDataClass data = (FriendsDataClass)msg.obj;
                friendRecyclerClass.add(data);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sns_follow_print, container, false);
        // 리니어레이아웃

        init();
        return rootView;
    }

    void init(){

        con = getActivity();
        token = ((MainActivity) con).token;

        friendRecyclerClass = new FriendRecyclerClass();
        friendRecyclerClass.execute(getActivity(),rootView,con,handlerPushMessage,token);

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();

        fref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getKey().equals(token)) continue;

                    int randomNum = (int) (Math.random() * 10);
                    int resId = getResources().getIdentifier("@drawable/"+imageName[randomNum],"drawable",((MainActivity)getActivity()).getPackageName());

                    FriendsDataClass fdata = new FriendsDataClass();
                    fdata.setUsername(data.child("nickName").getValue(String.class));
                    fdata.setUid(data.getKey());
                    fdata.setImageName(resId);

                    Message msg = Message.obtain();

                    msg.obj = fdata;
                    msg.what = 1;
                    handlerPushMessage.sendMessage(msg);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        AppFriendContext context =
                new AppFriendContext(AppFriendOrder.NICKNAME, 0, 100, "asc");

        KakaoTalkService.getInstance()
                .requestAppFriends(context, new TalkResponseCallback<AppFriendsResponse>() {
                    @Override
                    public void onNotKakaoTalkUser() {
                        Log.e("KAKAO_API", "카카오톡 사용자가 아님");
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "친구 조회 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(AppFriendsResponse result) {
                        Log.i("KAKAO_API", "친구 조회 성공");
                        System.out.println("사이즈좀 보여주세요 : " + result.getFriends().size()) ;
                        for (AppFriendInfo friend : result.getFriends()) {
                            final AppFriendInfo f = friend;
                            new Thread() {
                                @Override
                                public void run() {
                                    Log.d("KAKAO_API", f.toString());

                                    FriendsDataClass data = new FriendsDataClass();
                                    data.setUid(Long.toString(f.getId()));
                                    data.setUsername(f.getProfileNickname());

                                    Bitmap tbit = getImageBitmap(f.getProfileThumbnailImage());

                                    if(tbit == null){
                                        System.out.println("여기 안들어오냐?");
                                        int randomNum = (int) (Math.random() * 10);
                                        int resId = getResources().getIdentifier("@drawable/"+imageName[randomNum],"drawable",((MainActivity)getActivity()).getPackageName());
                                        data.setImageName(resId);
                                    }

                                    else data.setImageUrl(getImageBitmap(f.getProfileThumbnailImage()));
                                    data.setUuid(f.getUUID());
                                    Message msg = Message.obtain();

                                    msg.obj = data;
                                    msg.what = 1;
                                    handlerPushMessage.sendMessage(msg);
                                }
                            }.start();
                        }
                    }
                });
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

