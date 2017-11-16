package edu.fullerton.kevin.metalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class alarm extends AppCompatActivity {

    private ListView alarmListView;
    private ArrayList<HashMap<String, String>> data;
    private alarmDB db;
    private StringBuilder sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmListView = (ListView) findViewById(R.id.alarmList);
        data = new ArrayList<HashMap<String, String>>();

        db = new alarmDB(this);
        sb = new StringBuilder();


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



        ArrayList<item_alarm> task = db.getAlarms();


        for (item_alarm t: task){
            //sb.append(t.getHour() + "" + t.getName()+"\n");

            HashMap<String, String> map = new HashMap<String , String >();
            map.put("name", t.getName());
            String time = String.valueOf(t.getHour()) + String.valueOf(t.getMin());
            map.put("time", time);
            data.add(map);

        }


        //put a forloop here to



        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        alarmListView.setAdapter(adapter);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("editTextValue");
            }
        }
    }

    private void setAlarms(){

    }

}
