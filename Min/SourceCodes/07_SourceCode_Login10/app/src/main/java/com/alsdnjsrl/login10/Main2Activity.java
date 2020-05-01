
package com.alsdnjsrl.login10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Main2Activity extends AppCompatActivity {
    boolean flag ;
    int n = 0;
    String[] issameEmail = new String[100];
    String[] issameName = new String[100];
    String[] issamePassword = new String[100];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button button = (Button)findViewById(R.id.Button_SignUpComplete);
        final TextInputEditText EmailOrID = (TextInputEditText)findViewById(R.id.TextInputEditText_EmailOrID);
        final TextInputEditText Name = (TextInputEditText)findViewById(R.id.TextInputEditText_Name);
        final TextInputEditText Password = (TextInputEditText)findViewById(R.id.TextInputEditText_Password);
        //final 변하지않는다.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                String id = EmailOrID.getText().toString();
                String name= Name.getText().toString();
                String pass = Password.getText().toString();

                System.out.println("id : " + id);
                for(int i=0;i<n;i++) {
                    System.out.println(issameEmail[i]);
                    if (issameEmail[i].equals(id)) {
                        flag = true;
                        break;
                    }
                }

                if(flag)
                    Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다.", Toast.LENGTH_LONG).show();
                else {
                    issameEmail[n] = id;
                    issameName[n] = name;
                    issamePassword[n] = pass;
                    n++;
                }
            }
        });
    }
}