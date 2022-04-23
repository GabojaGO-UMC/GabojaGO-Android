package org.techtown.gabojago.menu.record.recordRetrofit

interface SinglerecordingDeleteView {
    fun onSinglerecordingDeleteSuccess()
    fun onSinglerecordingDeleteFailure(code: Int, message: String)
}