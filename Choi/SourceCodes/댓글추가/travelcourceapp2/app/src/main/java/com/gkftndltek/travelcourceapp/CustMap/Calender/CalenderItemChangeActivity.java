package com.gkftndltek.travelcourceapp.CustMap.Calender;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gkftndltek.travelcourceapp.CustMap.Calender.Recycler.CalenderDataClass;
import com.gkftndltek.travelcourceapp.R;

public class CalenderItemChangeActivity extends AppCompatActivity {

    private EditText EditText_Calender_Change_Name,EditText_Calender_Change_Content;
    private TextView TextView_Calender_Change_Add;

    private CalenderDataClass data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_item_change);
        init();
    }

    void init(){
        final Intent intent = getIntent();

        Intent it = getIntent();
        Bundle bun = it.getExtras();
        data = (CalenderDataClass) bun.getSerializable("data");

        EditText_Calender_Change_Name = findViewById(R.id.EditText_Calender_Change_Name);
        EditText_Calender_Change_Content = findViewById(R.id.EditText_Calender_Change_Content);

        EditText_Calender_Change_Name.setText(data.getName());
        EditText_Calender_Change_Content.setText(data.getContent());

        TextView_Calender_Change_Add = findViewById(R.id.TextView_Calender_Change_Add);
        TextView_Calender_Change_Add.setClickable(true);

        TextView_Calender_Change_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.setName( EditText_Calender_Change_Name.getText().toString());
                data.setContent(EditText_Calender_Change_Content.getText().toString());

                Intent intent = new Intent();
                intent.putExtra("data", data);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

}

