package com.dcasado.taskeralarm.tasker.receiver;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dcasado.taskeralarm.R;
import com.dcasado.taskeralarm.alarms.Alarm;
import com.dcasado.taskeralarm.alarms.data.AlarmCursor;
import com.dcasado.taskeralarm.alarms.data.AlarmsTableManager;
import com.dcasado.taskeralarm.alarms.data.AsyncAlarmsTableUpdateHandler;
import com.dcasado.taskeralarm.alarms.misc.AlarmController;
import com.dcasado.taskeralarm.tasker.bundles.AlarmBundleValues;
import com.twofortyfouram.locale.sdk.client.receiver.AbstractPluginSettingReceiver;

import static com.dcasado.taskeralarm.util.Preconditions.checkNotNull;

public final class FireReceiver extends AbstractPluginSettingReceiver {

    private static final String TAG = "FireReceiver";
    private AlarmController mAlarmController;
    private AsyncAlarmsTableUpdateHandler mAsyncUpdateHandler;

    @Override
    protected boolean isBundleValid(@NonNull final Bundle bundle) {
        return AlarmBundleValues.isBundleValid(bundle);
    }

    @Override
    protected boolean isAsync() {
        return false;
    }

    @Override
    protected void firePluginSetting(@NonNull final Context context, @NonNull final Bundle bundle) {

        mAlarmController = new AlarmController(context, null);
        mAsyncUpdateHandler = new AsyncAlarmsTableUpdateHandler(context, null, null, mAlarmController);

        int action = bundle.getInt(AlarmBundleValues.BUNDLE_EXTRA_INT_ACTION);
        int alarmId;
        String time;
        boolean enabled;
        String[] parsedTime;
        int hour;
        int minutes;

        switch (action) {
            case 0:
                String label = bundle.getString(AlarmBundleValues.BUNDLE_EXTRA_STRING_LABEL);
                time = bundle.getString(AlarmBundleValues.BUNDLE_EXTRA_STRING_TIME);
                parsedTime = parseTime(time);
                hour = Integer.valueOf(parsedTime[0]);
                minutes = Integer.valueOf(parsedTime[1]);
                createAlarm(context, label, hour, minutes);
                break;
            case 1:
                alarmId = bundle.getInt(AlarmBundleValues.BUNDLE_EXTRA_INT_ALARM_ID);
                deleteAlarm(context, alarmId);
                break;
            case 2:
                alarmId = bundle.getInt(AlarmBundleValues.BUNDLE_EXTRA_INT_ALARM_ID);
                setEnabledAlarm(context, alarmId, true);
                break;
            case 3:
                alarmId = bundle.getInt(AlarmBundleValues.BUNDLE_EXTRA_INT_ALARM_ID);
                setEnabledAlarm(context, alarmId, false);
                break;
            case 4:
                alarmId = bundle.getInt(AlarmBundleValues.BUNDLE_EXTRA_INT_ALARM_ID);
                time = bundle.getString(AlarmBundleValues.BUNDLE_EXTRA_STRING_TIME);
                enabled = bundle.getBoolean(AlarmBundleValues.BUNDLE_EXTRA_BOOLEAN_ENABLED);
                parsedTime = parseTime(time);
                hour = Integer.valueOf(parsedTime[0]);
                minutes = Integer.valueOf(parsedTime[1]);
                modifyAlarm(context, alarmId, hour, minutes, enabled);
                break;
            default:
                Log.d(TAG, "Action " + action + " not recognized");
                break;
        }
    }

    private String[] parseTime(String time) {
        String regex;
        if (time.contains(".")) {
            regex = "\\.";
        } else if (time.contains(":")) {
            regex = ":";
        } else {
            throw new IllegalArgumentException("Time must be of format HH.mm or HH:mm");
        }
        String[] parsedTime = time.split(regex);
        if (parsedTime.length != 2) {
            throw new IllegalArgumentException("Time must be of format HH.mm or HH:mm");
        }
        return parsedTime;
    }

    private void createAlarm(Context context, String label, int hour, int minutes) {
        String ringtone = PreferenceManager.getDefaultSharedPreferences(context).getString(
                context.getString(R.string.key_alarm_ringtone), Settings.System.ALARM_ALERT);

        Alarm alarm = Alarm.builder()
                .label(label)
                .hour(hour)
                .minutes(minutes)
                .ringtone(ringtone)
                .build();
        alarm.setEnabled(true);

        Log.d(TAG, "Alarm created");
        mAsyncUpdateHandler.asyncInsert(alarm);
    }

    private void deleteAlarm(Context context, int alarmId) {
        Log.d(TAG, "Alarm to be deleted: " + alarmId);

        AlarmCursor cursor = new AlarmsTableManager(context).queryItem(alarmId);
        Alarm alarm = checkNotNull(cursor.getItem());

        mAsyncUpdateHandler.asyncDelete(alarm);
    }

    private void setEnabledAlarm(Context context, int alarmId, boolean enable) {
        Alarm alarm = new AlarmsTableManager(context).queryItem(alarmId).getItem();
        alarm.setEnabled(enable);
        mAlarmController.scheduleAlarm(alarm, false);
        mAlarmController.save(alarm);
        Log.d(TAG, "Alarm enabled: " + alarmId);
    }

    private void modifyAlarm(Context context, int alarmId, int hour, int minutes, boolean enabled) {
        Alarm alarm = new AlarmsTableManager(context).queryItem(alarmId).getItem();
        Alarm newAlarm = alarm.toBuilder()
                .hour(hour)
                .minutes(minutes)
                .build();
        alarm.copyMutableFieldsTo(newAlarm);
        newAlarm.setEnabled(enabled);
        mAlarmController.scheduleAlarm(newAlarm, false);
        mAlarmController.save(newAlarm);
        Log.d(TAG, "Alarm modified: " + alarmId);
    }
}
