package edu.fullerton.kevin.metalarm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class alarm extends AppCompatActivity {

    private ListView alarmListView;
    private ArrayList<HashMap<String, String>> data;
    private alarmDB db;
    private int num;
    private ArrayList<item_alarm> alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmListView = (ListView) findViewById(R.id.alarmList);
        data = new ArrayList<HashMap<String, String>>();

        db = new alarmDB(this);
        num = 0;

        loadEvents();

        alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map o = (HashMap) alarmListView.getItemAtPosition(position);
                String name = (String) o.get("name");
                String time = (String) o.get("time");
                String ID = (String) o.get("id");
                Intent intent = new Intent(alarm.this, editAlarm.class);
                intent.putExtra("name", name);
                intent.putExtra("time", time);
                intent.putExtra("id", ID);
                alarm.this.startActivity(intent);

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.alarm_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Context context = getApplicationContext();
        Toast toastCancel = Toast.makeText(context, "Alarms Canceled", Toast.LENGTH_SHORT);
        switch(item.getItemId()) {
            case R.id.newEvent:
                addAlarm();
                return true;

            case R.id.setAlarms:
                setAlarms();
                return true;

            case R.id.cancelAlarms:
                /*db.deleteTable();
                finish();
                startActivity(getIntent());*/
                cancelAlarms();
                toastCancel.show();
                return true;
        }
        return true;
    }

    private void loadEvents(){
        int resource = R.layout.alarm_item;
        String[] from = {"name", "time"};
        int [] to = {R.id.alarmName, R.id.alarmTime};

        alarm = db.getAlarms();

        for (item_alarm t: alarm){
            //sb.append(t.getHour() + "" + t.getName()+"\n");

            HashMap<String, String> map = new HashMap<String , String >();
            map.put("name", t.getName());
            String time = String.valueOf(t.getHour()) +":"+ String.valueOf(t.getMin());
            map.put("time", time);
            map.put("id", String.valueOf(t.getId()));
            data.add(map);

        }

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        alarmListView.setAdapter(adapter);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            setAlarms();
        }
        if(requestCode == 2){
            cancelAlarms();
        }
    }

    private void cancelAlarms(){
        Intent delAlarm = new Intent(AlarmClock.ACTION_DISMISS_ALARM);
        delAlarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        delAlarm.putExtra(AlarmClock.ALARM_SEARCH_MODE_ALL, true);
        startActivityForResult(delAlarm, 2);
    }

    private void setAlarms(){
        try{
            item_alarm a = alarm.get(num);
            int hr = a.getHour();
            int min = a.getMin();
            Calendar currTime = Calendar.getInstance();
            currTime.add(Calendar.HOUR_OF_DAY, hr);
            currTime.add(Calendar.MINUTE, min);

            int minMET = currTime.get(Calendar.MINUTE);
            int hrMET   = currTime.get(Calendar.HOUR_OF_DAY);

            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            i.putExtra(AlarmClock.EXTRA_HOUR,hrMET);
            i.putExtra(AlarmClock.EXTRA_MINUTES, minMET);
            i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
            startActivityForResult(i, 1);
            num++;
        }
        catch(Exception e){

        }


    }

    public void addAlarm(){
        String name;
        int hour;
        int min;
        name = "Default";
        hour = 0;
        min = 0;

        item_alarm a = new item_alarm(name, hour, min);
        db.insertAlarm(a);

        Intent intent = new Intent(this, alarm.class);
        startActivity(intent);
    }

}
