package com.goodthings.app.util

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.goodthings.app.application.Consts
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * 图片压缩工具类
 */
class CompressPhotoUtils {

    private val fileList = ArrayList<String>()
    private var progressDialog: ProgressDialog? = null

    fun CompressPhoto(context: Context, list: List<String>, callBack: CompressCallBack) {
        val task = CompressTask(context, list, callBack)
        task.execute()
    }

    internal inner class CompressTask(private val context: Context, private val list: List<String>, private val callBack: CompressCallBack) : AsyncTask<Void, Int, Int>() {

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        override fun onPreExecute() {
            progressDialog = ProgressDialog.show(context, null, "处理中...")
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        override fun doInBackground(vararg params: Void): Int? {
            for (i in list.indices) {
                val bitmap = getBitmap(list[i])
                val path = SaveBitmap(bitmap, i)
                fileList.add(path!!)
            }
            return null
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        override fun onPostExecute(integer: Int?) {
            progressDialog!!.dismiss()
            callBack.success(fileList)
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        override fun onProgressUpdate(vararg values: Int?) {}
    }

    interface CompressCallBack {
        fun success(list: List<String>)
    }

    companion object {

        /**
         * 从sd卡获取压缩图片bitmap
         */
        fun getBitmap(srcPath: String): Bitmap {
            val newOpts = BitmapFactory.Options()
            newOpts.inJustDecodeBounds = true
            var bitmap = BitmapFactory.decodeFile(srcPath, newOpts)
            newOpts.inJustDecodeBounds = false
            val w = newOpts.outWidth
            val h = newOpts.outHeight
            val hh = 1280f
            val ww = 720f
            // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            var be = 1// be=1表示不缩放
            if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
                be = (newOpts.outWidth / ww).toInt()
            } else if (w < h && h > hh) {// 如果高度高的话根据高度固定大小缩放
                be = (newOpts.outHeight / hh).toInt()
            }
            newOpts.inSampleSize = be// 设置缩放比例
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts)
            return bitmap
        }

        /**
         * 保存bitmap到内存卡
         */
        fun SaveBitmap(bmp: Bitmap, num: Int): String? {
            val file = File(Consts.SAVEPICPATH)
            var path: String? = null
            if (!file.exists())
                file.mkdirs()
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")
                val picName = formatter.format(Date())
                path = file.getPath() + "/" + picName + "-" + num + ".jpg"
                val fileOutputStream = FileOutputStream(path!!)
                //bmp.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                val baos = ByteArrayOutputStream()
                var options_ = 100
                val maxFileSize = 100
                bmp.compress(Bitmap.CompressFormat.JPEG, options_, baos)
                //质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
                var baosLength = baos.toByteArray().size
                while (baosLength / 1024 > maxFileSize) {
                    //循环判断如果压缩后图片是否大于maxMemmorrySize,大于继续压缩
                    baos.reset()
                    //重置baos即让下一次的写入覆盖之前的内容
                    options_ = Math.max(0, options_ - 10)//图片质量每次减少10
                    bmp.compress(Bitmap.CompressFormat.JPEG, options_, baos)
                    //将压缩后的图片保存到baos中
                    baosLength = baos.toByteArray().size
                    if (options_ == 0)
                    //如果图片的质量已降到最低则，不再进行压缩
                        break
                }
                baos.flush()
                baos.close()
                baos.writeTo(fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return path
        }
    }

}


