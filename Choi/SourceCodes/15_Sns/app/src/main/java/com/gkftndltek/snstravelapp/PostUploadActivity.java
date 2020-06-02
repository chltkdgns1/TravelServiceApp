package com.gkftndltek.snstravelapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostUploadActivity extends AppCompatActivity {
    private EditText EditText_username,EditText_title, EditText_contents,EditText_Post_Tag;
    private Button Button_check,Button_image,Button_video;
    private ImageView ImageView_upload;

    private Uri filePathImage = null,filePathVideo = null;
    private List<Uri> getImages;

    private UriStrContainer uriStrContainer;

    private String fpImage = null , fpVideo = null;

    private ProgressDialog progressDialog;

    private String Title ;
    private String Contents ;
    private String Username;
    private String tag;

    private FirebaseStorage storage;
    private SimpleDateFormat formatter;
    private StorageReference storageRef;

    private final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postupload);

        init();
    }


    void init(){
        uriStrContainer = new UriStrContainer();
        getImages = new ArrayList<>();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://snsproject-defe7.appspot.com");

        EditText_Post_Tag = findViewById(R.id.EditText_Post_Tag);
        EditText_username = findViewById(R.id.EditText_username);
        EditText_title = findViewById(R.id.EditText_title);
        EditText_contents = findViewById(R.id.EditText_contents);
        Button_check = findViewById(R.id.Button_check);
        Button_image = findViewById(R.id.Button_image);
        Button_video = findViewById(R.id.Button_video);
        ImageView_upload = findViewById(R.id.ImageView_upload);

        //확인 버튼을 눌렀을 때,
        Button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Title = EditText_title.getText().toString();
                Contents = EditText_contents.getText().toString();
                Username = EditText_username.getText().toString();
                tag = EditText_Post_Tag.getText().toString();

                if(Title.isEmpty() || Contents.isEmpty() || Username.isEmpty()){
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                uploadFile();
            }
        });

        Button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestReadExternalStoragePermission();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                //사진을 여러개 선택할수 있도록 한다
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),  0);
            }
        });
        //동영상 버튼을 눌렀을때,
        Button_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestReadExternalStoragePermission();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
                startActivityForResult(intent, 1);
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
        else if(requestCode == 1 && resultCode == RESULT_OK)
            filePathVideo = data.getData();
    }

    private void uploadFile() {
        //업로드할 파일이 있으면 수행

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("업로드중...");
        progressDialog.show();

        final Intent intent = new Intent();

        intent.putExtra("Username",Username);
        intent.putExtra("Title",Title);
        intent.putExtra("Contents",Contents);
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
                                    if (filePathVideo == null && idxData == getImages.size() - 1) {
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

    /*
            if(filePathImage != null){
            Date now = new Date();
            final String Profile = formatter.format(now) + ".png";

            fpImage = Profile;

            if(fpImage != null) intent.putExtra("fpImage",fpImage);

            storageRef.child(Profile).putFile(filePathImage)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            if(filePathVideo == null) finish();
                            return;
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        }

        if(filePathVideo != null) {

            Date now = new Date();
            final String Profile = formatter.format(now) + ".mp4";

            fpVideo = Profile;

            if(fpVideo != null) intent.putExtra("fpVideo",fpVideo);

            storageRef.child(Profile).putFile(filePathVideo)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        }

        if(filePathVideo == null && filePathImage == null) finish();

     */
}