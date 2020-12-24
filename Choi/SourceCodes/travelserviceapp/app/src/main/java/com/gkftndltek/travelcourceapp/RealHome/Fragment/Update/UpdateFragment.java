package com.gkftndltek.travelcourceapp.RealHome.Fragment.Update;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.RealHome.HomeActivity;
import com.gkftndltek.travelcourceapp.RealHome.recycler.RecyclerClass;
import com.gkftndltek.travelcourceapp.Sns.BitmapPostData;
import com.gkftndltek.travelcourceapp.Sns.MainActivity;
import com.gkftndltek.travelcourceapp.Sns.PostRecycle.PostData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdateFragment extends Fragment {
    private Context con;
    private View rootView;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private RecyclerClass recyclerClass;

    private Activity act;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_home_fragment_all, container, false);
        init();
        return rootView;
    }

    private synchronized void init() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        con = getActivity();

        String nick = ((HomeActivity)getActivity()).nick;
        String token = ((HomeActivity)getActivity()).token;
        recyclerClass = new RecyclerClass();
        recyclerClass.execute(rootView,con,nick,token);


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://travelcourceapp.appspot.com");

        databaseReference.child("adminpost").child("관리자").child("update").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                PostData pdata = dataSnapshot.getValue(PostData.class);
                final BitmapPostData bdata = new BitmapPostData();
                int resId = getResources().getIdentifier("@drawable/manager","drawable",((HomeActivity)getActivity()).getPackageName());
                bdata.setImageData(resId);
                bdata.setNickname("관리자");
                bdata.setDescription(pdata.getDescription());
                bdata.setLike(pdata.getLike());
                bdata.setTimes(pdata.getTimes());
                bdata.setNowNick(((HomeActivity) getActivity()).token);
                bdata.setPath(pdata.getPrimaryKey());

                final int sz = (int) dataSnapshot.child("pictures").getChildrenCount();

                if (sz == 0) {
                    recyclerClass.add(bdata);
                    return;
                }

                for (DataSnapshot pics : dataSnapshot.child("pictures").getChildren()) {
                    String images = pics.getKey();
                    images = images.replace('W', '.');

                    final long ONE_MEGABYTE = 2048 * 2048;

                    storageRef.child(images).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            synchronized (recyclerClass) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                bdata.setImage(bitmap);
                                if (bdata.getImageSize() == sz) {
                                    System.out.println("Dsadadadadadsadsadasdas");
                                    recyclerClass.add(bdata);
                                    return;
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
