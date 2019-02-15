package com.philliphsu.clock2.tasker;

import android.support.annotation.NonNull;

import com.philliphsu.clock2.alarms.Alarm;

public class SpinnerAlarm {
    private String text;
    private Alarm alarm;

    public SpinnerAlarm(String text, Alarm alarm) {
        this.text = text;
        this.alarm = alarm;
    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

}
