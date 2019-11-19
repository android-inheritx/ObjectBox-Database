package com.example.objectbox

import android.app.Application
import com.example.objectbox.database.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log


class MyApplication : Application() {
    companion object {
        private var sApp: MyApplication? = null
        private var mBoxStore: BoxStore? = null

        fun getApp(): MyApplication? {
            return sApp
        }
    }

    override fun onCreate() {
        super.onCreate()
        sApp = this
        mBoxStore = MyObjectBox.builder().androidContext(this@MyApplication).build()

        if (BuildConfig.DEBUG) {
            val started = AndroidObjectBrowser(mBoxStore).start(this)
            Log.i("ObjectBrowser", "Started: $started")
        }

    }

    fun getBoxStore(): BoxStore? {
        return mBoxStore
    }
}