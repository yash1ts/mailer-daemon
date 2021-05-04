package com.mailerdaemon.app

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

fun Context.getDrawableCompat(id: Int) =
    ContextCompat.getDrawable(this, id)

fun Context.getColorCompat(id: Int) =
    ContextCompat.getColor(this, id)
