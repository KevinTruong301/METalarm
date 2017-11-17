package edu.fullerton.kevin.metalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class alarmDB {

    // database constants
    public static final String DB_NAME = "alarms.db";
    public static final int    DB_VERSION = 1;

    // alarm table constants
    public static final String ALARM_TABLE = "alarms";

    public static final String ALARM_ID = "_id";
    public static final int    ALARM_ID_COL = 0;

    public static final String ALARM_NAME = "alarm_name";
    public static final int    ALARM_NAME_COL = 1;

    public static final String ALARM_HOUR = "hour";
    public static final int    ALARM_HOUR_COL = 2;

    public static final String ALARM_MIN = "min";
    public static final int    ALARM_MIN_COL = 3;


    // CREATE and DROP TABLE statements

    public static final String CREATE_ALARM_TABLE =
            "CREATE TABLE " + ALARM_TABLE + " (" +
                    ALARM_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ALARM_NAME       + " TEXT, " +
                    ALARM_HOUR      + " int, " +
                    ALARM_MIN       + " int);";

    public static final String DROP_ALARM_TABLE =
            "DROP TABLE IF EXISTS " + ALARM_TABLE;
    public static final String TRUNCATE_ALARM_TABLE =
            "DELETE FROM " + ALARM_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            db.execSQL(CREATE_ALARM_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            db.execSQL(alarmDB.DROP_ALARM_TABLE);
            onCreate(db);
        }
    }
    // database object and database helper object
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public alarmDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    public ArrayList<item_alarm> getAlarms() {
        this.openReadableDB();
        Cursor cursor = db.query(ALARM_TABLE, null,
                null, null,
                null, null, null);
        ArrayList<item_alarm> alarm = new ArrayList<item_alarm>();
        while (cursor.moveToNext()) {
            alarm.add(getAlarmFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();
        this.closeDB();
        return alarm;
    }

    public item_alarm getAlarm(long id) {
        String where = ALARM_ID + "= ?";
        String[] whereArgs = { Long.toString(id) };

        // handle exceptions?
        this.openReadableDB();
        Cursor cursor = db.query(ALARM_TABLE,
                null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        item_alarm alarm = getAlarmFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return alarm;
    }

    private static item_alarm getAlarmFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                item_alarm alarm = new item_alarm(
                        cursor.getInt(ALARM_ID_COL),
                        cursor.getString(ALARM_NAME_COL),
                        cursor.getInt(ALARM_HOUR_COL),
                        cursor.getInt(ALARM_MIN_COL));
                return alarm;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    public long insertAlarm(item_alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(ALARM_NAME, alarm.getName());
        cv.put(ALARM_HOUR, alarm.getHour());
        cv.put(ALARM_MIN, alarm.getMin());

        this.openWriteableDB();
        long rowID = db.insert(ALARM_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateAlarm(item_alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(ALARM_NAME, alarm.getName());
        cv.put(ALARM_HOUR, alarm.getHour());
        cv.put(ALARM_MIN, alarm.getMin());

        String where = ALARM_ID + "= ?";
        String[] whereArgs = { String.valueOf(alarm.getId()) };

        this.openWriteableDB();
        int rowCount = db.update(ALARM_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteAlarm(long id) {
        String where = ALARM_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(ALARM_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public void deleteTable(){
        this.openWriteableDB();
        db.execSQL(alarmDB.TRUNCATE_ALARM_TABLE);
        this.closeDB();
    }
}
