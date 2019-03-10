package com.dcasado.taskeralarm.tasker.bundles;

import android.content.Context;
import android.os.Bundle;

import com.dcasado.taskeralarm.tasker.Actions;
import com.twofortyfouram.assertion.BundleAssertions;
import com.twofortyfouram.log.Lumberjack;
import com.twofortyfouram.spackle.AppBuildInfo;

import net.jcip.annotations.ThreadSafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.twofortyfouram.assertion.Assertions.assertNotEmpty;
import static com.twofortyfouram.assertion.Assertions.assertNotNull;

@ThreadSafe
public final class AlarmBundleValues {

    @NonNull
    public static final String BUNDLE_EXTRA_INT_ACTION
            = "com.dcasado.extra.ACTIONS_ACTION";

    @NonNull
    public static final String BUNDLE_EXTRA_INT_ALARM_ID
            = "com.dcasado.extra.INT_ALARM_ID";

    @NonNull
    public static final String BUNDLE_EXTRA_STRING_LABEL
            = "com.dcasado.extra.STRING_LABEL";

    @NonNull
    public static final String BUNDLE_EXTRA_STRING_TIME
            = "com.dcasado.extra.STRING_TIME";

    @NonNull
    public static final String BUNDLE_EXTRA_BOOLEAN_ENABLED
            = "com.dcasado.extra.BOOLEAN_ENABLED";

    /**
     * Type: {@code int}.
     * <p>
     * versionCode of the plug-in that saved the Bundle.
     */
    /*
     * This extra is not strictly required, however it makes backward and forward compatibility
     * significantly easier. For example, suppose a bug is found in how some version of the plug-in
     * stored its Bundle. By having the version, the plug-in can better detect when such bugs occur.
     */
    @NonNull
    public static final String BUNDLE_EXTRA_INT_VERSION_CODE
            = "com.dcasado.extra.INT_VERSION_CODE";

    /**
     * Private constructor prevents instantiation
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private AlarmBundleValues() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }

    /**
     * Method to verify the content of the bundle are correct.
     * <p>
     * This method will not mutate {@code bundle}.
     *
     * @param bundle bundle to verify. May be null, which will always return false.
     * @return true if the Bundle is valid, false if the bundle is invalid.
     */
    public static boolean isBundleValid(@Nullable final Bundle bundle) {
        if (null == bundle) {
            return false;
        }

        int action = bundle.getInt(BUNDLE_EXTRA_INT_ACTION);
        switch (action) {
            case 0:
                return isCreateAlarmBundleValid(bundle);
            case 1:
                return isDeleteAlarmBundleValid(bundle);
            case 2:
                return isEnableAlarmBundleValid(bundle);
            case 3:
                return isDisableAlarmBundleValid(bundle);
            case 4:
                return isModifyAlarmBundleValid(bundle);
            default:
                return false;
        }
    }

