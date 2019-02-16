/*
 * Copyright 2017 Phillip Hsu
 *
 * This file is part of ClockPlus.
 *
 * ClockPlus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ClockPlus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ClockPlus.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dcasado.taskeralarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Phillip Hsu on 5/31/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String CHANNEL_ID_UPCOMING = "com.dcasado.taskeralarm.CHANNEL_UPCOMING";
    public static final String CHANNEL_ID_MISSED = "com.dcasado.taskeralarm.CHANNEL_MISSED";
    public static final String CHANNEL_ID_STOPWATCH = "com.dcasado.taskeralarm.CHANNEL_STOPWATCH";
    public static final String CHANNEL_ID_TIMES_UP = "com.dcasado.taskeralarm.CHANNEL_TIMES_UP";
    public static final String CHANNEL_ID_RINGING = "com.dcasado.taskeralarm.CHANNEL_RINGING";
    public static final String CHANNEL_ID_EXPIRED = "com.dcasado.taskeralarm.CHANNEL_EXPIRED";

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private Menu mMenu;

    @LayoutRes
    protected abstract int layoutResId();

    @MenuRes
    protected abstract int menuResId();

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the associated SharedPreferences file with default values
        // for each preference when the user first opens your application.
        // When false, the system sets the default values only if this method has
        // never been called in the past (or the KEY_HAS_SET_DEFAULT_VALUES in the
        // default value shared preferences file is false).
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        // ========================================================================================
        // TOneverDO: Set theme after setContentView()
        final String themeDark = getString(R.string.theme_dark);
        final String themeBlack = getString(R.string.theme_black);
        String theme = PreferenceManager.getDefaultSharedPreferences(this).getString(
                getString(R.string.key_theme), null);
        if (themeDark.equals(theme)) {
            setTheme(R.style.AppTheme_Dark);
        } else if (themeBlack.equals(theme)) {
            setTheme(R.style.AppTheme_Black);
        }
        // ========================================================================================
        setContentView(layoutResId());
        // Direct volume changes to the alarm stream
        setVolumeControlStream(AudioManager.STREAM_ALARM);
        ButterKnife.bind(this);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(isDisplayHomeUpEnabled());
                ab.setDisplayShowTitleEnabled(isDisplayShowTitleEnabled());
            }
        }
        createAllNotificationChannels();
    }

    @Override
    public final boolean onCreateOptionsMenu(Menu menu) {
        if (menuResId() != 0) {
            getMenuInflater().inflate(menuResId(), menu);
            mMenu = menu;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Nullable
    public final Menu getMenu() {
        return mMenu;
    }

    protected boolean isDisplayHomeUpEnabled() {
        return true;
    }

    protected boolean isDisplayShowTitleEnabled() {
        return false;
    }

    private void createAllNotificationChannels() {
        createNotificationChannel(CHANNEL_ID_UPCOMING,
                getString(R.string.upcoming_alarm_channel_name),
                getString(R.string.upcoming_alarm_channel_description));
        createNotificationChannel(CHANNEL_ID_MISSED,
                getString(R.string.missed_alarm_channel_name),
                getString(R.string.missed_alarm_channel_description));
        createNotificationChannel(CHANNEL_ID_STOPWATCH,
                getString(R.string.stopwatch_channel_name),
                getString(R.string.stopwatch_channel_description));
        createNotificationChannel(CHANNEL_ID_TIMES_UP,
                getString(R.string.times_up_channel_name),
                getString(R.string.times_up_channel_description));
        createNotificationChannel(CHANNEL_ID_RINGING,
                getString(R.string.ringing_alarm_channel_name),
                getString(R.string.ringing_alarm_channel_description));
        createNotificationChannel(CHANNEL_ID_EXPIRED,
                getString(R.string.timer_expired_channel_name),
                getString(R.string.timer_expired_channel_description));
    }

    private void createNotificationChannel(String channelID, String channelName, String channelDescription) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(channelID, channelName, importance);
            channel.setDescription(channelDescription);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
