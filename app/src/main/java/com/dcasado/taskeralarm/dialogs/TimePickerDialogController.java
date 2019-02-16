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

package com.dcasado.taskeralarm.dialogs;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * Created by Phillip Hsu on 9/6/2016.
 */
public final class TimePickerDialogController extends DialogFragmentController<SystemTimePickerDialog> {
    private static final String TAG = "TimePickerController";

    private final TimePickerDialog.OnTimeSetListener mListener;
    private final Context mContext;
    private final FragmentManager mFragmentManager;

    /**
     * @param context Used to read the user's preference for the style of the time picker dialog to show.
     */
    public TimePickerDialogController(FragmentManager fragmentManager, Context context,
                                      TimePickerDialog.OnTimeSetListener listener) {
        super(fragmentManager);
        mFragmentManager = fragmentManager;
        mContext = context;
        mListener = listener;
    }

    public void show(int initialHourOfDay, int initialMinute, String tag) {
        SystemTimePickerDialog timepicker = SystemTimePickerDialog.newInstance(
                mListener, initialHourOfDay, initialMinute, DateFormat.is24HourFormat(mContext));
        timepicker.show(mFragmentManager, tag);
    }

    @Override
    public void tryRestoreCallback(String tag) {
        SystemTimePickerDialog dialog = findDialog(tag);
        if (dialog != null) {
            Log.i(TAG, "Restoring on time selected callback");
            dialog.setOnTimeSetListener(mListener);
        }
    }
}
