package example.kotlinreport.multiplatform.shared.common.paginator

import example.kotlinreport.multiplatform.shared.config.AppConfig

public abstract class Paginator {
    var mPage: Long = 1
    var mPageSize: Long = AppConfig.Pagination.PAGE_SIZE
    var mTotal: Long

    constructor(page: Long, pageSize: Long, total: Long) {
        this.mPage = page
        this.mPageSize = pageSize
        this.mTotal = total
    }

    fun getOffset(): Long {
        val offset: Long = (mPage - 1) * mPageSize
        return offset
    }

    abstract fun hasNextPage(): Boolean

    abstract fun isPageExists(): Boolean

    abstract fun isPageExists(page: Long): Boolean

}
