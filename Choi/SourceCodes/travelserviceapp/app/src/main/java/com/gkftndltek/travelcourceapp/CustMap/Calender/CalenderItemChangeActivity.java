package com.gkftndltek.travelcourceapp.CustMap.Calender;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gkftndltek.travelcourceapp.CustMap.Calender.Recycler.CalenderDataClass;
import com.gkftndltek.travelcourceapp.R;

public class CalenderItemChangeActivity extends AppCompatActivity {

    private EditText EditText_Calender_Change_Name,EditText_Calender_Change_Content;
    private EditText EditText_Calender_Adress,EditText_Calender_Category;
    private TextView TextView_Calender_Change_Add,TextView_Alarm;
    private ImageView ImageView_Calender_Image;

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
        ImageView_Calender_Image = findViewById(R.id.ImageView_Calender_Image);
        EditText_Calender_Category = findViewById(R.id.EditText_Calender_Category);
        EditText_Calender_Adress = findViewById(R.id.EditText_Calender_Adress);

        EditText_Calender_Adress.setText(data.getAddress());
        EditText_Calender_Category.setText(data.getCategory());
        EditText_Calender_Change_Name.setText(data.getName());
        EditText_Calender_Change_Content.setText(data.getContent());
        Glide.with(this).load(data.getUrl()).into(ImageView_Calender_Image);

        TextView_Calender_Change_Add = findViewById(R.id.TextView_Calender_Change_Add);
        TextView_Calender_Change_Add.setClickable(true);


        EditText_Calender_Change_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText_Calender_Change_Name.setBackgroundResource(R.drawable.rounded_edittext_red);

            }
        });

        EditText_Calender_Change_Content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText_Calender_Change_Content.setBackgroundResource(R.drawable.rounded_edittext_red);
            }
        });


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