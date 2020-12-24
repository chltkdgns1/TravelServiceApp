package com.gkftndltek.travelcourceapp.Sns.MeFragment;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.KakaoLog.UserData;
import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.RealHome.HomeActivity;
import com.gkftndltek.travelcourceapp.Sns.MainActivity;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.Follow.FollowFragment;

import com.gkftndltek.travelcourceapp.Sns.MeFragment.FollowWritePrint.Recycler.FollowWritePrintFragment;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.Followed.FollowedFragment;
import com.gkftndltek.travelcourceapp.Sns.Options.OptionActivity;
import com.gkftndltek.travelcourceapp.Sns.UploadPost.PostUploadActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.usermgmt.response.model.User;


public class MeFragments extends Fragment {

    public View rootView;
    public Context con;

    // 뷰

    private TextView TextView_Me_Posts,TextView_Me_Name,TextView_Followers,TextView_following;
    private ImageView ImageView_Me_Options,CircleImageView_Profile;

    private FirebaseDatabase database;
    private DatabaseReference fref;

    private ImageButton ImageButton_Photos,ImageButton_My_Post,ImageButton_Followed,ImageButton_Follow;

    // 프레그먼트

    private FollowFragment followFragment;
    private FollowWritePrintFragment followWritePrintFragment;
    private FollowedFragment followedFragment;
    public String token,nick;


    private int check = 1;

    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sns_me, container, false);
        // 리니어레이아웃

        init();
        return rootView;
    }

    void init() {
        con = getActivity();

        followFragment = new FollowFragment();
        followWritePrintFragment = new FollowWritePrintFragment();
        followedFragment= new FollowedFragment();

        ImageButton_Photos = rootView.findViewById(R.id.ImageButton_Photos);
        ImageButton_My_Post = rootView.findViewById(R.id.ImageButton_My_Post);
        ImageButton_Followed = rootView.findViewById(R.id.ImageButton_Followed);
        ImageButton_Follow = rootView.findViewById(R.id.ImageButton_Follow);

        CircleImageView_Profile = rootView.findViewById(R.id.CircleImageView_Profile);

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();

        token = ((MainActivity) con).token;
        nick = ((MainActivity) con).nick;

        TextView_Me_Posts = rootView.findViewById(R.id.TextView_Me_Posts);
        TextView_Me_Name = rootView.findViewById(R.id.TextView_Me_Name);
        TextView_Followers = rootView.findViewById(R.id.TextView_Followers);
        TextView_following= rootView.findViewById(R.id.TextView_following);
        ImageView_Me_Options = rootView.findViewById(R.id.ImageView_Me_Options);


        fref.child("post").child(token).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView_Me_Posts.setText(Long.toString(dataSnapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fref.child("users").child(token).child("followed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView_following.setText(Long.toString(dataSnapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fref.child("users").child(token).child("follower").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView_Followers.setText(Long.toString(dataSnapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fref.child("users").child(token).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData data = dataSnapshot.getValue(UserData.class);
                int resId = getResources().getIdentifier(data.getImageName(),"drawable",((MainActivity)getActivity()).getPackageName());
                CircleImageView_Profile.setImageResource(resId);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        //TextView_Me_Name.setText("token");
        TextView_Me_Name.setText(nick);


        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FragmentLayout_Four, followWritePrintFragment).commit();

        ImageButton_Photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check == 1) return ;
                check = 1;

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FragmentLayout_Four, followWritePrintFragment).commit();
            }
        });

        ImageButton_My_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check == 2) return ;
                check = 2;
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FragmentLayout_Four, followFragment).commit();
            }
        });

        ImageButton_Followed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check == 3) return ;
                check = 3;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FragmentLayout_Four, followedFragment).commit();
            }
        });

        ImageButton_Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check == 4) return ;
                check = 4;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FragmentLayout_Four, followFragment).commit();
            }
        });

//        ImageView_Me.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.Sns_Main, meFragments).commit();
//            }
//        });

        ImageView_Me_Options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(((MainActivity)getActivity()), OptionActivity.class);
                startActivity(intent);
                // 친구찾기
                // 팔로워보기
                //
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // ((MainActivity)context).setOnBackPressedListener(this); // 프레그먼트
    }
}
