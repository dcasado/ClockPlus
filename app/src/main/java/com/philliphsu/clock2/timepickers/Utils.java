/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.philliphsu.clock2.timepickers;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

/**
 * Utility helper functions for time and date pickers.
 */
public class Utils {

    public static int getTextColorFromThemeAttr(Context context, int resid) {
        // http://stackoverflow.com/a/33839580/5055032
//        final TypedValue value = new TypedValue();
//        context.getTheme().resolveAttribute(resid, value, true);
//        TypedArray a = context.obtainStyledAttributes(value.data,
//                new int[] {resid});
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{resid});
        final int color = a.getColor(0/*index*/, 0/*defValue*/);
        a.recycle();
        return color;
        // Didn't work! Gave me white!
//        return getColorFromThemeAttr(context, android.R.attr.textColorPrimary);
    }

    /**
     * Mutates the given drawable, applies the specified tint list, and sets this tinted
     * drawable on the target ImageView.
     *
     * @param target   the ImageView that should have the tinted drawable set on
     * @param drawable the drawable to tint
     * @param tintList Color state list to use for tinting this drawable, or null to clear the tint
     */
    public static void setTintList(ImageView target, Drawable drawable, ColorStateList tintList) {
        // TODO: What is the VectorDrawable counterpart for this process?
        // Use a mutable instance of the drawable, so we only affect this instance.
        // This is especially useful when you need to modify properties of drawables loaded from
        // resources. By default, all drawables instances loaded from the same resource share a
        // common state; if you modify the state of one instance, all the other instances will
        // receive the same modification.
        // Wrap drawable so that it may be used for tinting across the different
        // API levels, via the tinting methods in this class.
        drawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintList(drawable, tintList);
        target.setImageDrawable(drawable);
    }

    public static void setTint(Drawable drawable, @ColorInt int color) {
        drawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTint(drawable, color);
    }

    /**
     * Returns a tinted drawable from the given drawable resource, if {@code tintList != null}.
     * Otherwise, no tint will be applied.
     */
    public static Drawable getTintedDrawable(@NonNull Context context,
                                             @DrawableRes int drawableRes,
                                             @Nullable ColorStateList tintList) {
        Drawable d = DrawableCompat.wrap(ContextCompat.getDrawable(context, drawableRes).mutate());
        DrawableCompat.setTintList(d, tintList);
        return d;
    }
}
