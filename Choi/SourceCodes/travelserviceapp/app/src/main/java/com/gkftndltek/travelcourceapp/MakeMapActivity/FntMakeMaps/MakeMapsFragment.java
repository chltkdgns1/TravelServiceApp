package com.gkftndltek.travelcourceapp.MakeMapActivity.FntMakeMaps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.MakeMapActivity.FragmentMaps.MapFragment;
import com.gkftndltek.travelcourceapp.MakeMapActivity.MakeMapAct;
import com.gkftndltek.travelcourceapp.MakeMapActivity.firstRecyclerView.MakeMapData;
import com.gkftndltek.travelcourceapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MakeMapsFragment extends Fragment implements MakeMapAct.OnBackPressedListeners {

    private Context con;
    private View rootView;
    private EditText EditText_MakeMap,EditText_MakeMapComment;
    private TextView Button_MakeMapYes;

    // 데이터 베이스

    private String token;
    private FirebaseDatabase database;
    private DatabaseReference users, logined;

    // 프레그먼트

    private MapFragment mapFragment;


    // 액티비티

    private Activity act;

    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.map_make_fragment, container, false);
        init();
        return rootView;
    }

    void init() {


        act = ((MakeMapAct) getActivity());
        con = getActivity();
        mapFragment = new MapFragment();

        EditText_MakeMap = rootView.findViewById(R.id. EditText_MakeMap);
        EditText_MakeMapComment = rootView.findViewById(R.id.EditText_MakeMapComment);

        token = ((MakeMapAct)act).token;
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
        logined = database.getReference("logined");

        Button_MakeMapYes = rootView.findViewById(R.id.Button_MakeMapYes);
        Button_MakeMapYes.setClickable(true);

        Button_MakeMapYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mapName = EditText_MakeMap.getText().toString();
                String comment = EditText_MakeMapComment.getText().toString();
                if(mapName.isEmpty() || comment.isEmpty()) {
                    Toast.makeText(con,"이름과 내용을 입력해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }

                System.out.println(mapName);

                MakeMapData data = new MakeMapData();
                data.setMapName(mapName);
                data.setComent(comment);
                data.setTag("Tag  태크입니다 ㅎㅎ");

                users.child(token).child("maps").child(mapName).setValue(data);

                Button_MakeMapYes.setText("");
                getActivity(). getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_make_map, mapFragment).commit();

                EditText_MakeMap.setText("");
                EditText_MakeMapComment.setText("");

            }
        });
    }

    @Override
    public void onBack() {
        final MakeMapAct activity = (MakeMapAct) getActivity();

        AlertDialog.Builder alert_ex = new AlertDialog.Builder(con);
        alert_ex.setMessage("정말로 종료하시겠습니까?");

        alert_ex.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert_ex.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
            }
        });
        alert_ex.setTitle("Notice");
        AlertDialog alert = alert_ex.create();
        alert.show();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MakeMapAct) context).setOnBackPressedListener(this); // 프레그먼트
    }

}