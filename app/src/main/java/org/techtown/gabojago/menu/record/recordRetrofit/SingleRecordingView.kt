package org.techtown.gabojago.menu.record.recordRetrofit

interface SingleRecordingView {
    fun onSingleRecordingSuccess()
    fun onSingleRecordingFailure(code: Int, message: String)
}