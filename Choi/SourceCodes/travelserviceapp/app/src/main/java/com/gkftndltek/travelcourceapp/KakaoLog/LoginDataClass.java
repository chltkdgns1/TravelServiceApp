package com.gkftndltek.travelcourceapp.KakaoLog;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.RealHome.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class LoginDataClass extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference dbAcess;

    private String token, email;
    private String emailData; // 랜덤으로 생성된 이메일 번호
    private Boolean emailOk = false;

    private String [] imageName = {"image01","image02","image03","image04","image05","image06","image07","image08","image09","image10"};

    // 뷰

    private EditText EditText_NickName, EditText_Email, EditText_EmailConfirm;
    private TextView TextView_EamilConfirm, TextView_EmailOk, TextView_Login_Complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_inputdata);

        init();

    }

    void init() {




        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


        Intent it = getIntent();
        Bundle bun = it.getExtras();
        token = (String) bun.get("token");

        database = FirebaseDatabase.getInstance();
        dbAcess = database.getReference();

        EditText_NickName = findViewById(R.id.EditText_NickName);
        EditText_Email = findViewById(R.id.EditText_Email);
        EditText_EmailConfirm = findViewById(R.id.EditText_EmailConfirm);
        TextView_EamilConfirm = findViewById(R.id.TextView_EamilConfirm);
        EditText_EmailConfirm.setVisibility(View.GONE);

        TextView_EmailOk = findViewById(R.id.TextView_EmailOk);
        TextView_EmailOk.setVisibility(View.GONE);

        TextView_Login_Complete = findViewById(R.id.TextView_Login_Complete);

        TextView_EmailOk.setClickable(true);
        TextView_Login_Complete.setClickable(true);
        TextView_EamilConfirm.setClickable(true);

        TextView_EamilConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s = EditText_Email.getText().toString();

                if (s.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText_EmailConfirm.setVisibility(View.VISIBLE);
                TextView_EmailOk.setVisibility(View.VISIBLE);

                final String ts = s;
                final String token = s.replace("@", " ").replace(".", " ");
                dbAcess.child("Email").child(token).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long cnt = dataSnapshot.getChildrenCount();
                        if (cnt != 0) {
                            // 중복된 이메일
                            Toast.makeText(getApplicationContext(), "이미 등록된 이메일입니다. 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                            return;
                        } else {

                            // 이메일 보내는 소스코드
                            try {
                                GMailSender gMailSender = new GMailSender("alsdnjsrl223@gmail.com", "rhdqn6519!!@@");
                                emailData = gMailSender.getEmailCode();
                                gMailSender.sendMail("여행 좋아해? 앱 이메일 인증번호입니다.", emailData, ts);
                                Toast.makeText(getApplicationContext(), "이메일로 인증번호가 전송되었습니다.", Toast.LENGTH_LONG).show();

                                email = token;
                            } catch (SendFailedException e) {
                                Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                            } catch (MessagingException e) {
                                Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        TextView_EmailOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s = EditText_EmailConfirm.getText().toString();
                EditText_EmailConfirm.setText("");
                if (s.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "인증번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }


                System.out.println("s 출력 : " + s);
                System.out.println("인증번호 출력 : " + emailData);

                if (s.equals(emailData)) {
                    Toast.makeText(getApplicationContext(), "인증되었습니다.", Toast.LENGTH_LONG).show();
                    emailOk = true;
                    EditText_EmailConfirm.setVisibility(View.GONE);
                    TextView_EmailOk.setVisibility(View.GONE);
                    return;
                }
            }
        });

        TextView_Login_Complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = EditText_NickName.getText().toString();
                if (nick.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

//                if (!emailOk) {
//                    Toast.makeText(getApplicationContext(), "이메일을 인증해주세요.", Toast.LENGTH_LONG).show();
//                    return;
//                }

                emailOk = false;
                UserData data = new UserData();
                data.setEmail(email);
                data.setNickName(nick);

                int randomNum = (int) (Math.random() * 10);
                data.setImageName("@drawable/" + imageName[randomNum]);
                    // @drawable/Image01
                dbAcess.child("users").child(token).setValue(data);
//                dbAcess.child("Email").child(email).setValue(1);
                dbAcess.child("signuped").child(token).setValue(data);

                Intent intent = new Intent(LoginDataClass.this, HomeActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("nick", nick);
                startActivity(intent);

                finish();
                // 닉네임과 이메일을 데이터베이스에 넣어주고

                // 인텐트날린다.
            }
        });
    }
}







