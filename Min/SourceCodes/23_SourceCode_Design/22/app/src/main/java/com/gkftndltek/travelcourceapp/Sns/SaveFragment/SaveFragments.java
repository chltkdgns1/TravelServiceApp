package com.gkftndltek.travelcourceapp.Sns.SaveFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.BitmapPostData;
import com.gkftndltek.travelcourceapp.Sns.MainActivity;
import com.gkftndltek.travelcourceapp.Sns.PostRecycle.PostData;
import com.gkftndltek.travelcourceapp.Sns.PostRecycle.SaveRecyclerClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SaveFragments extends Fragment {

    private View rootView;
    private Context con;


    private ImageView ImageView_Empty;

    private SaveRecyclerClass saveRecyclerClasss;

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
        rootView = inflater.inflate(R.layout.fragment_sns_save, container, false);
        // 리니어레이아웃

        init();
        return rootView;
    }

    void init() {
        con = getActivity();

        ImageView_Empty = rootView.findViewById(R.id.ImageView_Empty);
        ImageView_Empty.setVisibility(View.GONE);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://travelcourceapp.appspot.com");
        token = ((MainActivity) con).token;

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();

        saveRecyclerClasss= new SaveRecyclerClass();
        saveRecyclerClasss.execute(rootView,con,handlerPushMessage);

        fref.child("save").child(token).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getChildrenCount() == 0){
                    ImageView_Empty.setVisibility(View.VISIBLE);
                    saveRecyclerClasss.recyclerViewGone();
                    return;
                }

                ImageView_Empty.setVisibility(View.GONE);
                saveRecyclerClasss.recyclerViewVisible();


                for(DataSnapshot snap : snapshot.getChildren()){

                    final String key = snap.getKey();

                    fref.child("post").child(token).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            synchronized (this) {
                                PostData data = dataSnapshot.getValue(PostData.class);
                                final BitmapPostData bdata = new BitmapPostData();

                                bdata.setNickname(data.getNickname());
                                bdata.setDescription(data.getDescription());
                                bdata.setLike(data.getLike());
                                bdata.setTimes(data.getTimes());
                                bdata.setNowNick(token);
                                bdata.setPath(key);

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
                                                    System.out.println("여기 잘 들어옵니까???");
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                    bdata.setImage(bitmap);
                                                    if(bdata.getImageSize() == sz)
                                                        saveRecyclerClasss.add(bdata);
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
                                else saveRecyclerClasss.add(bdata);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // ((MainActivity)context).setOnBackPressedListener(this); // 프레그먼트
    }
}