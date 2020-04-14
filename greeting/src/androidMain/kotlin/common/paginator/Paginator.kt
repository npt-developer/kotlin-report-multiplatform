package org.greeting.common.paginator

import android.util.Log

public class AndroidPaginator: Paginator{

    constructor(page: Long, pageSize: Long, total: Long) : super(page, pageSize, total) {
    }

    override fun hasNextPage(): Boolean {
        val pageLasted: Double = Math.ceil((mTotal.toDouble() / this.mPageSize.toDouble()))
        return this.mPage < pageLasted
    }

    override fun isPageExists(): Boolean {
        val pageLasted: Double = Math.ceil((mTotal / this.mPageSize).toDouble())
        return this.mPage <= pageLasted
    }

    override fun isPageExists(page: Long): Boolean {
        val pageLasted: Double = Math.ceil((mTotal / this.mPageSize).toDouble())
        return page <= pageLasted
    }

}
