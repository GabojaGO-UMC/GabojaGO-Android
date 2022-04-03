package org.techtown.gabojago.menu.record.recordRetrofit

interface SingleResultListView {
    fun onSingleResultListSuccess(result: ArrayList<SingleResultListResult>)
    fun onSingleResultListFailure(code: Int, message: String)
}