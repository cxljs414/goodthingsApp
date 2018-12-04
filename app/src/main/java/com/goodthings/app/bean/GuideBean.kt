package com.goodthings.app.bean

import java.io.Serializable

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/28
 * 修改内容：
 * 最后修改时间：
 */
class GuideBean (
        val id:Int,
        val imgurl:String,//图片地址
        val linkurl:String,//链接地址
        val time:Int,//显示时间
        val seesecond:Int,//当天显示次数
        val isforce:Int//是否使用，0否1是
) {

}