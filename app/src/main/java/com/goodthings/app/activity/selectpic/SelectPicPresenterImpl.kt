package com.goodthings.app.activity.selectpic

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import com.goodthings.app.activity.CropActivity
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.util.CompressPhotoUtils
import com.goodthings.app.application.Consts
import com.kevin.crop.UCrop
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/2/8
 * 修改内容：
 * 最后修改时间：
 */
class SelectPicPresenterImpl : BasePresenterImpl<SelectPicContract.SelectPicView>(),SelectPicContract.SelectPicPresenter{
    private var mImagePath: String? = ""
    private var cropWidth:Int = 512
    private var cropHeight:Int = 512
    override fun afterAttachView() {
        super.afterAttachView()
        cropWidth = mView?.getIntent()!!.getIntExtra("width",512)
        cropHeight = mView?.getIntent()!!.getIntExtra("height",512)
    }
    override fun takePhoto(isTakePhoto: Boolean) {
        if(isTakePhoto){
            val sdState:String = Environment.getExternalStorageState()
            if(sdState == Environment.MEDIA_MOUNTED){

                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,getTakePhotoUri())
                mView?.getActivity()?.startActivityForResult(intent, Consts.SELECT_PIC_BY_TACK_PHOTO)
            } else{
                mView?.showError("内存卡不存在")
            }
        }else{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            mView?.getActivity()?.startActivityForResult(intent, Consts.SELECT_PIC_BY_PICK_PHOTO)
        }
    }

    private fun getTakePhotoUri(): Uri? {
        var imageFilePath:String = mView?.getContext()?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath!!
        val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.CANADA)
        val imageFile= File(imageFilePath+"/${formatter.format(Date())}.jpg")
        mImagePath = imageFile.absolutePath
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(mView?.getContext()!!, mView?.getContext()!!.packageName + ".fileProvider", imageFile)
        }else{
            Uri.fromFile(imageFile)
        }
    }

    override fun handleActivityResult(requestCode: Int, data: Intent?) {

        when(requestCode){
            Consts.SELECT_PIC_BY_TACK_PHOTO ->{
                startCropPic(Uri.fromFile(File(mImagePath)))
            }
            Consts.SELECT_PIC_BY_PICK_PHOTO -> {
                startCropPic(data?.data)
            }
            UCrop.REQUEST_CROP ->handleCropResult(data)
            UCrop.RESULT_ERROR -> handleCropError(data)
        }
    }

    /**
     * 裁剪失败
     */
    private fun handleCropError(data: Intent?) {
        Log.i("path","error")
        if(mImagePath != ""){
            deleteTempPhotoFile()
        }

        val error:Throwable? = UCrop.getError(data!!)
        if(error != null){
            mView?.showError(error.message)
        }else run{
            mView?.showError("无法剪切图片")
        }
        mView?.getPicError()
    }

    /**
     * 处理裁剪结果
     */
    private fun handleCropResult(data: Intent?) {
        if(mImagePath != ""){
            deleteTempPhotoFile()
        }
        val resultUri:Uri? = UCrop.getOutput(data!!)
        val pics:ArrayList<String> = ArrayList()
        pics.add(resultUri?.path!!)
        CompressPhotoUtils().CompressPhoto(mView?.getContext()!!,pics,object: CompressPhotoUtils.CompressCallBack{
            override fun success(list: List<String>) {
                mView?.getPicSuccess(arrayOf(Uri.fromFile(File(list[0]))))
            }
        })
    }

    /**
     * 剪裁图片
     */
    private fun startCropPic(fromFile: Uri?) {
        val file = File(Consts.SAVEPICPATH)
        if(!file.exists())file.mkdirs()
        val targetFile = File(Consts.SAVEPICPATH+System.currentTimeMillis()+".jpg")
        UCrop.of(fromFile!!, Uri.fromFile(targetFile))
                .withAspectRatio(cropWidth.toFloat(),cropHeight.toFloat())
                .withMaxResultSize(cropWidth,cropHeight)
                .withTargetActivity(CropActivity::class.java)
                .start(mView?.getActivity()!!)
    }


    /**
     * 删除临时文件
     */
    private fun deleteTempPhotoFile() {
        val file = File(mImagePath)
        if(file.exists() && file.isFile){
            file.delete()
        }
        mImagePath = ""
    }

    private fun deleteDir(dir:String?){
        val file = File(dir)
        if(file.exists()){
            for(file:File in file.listFiles()){
                if(file.name.endsWith(".jpg") || file.name.endsWith(".png")){
                    file.delete()
                }
            }
        }
    }
}