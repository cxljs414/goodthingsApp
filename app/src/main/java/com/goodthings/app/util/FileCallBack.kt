package com.goodthings.app.util

import android.util.Log

import com.goodthings.app.bean.FileLoadEvent

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

import okhttp3.ResponseBody
import rx.Subscription
import rx.functions.Action1

/**
 * Created by miya95 on 2016/12/5.
 */
abstract class FileCallBack<T>(private val destFileDir: String, private val destFileName: String) {

    init {
        subscribeLoadProgress()
    }

    abstract fun onSuccess(t: T)

    abstract fun progress(progress: Long, total: Long)

    abstract fun onStart()

    abstract fun onCompleted()

    abstract fun onError(e: Throwable)

    fun saveFile(body: ResponseBody) {
        var inputs: InputStream? = null
        val buf = ByteArray(2048)
        var len: Int
        var fos: FileOutputStream? = null
        try {
            inputs = body.byteStream()
            val dir = File(destFileDir)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val file = File(dir, destFileName)
            fos = FileOutputStream(file)
            len = inputs!!.read(buf)
            while (len != -1) {
                fos.write(buf, 0, len)
                len = inputs!!.read(buf)
            }
            fos.flush()
            unsubscribe()
            //onCompleted();
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (inputs != null) inputs.close()
                if (fos != null) fos.close()
            } catch (e: IOException) {
                Log.e("saveFile", e.message)
            }

        }
    }

    /**
     * 订阅加载的进度条
     */
    fun subscribeLoadProgress() {
        val subscription = RxBus.getInstance().doSubscribe(FileLoadEvent::class.java, { fileLoadEvent -> progress(fileLoadEvent.bytesLoaded, fileLoadEvent.total) }) {
            //TODO 对异常的处理
        }
        RxBus.getInstance().addSubscription(this, subscription)
    }

    /**
     * 取消订阅，防止内存泄漏
     */
    fun unsubscribe() {
        RxBus.getInstance().unSubscribe(this)
    }

}
