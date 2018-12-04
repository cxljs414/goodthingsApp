package com.goodthings.app.bean

import java.io.Serializable

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/1/22
 * 修改内容：
 * 最后修改时间：
 */
class AddressBean : Serializable {

    var id: Int? = null//地址id
    var user_id: Int? = null
    var name: String? = null
    var phone: String? = null//收货人手机号
    var city: String? = null//所属省市区
    var addr: String? = null//具体地址
    var city_code: String? = null//城市编号，以;隔开
    var is_default: Int? = null//是否默认地址1是2否
}
