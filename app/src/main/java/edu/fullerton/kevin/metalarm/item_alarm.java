package edu.fullerton.kevin.metalarm;

/**
 * Created by Kevin on 11/15/2017.
 */

public class item_alarm {

    private long taskId;
    private long listId;
    private String name;
    private int hour;
    private int min;
    private String description;
    private String hidden;

    public static final String TRUE = "1";
    public static final String FALSE = "0";

    public item_alarm() {
        name = "";
        hour = 0;
        min = 0;

    }

    public item_alarm(String name, int hour, int min){
        this.name = name;
        this.hour = hour;
        this.min = min;
    }

    public item_alarm(int taskId, String name, int hour, int min) {
        this.taskId = taskId;
        this.name = name;
        this.hour = hour;
        this.min = min;
    }

    public long getId() {
        return taskId;
    }

    public void setId(long taskId) {
        this.taskId = taskId;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {return min;}

    public String getCompletedDate() {
        return description;
    }

    public long getCompletedDateMillis() {
        return Long.parseLong(description);
    }

    public void setCompletedDate(String description) {
        this.description = description;
    }

    public void setCompletedDate(long millis) {
        this.description = Long.toString(millis);
    }

    public String getHidden(){
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }
}