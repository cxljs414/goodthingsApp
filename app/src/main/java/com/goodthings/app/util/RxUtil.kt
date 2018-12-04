package com.goodthings.app.util

import android.util.Log
import com.goodthings.app.bean.BaseResult
import com.goodthings.app.bean.CommonResult
import com.google.gson.JsonParseException
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.text.ParseException


/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/13
 * 修改内容：
 * 最后修改时间：
 */
object RxUtil {

    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504

    fun <T> hanlderBaseResult(): FlowableTransformer<BaseResult<T>, T> {
        return FlowableTransformer { flowable ->
            flowable.map(HandlerFun())
                    .onErrorResumeNext(ErrorHandlerFun())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun  hanlderCommResult(): FlowableTransformer<CommonResult, CommonResult> {
        return FlowableTransformer { flowable ->
            flowable.map(HandlerComFun())
                    .onErrorResumeNext(ErrorHandlerFun())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    class HandlerFun<T> : Function<BaseResult<T>,T>{
        override fun apply(result: BaseResult<T>?): T? {
            Log.i("RxUtil",""+result?.code+":"+result?.msg)
            if(result?.code != 2000 ){
                throw ServerException(result?.code!!,result?.msg)
            }
            return result?.data
        }
    }

    class HandlerComFun : Function<CommonResult,CommonResult>{
        override fun apply(result: CommonResult?): CommonResult? {
            if(result?.code != 2000){
                throw ServerException(result?.code!!,result?.msg)
            }
            return result
        }
    }

    class ErrorHandlerFun<T> : Function<Throwable,Flowable<T>>{
        override fun apply(t: Throwable?): Flowable<T> {
            Log.i("RxUtil","RxUtil error:"+t?.message)
            return Flowable.error(handleException(t!!))
        }

        private fun handleException(e:Throwable ):Throwable{
            if (e is HttpException) {
                return when (e.code()) {
                    UNAUTHORIZED, FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT,
                    INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE -> Throwable("网络错误")
                    else -> {
                        Throwable("网络错误")
                    }
                }
            } else if (e is ServerException) {
                return Throwable(e.message)
            } else if (e is JsonParseException
                    || e is JSONException
                    || e is ParseException) {
                return Throwable("解析错误")
            } else if (e is ConnectException) {
                return Throwable("连接失败")
            } else if (e is javax.net.ssl.SSLHandshakeException) {
                return Throwable("证书验证失败")
            } else if (e is ConnectTimeoutException) {
                return Throwable("连接超时")
            } else if (e is java.net.SocketTimeoutException) {
                return Throwable("连接超时")
            } else {
                return Throwable("未知错误")
            }
        }
    }

    class ServerException(var code:Int= 0, override var message:String? = null) : RuntimeException() {
    }
}