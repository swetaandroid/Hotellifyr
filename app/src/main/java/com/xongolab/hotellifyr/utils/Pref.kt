package com.xongolab.hotellifyr.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xongolab.hotellifyr.core.MyApplication
import org.json.JSONArray

object Pref {

    private var sharedPreferences: SharedPreferences? = null

    val PREF_FILE: String = "HOTELLIFYR_PREF"
    private fun openPreference() {

        sharedPreferences = MyApplication.instance.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

    }

    /*Fore String Value Store*/
    fun getStringValue(key: String, defaultValue: String): String {
        openPreference()
        val result = sharedPreferences!!.getString(key, defaultValue).toString()
        sharedPreferences = null
        return result
    }

    fun setStringValue(key: String, value: String) {
        openPreference()
        val prefsPrivateEditor: SharedPreferences.Editor? = sharedPreferences!!.edit()
        prefsPrivateEditor!!.putString(key, value)
        prefsPrivateEditor.apply()
        sharedPreferences = null
    }


    /*For Integer Value*/
    fun setIntValue(key: String, value: Int) {
        openPreference()
        val prefsPrivateEditor: SharedPreferences.Editor? = sharedPreferences!!.edit()
        prefsPrivateEditor!!.putInt(key, value)
        prefsPrivateEditor.apply()
        sharedPreferences = null
    }

    fun getIntValue(key: String, defaultValue: Int): Int {
        openPreference()
        val result = sharedPreferences!!.getInt(key, defaultValue)
        sharedPreferences = null
        return result
    }

    /*For boolean Value Store*/
    fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        openPreference()
        val result = sharedPreferences!!.getBoolean(key, defaultValue)
        sharedPreferences = null
        return result
    }

    fun setBooleanValue(key: String, value: Boolean) {
        openPreference()
        val prefsPrivateEditor: SharedPreferences.Editor? = sharedPreferences!!.edit()
        prefsPrivateEditor!!.putBoolean(key, value)
        prefsPrivateEditor.apply()
    }

    /* For String ArrayList */
    /*fun getStringList(key: String): ArrayList<String> {
        openPreference()
        val result = sharedPreferences!!.getStringSet(key, HashSet()) ?: HashSet()
        sharedPreferences = null
        return ArrayList(result)
    }*/

    fun getStringList(key: String): ArrayList<String> {
        openPreference()
        val json = sharedPreferences?.getString(key, null) ?: return ArrayList()
        val jsonArray = JSONArray(json)
        val list = ArrayList<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }
        return list
    }

 /*   fun setStringList(key: String, list: ArrayList<String>) {
        openPreference()
        val prefsPrivateEditor: SharedPreferences.Editor? = sharedPreferences!!.edit()
        prefsPrivateEditor!!.putStringSet(key, HashSet(list))
        prefsPrivateEditor!!.apply()
    }
*/
    fun setStringList(key: String, list: ArrayList<String>) {
        openPreference()
         val uniqueList = list.distinct() // removes duplicates while preserving order
         val jsonArray = JSONArray()
        uniqueList.forEach { jsonArray.put(it) }
        sharedPreferences?.edit()?.putString(key, jsonArray.toString())?.apply()
    }

    fun clearStringList(key: String) {
        openPreference()
        val prefsPrivateEditor: SharedPreferences.Editor? = sharedPreferences!!.edit()
        prefsPrivateEditor!!.remove(key) // Remove the stored list
        prefsPrivateEditor!!.apply()
    }

    /*For Remove variable from pref*/
    fun remove(key: String) {
        openPreference()
        val prefsPrivateEditor = sharedPreferences!!.edit()
        prefsPrivateEditor.remove(key)
        prefsPrivateEditor.apply()
        sharedPreferences = null
    }

    /*For Remove variable from pref*/
    fun clearAllPref() {
        remove(PREF_IS_LOGIN)
        remove(PREF_AUTH_TOKEN)
        remove(PREF_USER_ID)
        remove(PREF_PROFILE_PIC)
        remove(PREF_FIRST_NAME)
        remove(PREF_LAST_NAME)
    }

    const val PREF_IS_LOGIN: String = "PREF_IS_LOGIN"
    const val PREF_AUTH_TOKEN: String = "PREF_AUTH_TOKEN"
    const val PREF_DEVICE_TOKEN: String = "PREF_DEVICE_TOKEN"
    const val PREF_LANGUAGE: String = "PREF_LANGUAGE"

    const val PREF_USER_ID: String = "PREF_USER_ID"
    const val PREF_FIRST_NAME: String = "PREF_FIRST_NAME"
    const val PREF_LAST_NAME: String = "PREF_LAST_NAME"
    const val PREF_PROFILE_PIC: String = "PREF_PROFILE_PIC"
    const val PREF_EMAIL: String = "PREF_EMAIL"
    const val PREF_COUNTRY_CODE: String = "PREF_COUNTRY_CODE"
    const val PREF_MOBILE_NO: String = "PREF_MOBILE_NO"
    const val PREF_SUPPORT_EMAIL: String = "PREF_SUPPORT_EMAIL"
    const val PREF_SUPPORT_CALL: String = "PREF_SUPPORT_CALL"
    const val PREF_CURRENT_LAT: String = "PREF_CURRENT_LAT"
    const val PREF_CURRENT_LONG: String = "PREF_CURRENT_LONG"

    const val PREF_CURRENCY: String = "PREF_CURRENCY"
    const val PREF_MY_PREFERENCES: String = "PREF_MY_PREFERENCES"

}