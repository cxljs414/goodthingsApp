package com.goodthings.app.util

import java.math.BigDecimal

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/25
 * 修改内容：
 * 最后修改时间：
 */
object DoubleUtil {

    fun round(v: Double?, scale: Int): Double {
        if (scale < 0) {
            throw IllegalArgumentException("The scale must be a positive integer or zero")
        }
        val b = if (null == v) BigDecimal("0.0") else BigDecimal(java.lang.Double.toString(v))
        val one = BigDecimal("1")
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }
}