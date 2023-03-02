package com.jimzrt.umsmounter.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object SharedPrefsHelper {
    private lateinit var mSharedPref: SharedPreferences
    const val EXAMPLE_TAG = "NAME"
    const val IS_FIRST_RUN = "IS_FIRST_RUN"
    const val VERSION = "VERSION"
    const val HAS_RW_EXTERNAL_PERMISSION = "WRITE_EXTERNAL_PERMISSION"
    const val USER_PATH = "USER_PATH"
    const val HAS_ROOT_PERMISSIONS = "HAS_ROOT_PERMISSIONS"


    fun init(context: Context) {
        if (!::mSharedPref.isInitialized)
            mSharedPref = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    fun read(key: String, defValue: String): String? {
        return mSharedPref.getString(key, defValue)
    }

    fun write(key: String, value: String) {
        val prefsEditor = mSharedPref.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun read(key: String, defValue: Boolean): Boolean {
        return mSharedPref.getBoolean(key, defValue)
    }

    fun write(key: String, value: Boolean) {
        val prefsEditor = mSharedPref.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.apply()
    }

    fun read(key: String, defValue: Int): Int {
        return mSharedPref.getInt(key, defValue)
    }

    fun write(key: String, value: Int) {
        val prefsEditor = mSharedPref.edit()
        prefsEditor.putInt(key, value)
        prefsEditor.apply()
    }

    fun clear() {
        mSharedPref.edit().clear()
    }
}