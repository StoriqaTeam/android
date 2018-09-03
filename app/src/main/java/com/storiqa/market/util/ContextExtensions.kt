package com.storiqa.market.util

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat

fun Context.getThemedColor(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}