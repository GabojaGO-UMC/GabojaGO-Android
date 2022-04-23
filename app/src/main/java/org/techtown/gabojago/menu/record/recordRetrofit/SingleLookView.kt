package org.techtown.gabojago.menu.record.recordRetrofit

interface SingleLookView {
    fun onSingleLookLoading()
    fun onSingleLookSuccess(result: SingleLookResult)
    fun onSingleLookFailure(code: Int, message: String)
}