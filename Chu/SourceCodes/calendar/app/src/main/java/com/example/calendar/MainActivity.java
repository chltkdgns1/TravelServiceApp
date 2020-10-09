package com.example.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.DatePicker;

public class MainActivity extends AppCompatActivity {

    //달력 설정
    private CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    void init(){
        calendarView = findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth){
                String data = year + "/" + (month+1)+"/"+dayOfMonth;
                Log.d("test","year = "+year + ", month = " + month+", day = "+dayOfMonth);
                Data data1= new Data(year,month,dayOfMonth);
                System.out.println(data1);
                Intent intent = new Intent(MainActivity.this,NextActivity.class);
                intent.putExtra("Data",data1);
                startActivity(intent);
            }
        });
    }
}