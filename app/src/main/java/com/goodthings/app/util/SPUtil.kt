package com.goodthings.app.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.goodthings.app.bean.User
import com.google.gson.Gson

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/1/8
 * 修改内容：
 * 最后修改时间：
 */
object SPUtil{

    fun putBooleanValue(context: Context, key: String, bl: Boolean) {
        val edit = getSharedPreference(context).edit()
        edit.putBoolean(key, bl)
        edit.commit()
    }

    private fun getSharedPreference(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getBooleanValue(context: Context, key: String): Boolean {
        return getSharedPreference(context).getBoolean(key, false)
    }

    fun getUserBean(context: Context): User? {
        val gson = Gson()
        return gson.fromJson(getSharedPreference(context).getString("user",""),User::class.java)
    }

    fun saveUserBean(context: Context?,user: User?){
        val edit = getSharedPreference(context!!).edit()
        edit.putString("user", Gson().toJson(user))
        edit.commit()
    }

    fun removeUserBean(context: Context?){
        val edit = getSharedPreference(context!!).edit()
        edit.remove("user")
        edit.commit()
    }

    fun putStringValue(context: Context, key: String, value: String) {
        val edit = getSharedPreference(context).edit()
        edit.putString(key, value)
        edit.commit()
    }

    fun getStringValue(context: Context,key:String): String {
        return getSharedPreference(context).getString(key,"")
    }

    fun getStringValue(context: Context,key:String,default:String): String {
        return getSharedPreference(context).getString(key,default)
    }

    fun getIntValue(context: Context,key:String): Int {
        return getSharedPreference(context).getInt(key,0)
    }

    fun getIntValue(context: Context,key:String,defaultValue:Int): Int {
        return getSharedPreference(context).getInt(key,defaultValue)
    }

    fun putIntValue(context: Context, key: String, value: Int) {
        val edit = getSharedPreference(context).edit()
        edit.putInt(key, value)
        edit.commit()
    }

    fun removeAll(context: Context) {
        val edit = getSharedPreference(context!!).edit()
        edit.clear()
        edit.commit()
    }
}