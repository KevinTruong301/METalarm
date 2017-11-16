package edu.fullerton.kevin.metalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class alarm extends AppCompatActivity {

    private ListView alarmListView;
    private ArrayList<HashMap<String, String>> data;
    private alarmDB db;
    private StringBuilder sb;
    private int numAlarms;
    private int num;
    private ArrayList<item_alarm> alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmListView = (ListView) findViewById(R.id.alarmList);
        data = new ArrayList<HashMap<String, String>>();

        db = new alarmDB(this);
        sb = new StringBuilder();
        num = 0;

        loadEvents();

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
                Intent intent = new Intent(this, editAlarm.class);
                this.startActivity(intent);
                return true;

            case R.id.setAlarms:
                setAlarms();
                return true;

            case R.id.cancelAlarms:
                db.deleteTable();
                finish();
                startActivity(getIntent());
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
        numAlarms = alarm.size();

        for (item_alarm t: alarm){
            //sb.append(t.getHour() + "" + t.getName()+"\n");

            HashMap<String, String> map = new HashMap<String , String >();
            map.put("name", t.getName());
            String time = String.valueOf(t.getHour()) + String.valueOf(t.getMin());
            map.put("time", time);
            data.add(map);

        }

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        alarmListView.setAdapter(adapter);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setAlarms();
    }

    private void setAlarms(){
        try{
            Calendar currTime = Calendar.getInstance();
            long millS = currTime.getTimeInMillis();
            Date date = currTime.getTime();
            int minutes = (int) ((millS / (1000*60)) % 60);
            int hours   = (int) ((millS / (1000*60*60)) % 24);
            item_alarm a = alarm.get(num);
            int hr = a.getHour();
            int min = a.getMin();
            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            i.putExtra(AlarmClock.EXTRA_HOUR, hr);
            i.putExtra(AlarmClock.EXTRA_MINUTES, min);
            i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
            startActivityForResult(i, 1);
            num++;
        }
        catch(Exception e){

        }


    }

}
