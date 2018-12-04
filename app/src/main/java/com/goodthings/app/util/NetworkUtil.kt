package com.goodthings.app.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/25
 * 修改内容：
 * 最后修改时间：
 */
class NetworkUtil(val context: Context) {

    /**
     * 当前WIFI是否可用
     */
    val WIFI_AVAILABLE: Boolean
        get() {
            val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connManager.activeNetworkInfo
            return (info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI)
        }
    /**
     * 当前网络是否可用
     */
    val NETWORK_ENABLE: Boolean
        get() {
            try {
                val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val info = connManager.activeNetworkInfo
                return info.state == NetworkInfo.State.CONNECTED
            } catch (e:Exception){
                return false
            }
        }

    /**
     * 手机管理器
     */
    val TELEPHONY_MANAGER = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    /**
     * WiFi管理器
     */
    val WIFI_MANAGER = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    /**
     * 连接活动管理器
     */
    val CONNECTIVITY_MANAGER = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * 中国运营商的名字
     */
    val PROVIDER: String
        @SuppressLint("MissingPermission")
        get() {
            val IMSI = TELEPHONY_MANAGER.subscriberId
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == TELEPHONY_MANAGER.simState) {
                    val operator = TELEPHONY_MANAGER.simOperator
                    if (operator != null) {
                        if (operator.equals("464000") || operator.equals("464002") || operator.equals("464007")) {
                            return "中国移动"
                        } else if (operator.equals("464001")) {
                            return "中国联通"
                        } else if (operator.equals("464003")) {
                            return "中国电信"
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
                        || IMSI.startsWith("46007")) {
                    return "中国移动"
                } else if (IMSI.startsWith("46001")) {
                    return "中国联通"
                } else if (IMSI.startsWith("46003")) {
                    return "中国电信"
                }
            }
            return "UnKnown"
        }

    /**
     * 当前网络的连接类型
     */
    val NET_WORK_CLASS: Int?
        get() {
            val netWorkInfo = CONNECTIVITY_MANAGER.activeNetworkInfo
            if (netWorkInfo != null && netWorkInfo.isAvailable && netWorkInfo.isConnected) {
                return netWorkInfo.type
            } else {
                return null
            }
        }

    /**
     * 当前网络的连接类型
     */
    val NET_WORK_TYPE: NetworkType
        get() {
            if (WIFI_AVAILABLE) return NetworkType.WIFI
            return when (NET_WORK_CLASS) {
                TelephonyManager.NETWORK_TYPE_GPRS,
                TelephonyManager.NETWORK_TYPE_EDGE,
                TelephonyManager.NETWORK_TYPE_CDMA,
                TelephonyManager.NETWORK_TYPE_1xRTT,
                TelephonyManager.NETWORK_TYPE_IDEN -> {
                    NetworkType.DATA2G
                }

                TelephonyManager.NETWORK_TYPE_UMTS,
                TelephonyManager.NETWORK_TYPE_EVDO_0,
                TelephonyManager.NETWORK_TYPE_EVDO_A,
                TelephonyManager.NETWORK_TYPE_HSDPA,
                TelephonyManager.NETWORK_TYPE_HSUPA,
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_EVDO_B,
                TelephonyManager.NETWORK_TYPE_EHRPD,
                TelephonyManager.NETWORK_TYPE_HSPAP -> {
                    NetworkType.DATA3G
                }

                TelephonyManager.NETWORK_TYPE_LTE -> {
                    NetworkType.DATA4G
                }

                else -> {
                    NetworkType.UNKNOWN
                }
            }
        }

    /**
     * 当前WiFi信号强度,单位"dBm"
     */
    val WIFI_RSSI: Int
        get() {
            if (NET_WORK_TYPE == NetworkType.WIFI) {
                val wifiInfo = WIFI_MANAGER.connectionInfo
                return if (wifiInfo == null) 0 else wifiInfo.rssi
            }
            return 0
        }

    /**
     * 获得连接WiFi的名称
     */
    val WIFI_SSID: String?
        get() {
            if (NET_WORK_TYPE == NetworkType.WIFI) {
                val wifiInfo = WIFI_MANAGER.connectionInfo
                return wifiInfo?.ssid
            }
            return null
        }

    /**
     * 当前SIM卡状态
     */
    val SIM_STATE_NOMAL: Boolean
            = !(TELEPHONY_MANAGER.simState == TelephonyManager.SIM_STATE_ABSENT
            || TELEPHONY_MANAGER.simState == TelephonyManager.SIM_STATE_UNKNOWN)

}

enum class NetworkType {
    UNKNOWN,
    DISCONNECTION,
    WIFI,
    DATA2G,
    DATA3G,
    DATA4G,
    DATA5G
}