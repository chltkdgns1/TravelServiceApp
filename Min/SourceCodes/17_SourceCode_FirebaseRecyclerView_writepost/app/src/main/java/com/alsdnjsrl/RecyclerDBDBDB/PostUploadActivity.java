package com.alsdnjsrl.RecyclerDBDBDB;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PostUploadActivity extends AppCompatActivity {
    EditText EditText_username,EditText_title, EditText_contents;
    Button Button_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postupload);

        EditText_username = findViewById(R.id.EditText_username);
        EditText_title = findViewById(R.id.EditText_title);
        EditText_contents = findViewById(R.id.EditText_contents);
        Button_check = findViewById(R.id.Button_check);



        //확인 버튼을 눌렀을 때,
        Button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = EditText_title.getText().toString();
                String Contents = EditText_contents.getText().toString();
                String Username = EditText_username.getText().toString(); /*int형*/
                Intent intent = new Intent(PostUploadActivity.this, PostViewActivity.class);
                intent.putExtra("Username",Username);
                intent.putExtra("Title",Title);
                intent.putExtra("Contents",Contents);
                startActivity(intent);
            }
        });
    }
}
