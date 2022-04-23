package org.techtown.gabojago.menu.record.recordRetrofit

interface SingleModifyView {
    fun onSingleModifySuccess()
    fun onSingleModifyFailure(code: Int, message: String)
}