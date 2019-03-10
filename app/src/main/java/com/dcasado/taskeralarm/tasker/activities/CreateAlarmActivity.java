package com.dcasado.taskeralarm.tasker.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.dcasado.taskeralarm.R;
import com.dcasado.taskeralarm.tasker.TaskerPlugin;
import com.dcasado.taskeralarm.tasker.bundles.AlarmBundleValues;
import com.twofortyfouram.locale.sdk.client.ui.activity.AbstractAppCompatPluginActivity;
import com.twofortyfouram.log.Lumberjack;

import net.jcip.annotations.NotThreadSafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@NotThreadSafe
public final class CreateAlarmActivity extends AbstractAppCompatPluginActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tasker_create_alarm);

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

        getSupportActionBar().setSubtitle(R.string.create_alarm_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onPostCreateWithPreviousResult(@NonNull final Bundle previousBundle,
                                               @NonNull final String previousBlurb) {
        final String label = previousBundle.getString(AlarmBundleValues.BUNDLE_EXTRA_STRING_LABEL);
        ((EditText) findViewById(R.id.activity_tasker_create_alarm_edit_text_label)).setText(label);
        final String time = previousBundle.getString(AlarmBundleValues.BUNDLE_EXTRA_STRING_TIME);
        ((EditText) findViewById(R.id.activity_tasker_create_alarm_edit_text_time)).setText(time);
    }

    @Override
    public boolean isBundleValid(@NonNull final Bundle bundle) {
        return AlarmBundleValues.isBundleValid(bundle);
    }

    @Nullable
    @Override
    public Bundle getResultBundle() {
        Bundle result = null;

        final String label = ((EditText) findViewById(R.id.activity_tasker_create_alarm_edit_text_label)).getText().toString();
        final String time = ((EditText) findViewById(R.id.activity_tasker_create_alarm_edit_text_time)).getText().toString();
        if (!TextUtils.isEmpty(label) && !TextUtils.isEmpty(time)) {
            result = AlarmBundleValues.generateCreateAlarmBundle(getApplicationContext(), label, time);

            if (TaskerPlugin.Setting.hostSupportsOnFireVariableReplacement(this)) {
                TaskerPlugin.Setting.setVariableReplaceKeys(result, new String[]{
                        AlarmBundleValues.BUNDLE_EXTRA_STRING_LABEL,
                        AlarmBundleValues.BUNDLE_EXTRA_STRING_TIME
                });
            }
        }

        return result;
    }

    @NonNull
    @Override
    public String getResultBlurb(@NonNull final Bundle bundle) {
        final String label = bundle.getString(AlarmBundleValues.BUNDLE_EXTRA_STRING_LABEL);

        final int maxBlurbLength = getResources().getInteger(
                R.integer.com_twofortyfouram_locale_sdk_client_maximum_blurb_length);

        if (label.length() > maxBlurbLength) {
            return label.substring(0, maxBlurbLength);
        }

        return label;
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
}
