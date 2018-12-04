package com.goodthings.app.bean

import java.io.Serializable

/**
 * 分页
 * @author lzp
 */
// dbutils必须要有
class PageBean<T>  {
    /**
     * 定义当前页
     */
    var currentPage = 1
    /**
     * 定义一页展示多少条
     */
    var pageSize = 10
    /**
     * 当前页展示的数据集合
     */
    var pageList: List<T>? = null
    /**
     * 定义总条目个数 count(*)
     */
    var totalCount: Int = 0

    /**
     * 定义查询时的其实索引值 select * from t_user limit 3,5
     */
    // 自行或许起始索引值
    var startIndex: Int = 0
        get() {
            this.startIndex = (currentPage - 1) * pageSize
            return field
        }

    /**
     * 定义上一页 pageBean.prePage
     */
    /**
     * 获取上一页
     *
     * @return
     */
    var prePage: Int = 0
        get() {
            if (currentPage <= 1) {
                this.prePage = 1
            } else {
                this.prePage = currentPage - 1
            }
            return field
        }

    /**
     * 定义要展示的本次集合
     */
    /**
     * 一共有多少页
     */
    // 一共有多少条 100 /5 101 21
    var totalPage: Int = 0
        get() {
            if (totalCount % pageSize == 0) {
                this.totalPage = totalCount / pageSize
            } else {
                this.totalPage = totalCount / pageSize + 1
            }
            return field
        }
    /**
     * 定义下一页
     */
    /**
     * 获取下一页
     *
     * @return
     */
    var nextPage: Int = 0
        get() {

            if (currentPage >= totalPage) {
                this.nextPage = totalPage
            } else {
                this.nextPage = currentPage + 1
            }

            return field
        }

    /**
     * 获取总共的页码
     *
     * @return
     */
    /**
     * 开始页
     */
    var startPage: Int = 0
        get() {
            if (currentPage <= 10) {
                this.startPage = 1
            } else {
                this.startPage = currentPage - 4
            }
            if (field <= 1) {
                this.startPage = 1
            }

            return field
        }
    /**
     * 结束页
     */
    var endPage: Int = 0
        get() {
            if (currentPage <= 10) {
                this.endPage = 10
            } else {
                this.endPage = currentPage + 4
            }
            if (field >= totalPage) {
                this.endPage = totalPage
            }
            return field
        }

}
