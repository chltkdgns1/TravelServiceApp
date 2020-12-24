package com.gkftndltek.travelcourceapp.Sns.MeFragment.FollowWritePrint.Recycler;

import android.content.Context;
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
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.MainActivity;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.FollowWritePrint.Recycler.Recycler.PhotosClass;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.FollowWritePrint.Recycler.Recycler.PhotosData;
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

public class FollowWritePrintFragment extends Fragment {


    private View rootView;
    private Context con;

    // 닉네임 토큰
    private String nick, token;


    private PhotosClass photosClass;

    private FirebaseDatabase database;
    private DatabaseReference fref;
    private FirebaseStorage storage;
    private StorageReference storageRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sns_follow_write_print, container, false);
        // 리니어레이아웃

        init();
        return rootView;
    }

    void init() {
        con = getActivity();

        photosClass = new PhotosClass();
        photosClass.execute(rootView, con);

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://travelcourceapp.appspot.com");
        token = ((MainActivity) con).token;

        fref.child("post").child(token).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                TextView_Me_Posts.setText(Long.toString(snapshot.getChildrenCount()));

                for (DataSnapshot snap : snapshot.getChildren()) {
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

                                        final long ONE_MEGABYTE = 2048 * 2048;

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
                                } else {
                                    Drawable drawable = getResources().getDrawable(R.drawable.tg);
                                    photosData.setData(((BitmapDrawable) drawable).getBitmap());
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


    }
}
