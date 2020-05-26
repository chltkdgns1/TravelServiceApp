package com.alsdnjsrl.RecyclerDBDBDB;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostUploadActivity extends AppCompatActivity {
    private EditText EditText_username,EditText_title, EditText_contents;
    private Button Button_check,Button_image,Button_video;
    private ImageView ImageView_upload;
    private Uri filePath;
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postupload);

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
                final String Title = EditText_title.getText().toString();
                final  String Contents = EditText_contents.getText().toString();
                final  String Username = EditText_username.getText().toString();
                if(Title.equals("")){
                    Toast.makeText(getApplicationContext(), "제목을 입력하시오.", Toast.LENGTH_LONG).show();
                }
                else if(Contents.equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 입력하시오.", Toast.LENGTH_LONG).show();
                }
                else if(Username.equals("")){
                    Toast.makeText(getApplicationContext(), "작성자를 입력하시오.", Toast.LENGTH_LONG).show();
                }
                else if(!flag){
                    Intent intent = new Intent(PostUploadActivity.this, PostViewActivity.class);
                    intent.putExtra("Username", Username);
                    intent.putExtra("Title", Title);
                    intent.putExtra("Contents", Contents);
                    startActivity(intent);
                }
                else if(flag){
                    uploadFile();
                }
            }
        });

        //사진 버튼을 눌렀을때,
        Button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 0);
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);//문제아님
            }
        });
        //동영상 버튼을 눌렀을때,
        Button_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
                startActivityForResult(intent, 1);


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 500 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri selectedImageUri = data.getData();
//            ImageView_upload.setImageURI(selectedImageUri);
//        }
        //사진 올리기
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d("PostUploadActivity", "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ImageView_upload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //동영상 올리기
        else if(requestCode == 1 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d("PostUploadActivity", "uri:" + String.valueOf(filePath));
            Uri bitmap = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            ImageView_upload.setImageURI(bitmap);
        }
    }
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();
            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final String Title = EditText_title.getText().toString();
            final  String Contents = EditText_contents.getText().toString();
            final  String Username = EditText_username.getText().toString();
            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            final String Profile = formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://mysnsproject-85886.appspot.com").child(Profile);
            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostUploadActivity.this, PostViewActivity.class);
                            intent.putExtra("Username", Username);
                            intent.putExtra("Title", Title);
                            intent.putExtra("Contents", Contents);
                            intent.putExtra("Profile", filePath.toString());
                            startActivity(intent);
                            System.out.println("잘넘어가니? 이녀석아!?!?!!?!");
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