    private static boolean isCreateAlarmBundleValid(@NonNull final Bundle bundle) {
        try {
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_ACTION);
            BundleAssertions.assertHasString(bundle, BUNDLE_EXTRA_STRING_LABEL, false, false);
            BundleAssertions.assertHasString(bundle, BUNDLE_EXTRA_STRING_TIME, false, false);
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_VERSION_CODE);
//            BundleAssertions.assertKeyCount(bundle, 4);
        } catch (final AssertionError e) {
            Lumberjack.e("Bundle failed verification%s", e);
            return false;
        }
        return true;
    }

    private static boolean isDeleteAlarmBundleValid(@NonNull final Bundle bundle) {
        try {
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_ACTION);
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_ALARM_ID);
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_VERSION_CODE);
            BundleAssertions.assertKeyCount(bundle, 3);
        } catch (final AssertionError e) {
            Lumberjack.e("Bundle failed verification%s", e);
            return false;
        }
        return true;
    }

    private static boolean isEnableAlarmBundleValid(@NonNull final Bundle bundle) {
        try {
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_ACTION);
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_ALARM_ID);
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_VERSION_CODE);
            BundleAssertions.assertKeyCount(bundle, 3);
        } catch (final AssertionError e) {
            Lumberjack.e("Bundle failed verification%s", e);
            return false;
        }
        return true;
    }

    private static boolean isDisableAlarmBundleValid(@NonNull final Bundle bundle) {
        try {
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_ACTION);
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_ALARM_ID);
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_VERSION_CODE);
            BundleAssertions.assertKeyCount(bundle, 3);
        } catch (final AssertionError e) {
            Lumberjack.e("Bundle failed verification%s", e);
            return false;
        }
        return true;
    }

    private static boolean isModifyAlarmBundleValid(@NonNull final Bundle bundle) {
        try {
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_ACTION);
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_ALARM_ID);
            BundleAssertions.assertHasString(bundle, BUNDLE_EXTRA_STRING_TIME);
            BundleAssertions.assertHasBoolean(bundle, BUNDLE_EXTRA_BOOLEAN_ENABLED);
            BundleAssertions.assertHasInt(bundle, BUNDLE_EXTRA_INT_VERSION_CODE);
            //BundleAssertions.assertKeyCount(bundle, 4);
        } catch (final AssertionError e) {
            Lumberjack.e("Bundle failed verification%s", e);
            return false;
        }
        return true;
    }

    /**
     * @param context Application context.
     * @param label   The toast label to be displayed by the plug-in.
     * @param time    The time to create the alarm
     * @return A plug-in bundle.
     */
    @NonNull
    public static Bundle generateCreateAlarmBundle(@NonNull final Context context,
                                                   @NonNull final String label,
                                                   @NonNull final String time) {
        assertNotNull(context, "context");
        assertNotEmpty(label, "label");
        assertNotEmpty(label, "time");

        final Bundle result = new Bundle();
        result.putInt(BUNDLE_EXTRA_INT_VERSION_CODE, AppBuildInfo.getVersionCode(context));
        result.putInt(BUNDLE_EXTRA_INT_ACTION, Actions.CREATE.getValue());
        result.putString(BUNDLE_EXTRA_STRING_LABEL, label);
        result.putString(BUNDLE_EXTRA_STRING_TIME, time);

        return result;
    }

    /**
     * @param context Application context.
     * @param alarmId The ID of the alarm to delete
     * @return A plug-in bundle.
     */
    @NonNull
    public static Bundle generateDeleteAlarmBundle(@NonNull final Context context,
                                                   final int alarmId) {
        assertNotNull(context, "context");
        assertNotNull(alarmId, "alarmId");

        final Bundle result = new Bundle();
        result.putInt(BUNDLE_EXTRA_INT_VERSION_CODE, AppBuildInfo.getVersionCode(context));
        result.putInt(BUNDLE_EXTRA_INT_ACTION, Actions.DELETE.getValue());
        result.putInt(BUNDLE_EXTRA_INT_ALARM_ID, alarmId);

        return result;
    }

    /**
     * @param context Application context.
     * @param alarmId The ID of the alarm to enable
     * @return A plug-in bundle.
     */
    @NonNull
    public static Bundle generateEnableAlarmBundle(@NonNull final Context context,
                                                   final int alarmId) {
        assertNotNull(context, "context");
        assertNotNull(alarmId, "alarmId");

        final Bundle result = new Bundle();
        result.putInt(BUNDLE_EXTRA_INT_VERSION_CODE, AppBuildInfo.getVersionCode(context));
        result.putInt(BUNDLE_EXTRA_INT_ACTION, Actions.ENABLE.getValue());
        result.putInt(BUNDLE_EXTRA_INT_ALARM_ID, alarmId);

        return result;
    }

    /**
     * @param context Application context.
     * @param alarmId The ID of the alarm to disable
     * @return A plug-in bundle.
     */
    @NonNull
    public static Bundle generateDisableAlarmBundle(@NonNull final Context context,
                                                    final int alarmId) {
        assertNotNull(context, "context");
        assertNotNull(alarmId, "alarmId");

        final Bundle result = new Bundle();
        result.putInt(BUNDLE_EXTRA_INT_VERSION_CODE, AppBuildInfo.getVersionCode(context));
        result.putInt(BUNDLE_EXTRA_INT_ACTION, Actions.DISABLE.getValue());
        result.putInt(BUNDLE_EXTRA_INT_ALARM_ID, alarmId);

        return result;
    }

    /**
     * @param context Application context.
     * @param alarmId The ID of the alarm to disable
     * @return A plug-in bundle.
     */
    @NonNull
    public static Bundle generateModifyAlarmBundle(@NonNull final Context context,
                                                   final int alarmId,
                                                   final String time,
                                                   final boolean enabled) {
        assertNotNull(context, "context");
        assertNotNull(alarmId, "alarmId");
        assertNotNull(time, "time");

        final Bundle result = new Bundle();
        result.putInt(BUNDLE_EXTRA_INT_VERSION_CODE, AppBuildInfo.getVersionCode(context));
        result.putInt(BUNDLE_EXTRA_INT_ACTION, Actions.MODIFY.getValue());
        result.putInt(BUNDLE_EXTRA_INT_ALARM_ID, alarmId);
        result.putString(BUNDLE_EXTRA_STRING_TIME, time);
        result.putBoolean(BUNDLE_EXTRA_BOOLEAN_ENABLED, enabled);

        return result;
    }

    /**
     * @param bundle A valid plug-in bundle.
     * @return The action inside the plug-in bundle.
     */
    public static int getAction(@NonNull final Bundle bundle) {
        return bundle.getInt(BUNDLE_EXTRA_INT_ACTION);
    }
}
