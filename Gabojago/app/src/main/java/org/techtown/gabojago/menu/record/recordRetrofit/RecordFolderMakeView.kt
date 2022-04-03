package org.techtown.gabojago.menu.record.recordRetrofit

interface RecordFolderMakeView {
    fun onRecordFolderMakeSuccess()
    fun onRecordFolderMakeFailure(code: Int, message: String)
}