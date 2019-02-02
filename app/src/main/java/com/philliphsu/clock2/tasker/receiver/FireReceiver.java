/*
 * android-toast-setting-plugin-for-locale <https://github.com/twofortyfouram/android-toast-setting-plugin-for-locale>
 * Copyright 2014 two forty four a.m. LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.philliphsu.clock2.tasker.receiver;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philliphsu.clock2.alarms.Alarm;
import com.philliphsu.clock2.alarms.data.AlarmCursor;
import com.philliphsu.clock2.alarms.data.AlarmsTable;
import com.philliphsu.clock2.alarms.data.AlarmsTableManager;
import com.philliphsu.clock2.alarms.data.AsyncAlarmsTableUpdateHandler;
import com.philliphsu.clock2.alarms.misc.AlarmController;
import com.philliphsu.clock2.tasker.bundles.DeleteAlarmBundleValues;
import com.philliphsu.clock2.tasker.bundles.PluginBundleValues;
import com.twofortyfouram.locale.sdk.client.receiver.AbstractPluginSettingReceiver;

import static com.philliphsu.clock2.util.Preconditions.checkNotNull;

public final class FireReceiver extends AbstractPluginSettingReceiver {

    private static final String TAG = "FireReceiver";

    @Override
    protected boolean isBundleValid(@NonNull final Bundle bundle) {
        return PluginBundleValues.isBundleValid(bundle) || DeleteAlarmBundleValues.isBundleValid(bundle);
    }

    @Override
    protected boolean isAsync() {
        return false;
    }

    @Override
    protected void firePluginSetting(@NonNull final Context context, @NonNull final Bundle bundle) {
        int action = bundle.getInt(PluginBundleValues.BUNDLE_EXTRA_INT_ACTION);
        String label = bundle.getString(PluginBundleValues.BUNDLE_EXTRA_STRING_LABEL);

        switch (action) {
            case 0:
                String time = bundle.getString(PluginBundleValues.BUNDLE_EXTRA_STRING_TIME);
                String[] parsedTime;
                if (time.contains(".")) {
                    parsedTime = parseTime(time, "\\.");
                } else if (time.contains(":")) {
                    parsedTime = parseTime(time, ":");
                } else {
                    throw new IllegalArgumentException("Time must be of format HH.mm or HH:mm");
                }
                int hour = Integer.valueOf(parsedTime[0]);
                int minutes = Integer.valueOf(parsedTime[1]);
                createAlarm(context, label, hour, minutes);
                break;
            case 1:
                deleteAlarm(context, label);
                break;
            default:
                break;
        }
    }

    private String[] parseTime(String time, String regex) {
        String[] parsedTime = time.split(regex);
        if (parsedTime.length != 2) {
            throw new IllegalArgumentException("Time must be of format HH.mm or HH:mm");
        }
        return parsedTime;
    }

    private void createAlarm(Context context, String label, int hour, int minutes) {
        AlarmController mAlarmController = new AlarmController(context, null);
        AsyncAlarmsTableUpdateHandler mAsyncUpdateHandler;
        mAsyncUpdateHandler = new AsyncAlarmsTableUpdateHandler(context, null, null, mAlarmController);

        Alarm alarm = Alarm.builder()
                .label(label)
                .hour(hour)
                .minutes(minutes)
                .build();
        alarm.setEnabled(true);

        Log.d(TAG, "Alarm created");
        mAsyncUpdateHandler.asyncInsert(alarm);
    }

    private void deleteAlarm(Context context, String label) {
        Log.d(TAG, "Alarm to be deleted: " + label);
        AsyncAlarmsTableUpdateHandler mAsyncUpdateHandler;
        AlarmController mAlarmController = new AlarmController(context, null);
        mAsyncUpdateHandler = new AsyncAlarmsTableUpdateHandler(context, null, null, mAlarmController);

        String where = AlarmsTable.COLUMN_LABEL + " = \"" + label + "\"";
        AlarmCursor cursor = new AlarmsTableManager(context).queryItems(where, "1");
        cursor.moveToFirst();
        Alarm alarm = checkNotNull(cursor.getItem());

        mAsyncUpdateHandler.asyncDelete(alarm);
    }
}
