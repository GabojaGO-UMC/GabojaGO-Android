package org.techtown.gabojago.menu.record.recordRetrofit

interface SingleResultListView {
    fun onSingleResultListSuccess(result: SingleResult)
    fun onSingleResultListLoading()
    fun onSingleResultListFailure(code: Int, message: String)
}