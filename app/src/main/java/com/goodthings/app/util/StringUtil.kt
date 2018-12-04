package com.goodthings.app.util

import android.text.TextUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/26
 * 修改内容：
 * 最后修改时间：
 */
object StringUtil {

    fun isJson(target: String): Boolean {
        if (TextUtils.isEmpty(target)) {
            return false
        }
        var tag = false
        try {
            if (target.startsWith("[")) {
                JSONArray(target)
            } else {
                JSONObject(target)
            }
            tag = true
        } catch (ignore: JSONException) {
            //            ignore.printStackTrace();
            tag = false
        }

        return tag
    }
}