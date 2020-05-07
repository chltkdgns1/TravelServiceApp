package com.alsdnjsrl.login10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    TextInputEditText TextInputEditText_email, TextInputEditText_password;
    RelativeLayout RelativeLayout_Login;

    String emailOK = "123@gmail.com";
    String passwordOK = "1234";

    String inputEmail = "";
    String inputPassword = "";


//    class Data {
//        String []EmailData = new String[100];
//        String []PassData = new String[100];
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputEditText_email = findViewById(R.id.TextInputEditText_email);
        TextInputEditText_password = findViewById(R.id.TextInputEditText_password);
        RelativeLayout_Login = findViewById(R.id.RelativeLayout_Login);


//        Data data;
        //1번째째
       //이름과 변수명을 동일하게 하면 헷갈리지가 않음.
        //1. 값을 가져온다.
        //2. 클릭을 감지한다.
        //3. 1번의 값을 다음 엑티비티로 넘긴다.

        //2번째
        //1. 값을 가져온다. (검사하기) (123@gmail.com / 1234 일 경우에만 로그인)
        //2. 클릭을 감지한다.
        //3. 1번의 값을 다음 엑티비티로 넘긴다.
        RelativeLayout_Login.setClickable(false);
        TextInputEditText_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null) {
                    Log.d("SENTI", s.toString());
                    inputEmail = s.toString();
                    RelativeLayout_Login.setEnabled(validation());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        TextInputEditText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s != null) {
                    Log.d("SENTI", s.toString());
                    inputPassword = s.toString();
                    RelativeLayout_Login.setEnabled(validation());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        RelativeLayout_Login.setClickable(true);
        RelativeLayout_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = TextInputEditText_email.getText().toString();
                String password = TextInputEditText_password.getText().toString();

                Intent intent = new Intent(MainActivity.this, LoginResultActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                startActivity(intent);
                //위 4줄로 받아온값을 들고 LoginResultActivity로 간다.

            }
        });




        TextView text = findViewById(R.id.TextView_signup);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = TextInputEditText_email.getText().toString();
                String password = TextInputEditText_password.getText().toString();
                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

    }


    public boolean validation(){
        return inputEmail.equals(emailOK) && inputPassword.equals(passwordOK);
    }
}
