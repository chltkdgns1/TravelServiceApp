package com.gkftndltek.travelcourceapp.Sns.MeFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.MainActivity;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.Recycler.PhotosClass;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.Recycler.PhotosData;
import com.gkftndltek.travelcourceapp.Sns.Options.OptionActivity;
import com.gkftndltek.travelcourceapp.Sns.PostRecycle.PostData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MeFragments extends Fragment {

    private View rootView;
    private Context con;

    // 뷰

    private TextView TextView_Me_Posts,TextView_Me_Name,TextView_Followers,TextView_following;
    private ImageView ImageView_Me_Options;

    private PhotosClass photosClass;

    private FirebaseDatabase database;
    private DatabaseReference fref;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private String token;

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

        photosClass = new PhotosClass();
        photosClass .execute(rootView,con,handlerPushMessage);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://travelcourceapp.appspot.com");
        token = ((MainActivity) con).token;

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();


        TextView_Me_Posts = rootView.findViewById(R.id.TextView_Me_Posts);
        TextView_Me_Name = rootView.findViewById(R.id.TextView_Me_Name);
        TextView_Followers = rootView.findViewById(R.id.TextView_Followers);
        TextView_following= rootView.findViewById(R.id.TextView_following);
        ImageView_Me_Options = rootView.findViewById(R.id.ImageView_Me_Options);

        TextView_Followers.setText("0");
        TextView_following.setText("0");

        TextView_Me_Name.setText(token);

        fref.child("post").child(token).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                TextView_Me_Posts.setText(Long.toString(snapshot.getChildrenCount()));
                for(DataSnapshot snap : snapshot.getChildren()){
                    final String key = snap.getKey();

                    fref.child("post").child(token).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            synchronized (this) {
                                PostData data = dataSnapshot.getValue(PostData.class);
                                final PhotosData photosData = new PhotosData();

                                photosData.setPath(token + "," + key);

                                final int sz = (int) dataSnapshot.child("pictures").getChildrenCount();

                                if (sz != 0) {
                                    for (DataSnapshot pics : dataSnapshot.child("pictures").getChildren()) {
                                        String images = pics.getKey();

                                        images = images.replace('W', '.');

                                        final long ONE_MEGABYTE = 1024 * 1024;

                                        storageRef.child(images).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {
                                                synchronized (this) {
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                    photosData.setData(bitmap);
                                                    System.out.println("아니 시발 왜;;");
                                                    photosClass.add(photosData);
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle any errors
                                            }
                                        });

                                        break;
                                    }
                                }
                                else {
                                    Drawable drawable = getResources().getDrawable(R.drawable.tg);
                                    photosData.setData(((BitmapDrawable)drawable).getBitmap());
                                    System.out.println("아니 시발 왜!!");
                                    photosClass.add(photosData);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
