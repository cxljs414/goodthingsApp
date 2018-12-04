package com.goodthings.app.util

import android.graphics.Bitmap
import com.goodthings.app.application.Consts
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/12
 * 修改内容：
 * 最后修改时间：
 */
object FileUtil {

    fun saveBitmap(bmp: Bitmap){
        saveBitmap(bmp,false,0)
    }

    /**
     * 保存bitmap到内存卡
     */
    fun saveBitmap(bmp: Bitmap,isCompress:Boolean,quelity:Int): String? {
        val file = File(Consts.APPIMAGEPATH)
        var path: String? = null
        if (!file.exists())
            file.mkdirs()
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")
            val picName = formatter.format(Date())
            path = file.path + "/hsfs_share_" + picName + ".jpg"
            val fileOutputStream = FileOutputStream(path!!)
            val baos = ByteArrayOutputStream()
            if(!isCompress){
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            }else{
                var options_ = 100
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                //质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
                var baosLength = baos.toByteArray().size
                while (baosLength / 1024 > quelity) {
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