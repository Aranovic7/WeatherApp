package com.example.weatherapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


fun <T : Any> Activity.startNewActivity(mClass: Class<T>, finish: Boolean) {
    startActivity(Intent(this, mClass))
    if (finish) {
        finish()
    }
}

fun <T : Any> Activity.startNewActivity(
    mClass: Class<T>,
    finish: Boolean,
    values: (Intent) -> Unit
) {
    startActivity(Intent(this, mClass).also {
        values(it)
    })
    if (finish) {
        finish()
    }
}

fun ImageView.setImage(imageId: Any?) {
    Glide.with(this).load(imageId).into(this)
}

fun ImageView.setImage(imageId: Any?, placeHolder: Any?) {
    Glide.with(this).load(imageId).into(this)
}

fun Context.showToast(message: String?) {
    message?.let {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }
}

fun View.beGone() {
    this.visibility = View.GONE
}

fun View.beInvisible() {
    this.visibility = View.INVISIBLE
}


fun View.beVisible() {
    this.visibility = View.VISIBLE
}

fun View.showSnackBar(message: String, lenght: Int, actionText: String, actionWork: () -> Unit) {
    val sb = Snackbar.make(this, message, lenght)
    sb.setAction(actionText) { v ->
        actionWork()
    }
    sb.show()
}

fun View.showSnackBar(message: String, lenght: Int) {
    val sb = Snackbar.make(this, message, lenght)

    sb.show()
}


fun String.showLog(message: String?) {
    message?.let {
        Log.d(this, it)
    }
}

fun Context.getCityName(coordinates: Pair<Double, Double>): MutableList<Address>? {
    val geocoder = Geocoder(this, Locale.getDefault())
    return geocoder.getFromLocation(coordinates.first, coordinates.second, 1)
}

fun Long.getTime(): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(this * 1000))
}

fun Long.getTimeDate(): String {
    return SimpleDateFormat("EEE MMM dd, yyyy hh:mm a").format(Date(this * 1000))
}

fun Long.getDate(): String {
    return SimpleDateFormat("EEE MMM dd, yyyy").format(Date(this * 1000))
}
fun Long.getDayDate(): String {
    return SimpleDateFormat("dd").format(Date(this * 1000))
}
fun Long.getDay(): String {
    val outFormat = SimpleDateFormat("EEE")
    val goal = outFormat.format(Date(this * 1000))
    return goal
}

fun Float.getCelsiusTemperature(): String {
    return (this - 273.15).toInt().toString() + Constant.C
}

var bottomAdCount: Int = 0








