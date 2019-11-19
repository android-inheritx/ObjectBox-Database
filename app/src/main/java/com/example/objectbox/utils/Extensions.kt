package com.example.objectbox.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Returns Zero (0) if it is null
 */
fun Number?.orZero(): Number = this ?: 0

/**
 * Returns Zero (0) if it is null
 */
fun Int?.orZero(): Int = this ?: 0

/**
 * Returns Zero (0) if it is null
 */
fun Long?.orZero(): Long = this ?: 0

/**
 * Returns Zero (0) if it is null
 */
fun Float?.orZero(): Float = this ?: 0.0f

/**
 * Returns Zero (0) if it is null
 */
fun Double?.orZero(): Double = this ?: 0.0

/**
 * Returns false if it is null
 */
fun Boolean?.orFalse(): Boolean = this ?: false


/**
 * Checks if a string is a valid email
 */
fun String.isEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()


/**
 * Returns class name. Useful for Log Tags
 */
val Any.TAG: String
    get() = this::class.java.simpleName

/**
 * Sets the view's visibility to GONE
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * Sets the view's visibility to VISIBLE
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Sets the view's visibility to INVISIBLE
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Hides the soft keyboard
 */
fun Activity.hideKeyboard(): Boolean {
    val view = currentFocus
    view?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
    return false
}

/**
 * Logs current object as Debug
 */
fun Any.logD(tag: String = TAG) = Log.d(tag, toString())

fun Context?.toastNow(msg: String, duration: Int = Toast.LENGTH_LONG) = cancelAndMakeToast(this, msg, duration)
fun Fragment?.toastNow(msg: String) = cancelAndMakeToast(this?.context, msg, Toast.LENGTH_LONG)

private fun cancelAndMakeToast(ctx: Context?, msg: String, duration: Int): Toast? {
    ToastQueue.cancelToasts()
    return makeToast(ctx, msg, duration)
}

private object ToastQueue {
    val toastQueue = mutableListOf<Toast>()

    fun cancelToasts() {
        toastQueue.forEach { it.cancel() }
        toastQueue.clear()
    }

    fun removeToast(toast: Toast) = toastQueue.remove(toast)

}

private fun makeToast(ctx: Context?, msg: String, duration: Int): Toast? {
    return ctx?.let {
        val toast = Toast.makeText(ctx, msg, duration)
        toast.show()
        // remove from list after 4 seconds (longest toast time is 3.5 seconds)
        toast.view?.postDelayed({
            ToastQueue.removeToast(toast)
        }, 4000L)
        ToastQueue.toastQueue.add(toast)
        toast
    }
}