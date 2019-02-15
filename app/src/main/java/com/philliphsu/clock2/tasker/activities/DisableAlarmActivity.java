package com.philliphsu.clock2.tasker.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.philliphsu.clock2.R;
import com.philliphsu.clock2.alarms.Alarm;
import com.philliphsu.clock2.alarms.data.AlarmCursor;
import com.philliphsu.clock2.alarms.data.AlarmsTableManager;
import com.philliphsu.clock2.tasker.SpinnerAlarm;
import com.philliphsu.clock2.tasker.bundles.AlarmBundleValues;
import com.twofortyfouram.locale.sdk.client.ui.activity.AbstractAppCompatPluginActivity;
import com.twofortyfouram.log.Lumberjack;

import net.jcip.annotations.NotThreadSafe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@NotThreadSafe
public final class DisableAlarmActivity extends AbstractAppCompatPluginActivity {

    private AlarmsTableManager mTableManager;
    private Spinner spinner;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tasker_enable_alarm);

        /*
         * To help the user keep context, the title shows the host's name and the subtitle
         * shows the plug-in's name.
         */
        CharSequence callingApplicationLabel = null;
        try {
            callingApplicationLabel =
                    getPackageManager().getApplicationLabel(
                            getPackageManager().getApplicationInfo(getCallingPackage(),
                                    0));
        } catch (final PackageManager.NameNotFoundException e) {
            Lumberjack.e("Calling package couldn't be found%s", e); //$NON-NLS-1$
        }
        if (null != callingApplicationLabel) {
            setTitle(callingApplicationLabel);
        }

        getSupportActionBar().setSubtitle(R.string.disable_alarm_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = findViewById(R.id.activity_tasker_enable_alarm_spinner);

        mTableManager = new AlarmsTableManager(getApplicationContext());

        List<SpinnerAlarm> spinnerAlarms = new ArrayList<>();
        for (Alarm alarm : getAllAlarms()) {
            SpinnerAlarm sa = new SpinnerAlarm(alarm.getIntId() + " - " + alarm.label(), alarm);
            spinnerAlarms.add(sa);
        }

        ArrayAdapter<SpinnerAlarm> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerAlarms);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onPostCreateWithPreviousResult(@NonNull final Bundle previousBundle,
                                               @NonNull final String previousBlurb) {
        final int alarmId = previousBundle.getInt(AlarmBundleValues.BUNDLE_EXTRA_INT_ALARM_ID);

        Alarm alarm = mTableManager.queryItem(alarmId).getItem();
    }

    @Override
    public boolean isBundleValid(@NonNull final Bundle bundle) {
        return AlarmBundleValues.isBundleValid(bundle);
    }

    @Nullable
    @Override
    public Bundle getResultBundle() {
        Bundle result = null;

        final int alarmId = ((SpinnerAlarm) spinner.getSelectedItem()).getAlarm().getIntId();
        if (alarmId >= 0) {
            result = AlarmBundleValues.generateDisableAlarmBundle(getApplicationContext(), alarmId);
        }

        return result;
    }

    @NonNull
    @Override
    public String getResultBlurb(@NonNull final Bundle bundle) {
        final String alarmId = ((SpinnerAlarm) spinner.getSelectedItem()).getText();

        final int maxBlurbLength = getResources().getInteger(
                R.integer.com_twofortyfouram_locale_sdk_client_maximum_blurb_length);

        if (alarmId.length() > maxBlurbLength) {
            return alarmId.substring(0, maxBlurbLength);
        }

        return alarmId;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tasker, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        } else if (R.id.menu_discard_changes == item.getItemId()) {
            // Signal to AbstractAppCompatPluginActivity that the user canceled.
            mIsCancelled = true;
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Alarm> getAllAlarms() {
        List<Alarm> alarms = new ArrayList<>();
        AlarmCursor cursor = mTableManager.queryItems();
        while (cursor.moveToNext()) {
            Alarm alarm = cursor.getItem();
            alarms.add(alarm);
        }
        cursor.close();
        return alarms;
    }
}
