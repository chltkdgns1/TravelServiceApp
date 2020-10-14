package com.gkftndltek.travelcourceapp.Sns.UploadPost;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.PostViewPagerAdapter;
import com.gkftndltek.travelcourceapp.Sns.UriStrContainer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostUploadActivity extends AppCompatActivity {

    // 뷰
    private TextView TextView_Make_Post;
    private EditText EditText_Post_Description,EditText_Post_Tag;

    private ViewPager ViewPager_Get_Post;
    private PostViewPagerAdapter adapter;

    //private Uri filePathImage = null, filePathVideo = null;
    private List<Uri> getImages;

    private UriStrContainer uriStrContainer;

    //private String fpImage = null , fpVideo = null;

    private ProgressDialog progressDialog;

    private FirebaseStorage storage;
    private SimpleDateFormat formatter;
    private StorageReference storageRef;

    private String description,tag,token;
    private final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getImages = new ArrayList<>();
        Intent it = getIntent();
        Bundle bun = it.getExtras();
        token = bun.getString("token");

        requestReadExternalStoragePermission();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),  0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_post);

        init();
    }


    void init(){
        ViewPager_Get_Post = findViewById(R.id.ViewPager_Get_Post);
        ViewPager_Get_Post.setClipToPadding(false);
        adapter = new PostViewPagerAdapter(this);

        EditText_Post_Tag = findViewById(R.id.EditText_Post_Tag);
        EditText_Post_Description= findViewById(R.id.EditText_Post_Description);

        TextView_Make_Post = findViewById(R.id.TextView_Make_Post);
        TextView_Make_Post.setClickable(true);

        uriStrContainer = new UriStrContainer();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://travelcourceapp.appspot.com");

        TextView_Make_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description =  EditText_Post_Description.getText().toString();
                tag = EditText_Post_Tag.getText().toString();
                uploadFile();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if((progressDialog != null) && progressDialog.isShowing() ){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK) {
            getImages.clear();
            ClipData cdata = data.getClipData();
            if(cdata == null){
                getImages.add(data.getData());
            }
            else{
                for(int i=0;i<cdata.getItemCount();i++)
                    getImages.add(cdata.getItemAt(i).getUri());
            }
        }

        adapter.clear();
        int sz = getImages.size();

        System.out.println("사진 사이즈가 어떻게 되나요? : " + sz);
        for(int i=0;i<sz;i++){
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), getImages.get(i));
                adapter.add(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        ViewPager_Get_Post.setAdapter(adapter);
      //  else if(requestCode == 1 && resultCode == RESULT_OK)
      //      filePathVideo = data.getData();
    }

    private void uploadFile() {
        //업로드할 파일이 있으면 수행

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("업로드중...");
        progressDialog.show();

        final Intent intent = new Intent();

        intent.putExtra("token",token);
        intent.putExtra("description",description);
        intent.putExtra("tag",tag);

        setResult(1,intent);

        if(!getImages.isEmpty()){

            System.out.println("몇장가져옵니까? " + getImages.size());

            for(int i = 0;i<getImages.size();i++){

                synchronized (this) {
                    formatter = new SimpleDateFormat("yyyyMMHH_mmss");
                    Date now = new Date();
                    final String Profile = formatter.format(now) + i + ".png";
                    uriStrContainer.push(Profile);

                    final int idxData = i;

                    storageRef.child(Profile).putFile(getImages.get(i))
                            //성공시
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    if (idxData == getImages.size() - 1) {
                                        intent.putExtra("UriStr", uriStrContainer);
                                        Toast.makeText(getApplicationContext(), "업로드 성공!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    return;
                                }
                            })
                            //실패시
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    return;
                                }
                            })
                            //진행중
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                }
                            });
                }
            }

        }
        else finish();
    }

    private void requestReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}