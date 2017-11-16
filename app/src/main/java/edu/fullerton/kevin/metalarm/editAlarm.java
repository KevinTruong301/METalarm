package edu.fullerton.kevin.metalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Created by Kevin on 11/15/2017.
 */

public class editAlarm extends AppCompatActivity implements View.OnClickListener{

    private alarmDB db;
    private EditText alarmTitle;
    private TimePicker alarmTime;
    private Button saveAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_alarm);
        db = new alarmDB(this);
        alarmTitle = (EditText)findViewById(R.id.alarmEditName);
        alarmTime = (TimePicker)findViewById(R.id.alarmEditTime);
        saveAlarm = (Button) findViewById(R.id.saveAlarm);


        alarmTime.setIs24HourView(true);
        saveAlarm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.saveAlarm:
                addAlarm();
            case R.id.discardChanges:
                Intent intent = new Intent(this, alarm.class);
                startActivity(intent);
            case R.id.deleteAlarm:

        }
    }

    public void addAlarm(){
        String name;
        int hour;
        int min;
        name = alarmTitle.getText().toString();
        hour = alarmTime.getCurrentHour();
        min = alarmTime.getCurrentMinute();

        item_alarm a = new item_alarm(name, hour, min);
        db.insertAlarm(a);

        Intent intent = new Intent(this, alarm.class);
        startActivity(intent);
    }
}


