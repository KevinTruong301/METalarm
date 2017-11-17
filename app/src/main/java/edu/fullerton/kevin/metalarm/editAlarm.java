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
    private Button discardChange;
    private Button deleteAlarm;
    private String name;
    private String time;
    private Long id;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_alarm);
        db = new alarmDB(this);
        alarmTitle = (EditText)findViewById(R.id.alarmEditName);
        alarmTime = (TimePicker)findViewById(R.id.alarmEditTime);
        saveAlarm = (Button) findViewById(R.id.saveAlarm);
        discardChange = (Button) findViewById(R.id.discardChanges);
        deleteAlarm = (Button) findViewById(R.id.deleteAlarm);

        intent = getIntent();

        name = intent.getStringExtra("name");
        time = intent.getStringExtra("time");
        id = Long.valueOf( intent.getStringExtra("id"));

        alarmTitle.setText(name);

        alarmTime.setCurrentHour(0);
        alarmTime.setCurrentMinute(0);
        alarmTime.setIs24HourView(true);

        saveAlarm.setOnClickListener(this);
        discardChange.setOnClickListener(this);
        deleteAlarm.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.saveAlarm:
                editAlarm();
                break;
            case R.id.discardChanges:
                Intent intent = new Intent(this, alarm.class);
                startActivity(intent);
                break;
            case R.id.deleteAlarm:
                deleteAlarm();
                break;

        }
    }

    private void deleteAlarm(){
        db.deleteAlarm(id);
        Intent intent = new Intent(this, alarm.class);
        startActivity(intent);
    }

    public void editAlarm(){
        String name;
        int hour;
        int min;
        name = alarmTitle.getText().toString();
        hour = alarmTime.getCurrentHour();
        min = alarmTime.getCurrentMinute();

        item_alarm a = new item_alarm(name, hour, min);
        a.setId(id);
        db.updateAlarm(a);

        Intent intent = new Intent(this, alarm.class);
        startActivity(intent);
    }
}


