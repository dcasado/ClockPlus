package com.philliphsu.clock2.timers;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.philliphsu.clock2.AsyncTimersTableUpdateHandler;
import com.philliphsu.clock2.R;
import com.philliphsu.clock2.Timer;
import com.philliphsu.clock2.ringtone.RingtoneService;

public class TimerRingtoneService extends RingtoneService<Timer> {
    private static final String TAG = "TimerRingtoneService";

    // private because they refer to our foreground notification's actions.
    // we reuse these from TimerNotificationService because they're just constants, the values
    // don't actually matter.
    private static final String ACTION_ADD_ONE_MINUTE = TimerNotificationService.ACTION_ADD_ONE_MINUTE;
    private static final String ACTION_STOP = TimerNotificationService.ACTION_STOP;

    private TimerController mController;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // This has to be first so our Timer is initialized
        int value = super.onStartCommand(intent, flags, startId);
        if (mController == null) {
            mController = new TimerController(getRingingObject(),
                    new AsyncTimersTableUpdateHandler(this, null));
        }
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_ADD_ONE_MINUTE:
                    mController.addOneMinute();
                    break;
                case ACTION_STOP:
                    mController.stop();
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
            stopSelf(startId);
            finishActivity();
        }
        return value;
    }

    @Override
    protected void onAutoSilenced() {
        mController.stop();
    }

    @Override
    protected Uri getRingtoneUri() {
        String ringtone = PreferenceManager.getDefaultSharedPreferences(this).getString(
                getString(R.string.key_timer_ringtone), Settings.System.ALARM_ALERT);
        return Uri.parse(ringtone);
    }

    @Override
    protected Notification getForegroundNotification() {
        String title = getRingingObject().label();
        if (title.isEmpty()) {
            title = getString(R.string.timer);
        }
        return new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(getString(R.string.times_up))
                .setSmallIcon(R.drawable.ic_half_day_1_24dp) // TODO: correct icon
                .setShowWhen(false) // TODO: Should we show this?
//                .setOngoing(true) // foreground notes are ongoing by default
                .addAction(R.drawable.ic_add_circle_24dp, // TODO: correct icon
                        getString(R.string.add_one_minute),
                        getPendingIntent(ACTION_ADD_ONE_MINUTE, getRingingObject().getIntId()))
                .addAction(R.drawable.ic_add_circle_24dp, // TODO: correct icon
                        getString(R.string.stop),
                        getPendingIntent(ACTION_STOP, getRingingObject().getIntId()))
                .build();
// TODO:               .setContentIntent(getPendingIntent(timer.requestCode(), intent, true));
    }

    @Override
    protected boolean doesVibrate() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                getString(R.string.key_timer_vibrate), false);
    }

    @Override
    protected int minutesToAutoSilence() {
        // TODO: Use same value as for Alarms, or create new preference.
        return 1;
    }
}
