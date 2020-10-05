package com.gkftndltek.travelcourceapp.Login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import com.gkftndltek.travelcourceapp.MakeMapActivity.MakeMapAct;
import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.RealHome.HomeActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInOptions gso;
    private FirebaseAuth mAuth;

    private LinearLayout LinearLayout_Google_Button, LinearLayout_Logout;
    private GoogleApiClient mGoogleApiClient;

    // 파이어베이스

    private FirebaseDatabase database;
    private DatabaseReference users, logined;

    // Alert
    private AlertDialog.Builder alt_bld;
    private EditText et;
    private AlertDialog alert;
    private String nickName = "";
    private boolean okCheck = false;

    // 프로그래스바

    AppCompatDialog progressDialog = null;

    // 뷰

    private boolean startCheck = false;
    private ImageView ImageView_Loading;

    // 로그아웃체크

    private boolean logoutCheck = false;
    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { // 네이버 검색했다
                /*
                UserData data = (UserData) msg.obj;
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);

                 */
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        init();
    }

    public void init() {

        ImageView_Loading = findViewById(R.id.ImageView_Loading);

        final Handler handler = new Handler();

        progressON("시작중입니다....");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (startCheck) {
                    handler.postDelayed(this,2000);
                    progressOFF();
                    ImageView_Loading.setVisibility(View.GONE);
                }
                else{
                    startCheck = false;
                }
            }
        },2000);

        Intent it = getIntent();
        if(it != null){
            Bundle bun = it.getExtras();
            if (bun != null ) {
                if (bun.get("logout") != null) {
                    logoutCheck = (boolean) bun.get("logout");
                }
            }
        }

        getAlert();
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

        users = database.getReference("users");
        logined = database.getReference("logined");

        LinearLayout_Google_Button = findViewById(R.id.LinearLayout_Google_Button);
        LinearLayout_Google_Button.setClickable(true);

        /*
        LinearLayout_Logout = findViewById(R.id.LinearLayout_Logout);
        LinearLayout_Logout.setClickable(true);

        LinearLayout_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                // 귀중한 코드
            }
        });

         */

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        if (logoutCheck == true) {
            signOut();
        }

        goolgleStart();
        startCheck = true;
    }

    //
    public void signOut() {

        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

            @Override
            public void onConnected(@Nullable Bundle bundle) {

                mAuth.signOut();
                if (mGoogleApiClient.isConnected()) {

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {

                            } else {

                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                finish();
            }
        });
    }

    private void goolgleStart() {
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.LinearLayout_Google_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

                // Toast.makeText(getApplicationContext(), "Google sign in success", Toast.LENGTH_LONG).show();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Toast.makeText(getApplicationContext(), "Google sign in failed", Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        // Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String token = acct.getEmail().replace("@", " ").replace("."," ");
                            users.child(token).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    UserData data = dataSnapshot.getValue(UserData.class);

                                    if (data != null) {
                                        logined.child(token).setValue(1);
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        intent.putExtra("token", token);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, LoginDataClass.class);
                                        intent.putExtra("token", token);
                                        startActivity(intent);
                                    }
                                    return;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // 실패한 경우
                        }
                    }
                });
    }

    void getAlert() {
        et = new EditText(this);
        alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("내 닉네임")
                .setMessage("사용할 닉네임을 정해주세요")
                .setCancelable(false)
                .setView(et)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        nickName = et.getText().toString();
                        okCheck = true;
                    }
                });

        alert = alt_bld.create();
    }

    public void progressON(String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        }
        else {
            progressDialog = new AppCompatDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.dialog_layout);
            progressDialog.show();
            onPause();
        }

        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();

        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }
    }

    public void progressSET(String message) {

        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }

    }

    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}




