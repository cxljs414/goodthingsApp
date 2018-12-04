package com.goodthings.app.util

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.amap.api.services.core.AMapException

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/21
 * 修改内容：
 * 最后修改时间：
 */
object AMapUtil {

    fun checkEditText(editText: EditText?):String{
        return if(editText != null && editText.text != null
                       && editText.text.toString().trim { it <= ' ' } != ""){
            editText.text.toString().trim { it <= ' ' }
        }else{
            ""
        }
    }

    fun isEmptyOrNullString(s:String):Boolean{
        return (s == null ) || (s.trim().length == 0)
    }

    fun showError(context: Context,rCode:Int){
        try {
            when(rCode){
                1001 -> throw AMapException(AMapException.AMAP_SIGNATURE_ERROR)
                1002 -> throw AMapException(AMapException.AMAP_INVALID_USER_KEY)
                1003 -> throw AMapException(AMapException.AMAP_SERVICE_NOT_AVAILBALE)
                1004 -> throw AMapException(AMapException.AMAP_DAILY_QUERY_OVER_LIMIT)
                1005 -> throw AMapException(AMapException.AMAP_ACCESS_TOO_FREQUENT)
                1006->throw  AMapException(AMapException.AMAP_INVALID_USER_IP)
                1007->
                throw  AMapException(AMapException.AMAP_INVALID_USER_DOMAIN)
                 1008->
                throw  AMapException(AMapException.AMAP_INVALID_USER_SCODE)
                 1009->
                throw  AMapException(AMapException.AMAP_USERKEY_PLAT_NOMATCH)
                 1010->
                throw  AMapException(AMapException.AMAP_IP_QUERY_OVER_LIMIT)
                 1011->
                throw  AMapException(AMapException.AMAP_NOT_SUPPORT_HTTPS)
                 1012->
                throw  AMapException(AMapException.AMAP_INSUFFICIENT_PRIVILEGES)
                 1013->
                throw  AMapException(AMapException.AMAP_USER_KEY_RECYCLED)
                 1100->
                throw  AMapException(AMapException.AMAP_ENGINE_RESPONSE_ERROR)
                 1101->
                throw  AMapException(AMapException.AMAP_ENGINE_RESPONSE_DATA_ERROR)
                 1102->
                throw  AMapException(AMapException.AMAP_ENGINE_CONNECT_TIMEOUT)
                 1103->
                throw  AMapException(AMapException.AMAP_ENGINE_RETURN_TIMEOUT)
                 1200->
                throw  AMapException(AMapException.AMAP_SERVICE_INVALID_PARAMS)
                 1201->
                throw  AMapException(AMapException.AMAP_SERVICE_MISSING_REQUIRED_PARAMS)
                 1202->
                throw  AMapException(AMapException.AMAP_SERVICE_ILLEGAL_REQUEST)
                 1203->
                throw  AMapException(AMapException.AMAP_SERVICE_UNKNOWN_ERROR)
            //sdk返回错误
                 1800->
                throw  AMapException(AMapException.AMAP_CLIENT_ERRORCODE_MISSSING)
                 1801->
                throw  AMapException(AMapException.AMAP_CLIENT_ERROR_PROTOCOL)
                 1802->
                throw  AMapException(AMapException.AMAP_CLIENT_SOCKET_TIMEOUT_EXCEPTION)
                 1803->
                throw  AMapException(AMapException.AMAP_CLIENT_URL_EXCEPTION)
                 1804->
                throw  AMapException(AMapException.AMAP_CLIENT_UNKNOWHOST_EXCEPTION)
                 1806->
                throw  AMapException(AMapException.AMAP_CLIENT_NETWORK_EXCEPTION)
                 1900->
                throw  AMapException(AMapException.AMAP_CLIENT_UNKNOWN_ERROR)
                 1901->
                throw  AMapException(AMapException.AMAP_CLIENT_INVALID_PARAMETER)
                 1902->
                throw  AMapException(AMapException.AMAP_CLIENT_IO_EXCEPTION)
                 1903->
                throw  AMapException(AMapException.AMAP_CLIENT_NULLPOINT_EXCEPTION)
            //云图和附近错误码  
                 2000->
                throw  AMapException(AMapException.AMAP_SERVICE_TABLEID_NOT_EXIST)
                 2001->
                throw  AMapException(AMapException.AMAP_ID_NOT_EXIST)
                 2002->
                throw  AMapException(AMapException.AMAP_SERVICE_MAINTENANCE)
                 2003->
                throw  AMapException(AMapException.AMAP_ENGINE_TABLEID_NOT_EXIST)
                 2100->
                throw  AMapException(AMapException.AMAP_NEARBY_INVALID_USERID)
                 2101->
                throw  AMapException(AMapException.AMAP_NEARBY_KEY_NOT_BIND)
                 2200->
                throw  AMapException(AMapException.AMAP_CLIENT_UPLOADAUTO_STARTED_ERROR)
                 2201->
                throw  AMapException(AMapException.AMAP_CLIENT_USERID_ILLEGAL)
                 2202->
                throw  AMapException(AMapException.AMAP_CLIENT_NEARBY_NULL_RESULT)
                 2203->
                throw  AMapException(AMapException.AMAP_CLIENT_UPLOAD_TOO_FREQUENT)
                 2204->
                throw  AMapException(AMapException.AMAP_CLIENT_UPLOAD_LOCATION_ERROR)
            //路径规划   
                 3000->
                throw  AMapException(AMapException.AMAP_ROUTE_OUT_OF_SERVICE)
                 3001->
                throw  AMapException(AMapException.AMAP_ROUTE_NO_ROADS_NEARBY)
                 3002->
                throw  AMapException(AMapException.AMAP_ROUTE_FAIL)
                 3003->
                throw  AMapException(AMapException.AMAP_OVER_DIRECTION_RANGE)
            //短传分享
                 4000->
                throw  AMapException(AMapException.AMAP_SHARE_LICENSE_IS_EXPIRED)
                 4001->
                throw  AMapException(AMapException.AMAP_SHARE_FAILURE)
            }
        }catch (e:Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}