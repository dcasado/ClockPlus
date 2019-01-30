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
import com.philliphsu.clock2.alarms.data.AsyncAlarmsTableUpdateHandler;
import com.philliphsu.clock2.alarms.misc.AlarmController;
import com.philliphsu.clock2.tasker.bundles.PluginBundleValues;
import com.twofortyfouram.locale.sdk.client.receiver.AbstractPluginSettingReceiver;

public final class FireReceiver extends AbstractPluginSettingReceiver {

    private static final String TAG = "FireReceiver";

    @Override
    protected boolean isBundleValid(@NonNull final Bundle bundle) {
        return PluginBundleValues.isBundleValid(bundle);
    }

    @Override
    protected boolean isAsync() {
        return false;
    }

    @Override
    protected void firePluginSetting(@NonNull final Context context, @NonNull final Bundle bundle) {

        AlarmController mAlarmController = new AlarmController(context, null);
        AsyncAlarmsTableUpdateHandler mAsyncUpdateHandler;
        mAsyncUpdateHandler = new AsyncAlarmsTableUpdateHandler(context, null, null, mAlarmController);

        String label = bundle.getString(PluginBundleValues.BUNDLE_EXTRA_STRING_LABEL);
        String[] time = bundle.getString(PluginBundleValues.BUNDLE_EXTRA_STRING_TIME).split("\\.");
        int hour = Integer.valueOf(time[0]);
        int minutes = Integer.valueOf(time[1]);

        Alarm alarm = Alarm.builder()
                .label(label)
                .hour(hour)
                .minutes(minutes)
                .build();
        alarm.setEnabled(true);

//        mAlarmController.scheduleAlarm(alarm, false);

        Log.d("TASKER_INT", "Alarm created");
       // mAsyncUpdateHandler.asyncInsert(alarm);
        mAsyncUpdateHandler.asyncDelete(alarm);
    }
}
