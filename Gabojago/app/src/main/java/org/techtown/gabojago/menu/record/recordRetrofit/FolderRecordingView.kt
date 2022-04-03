package org.techtown.gabojago.menu.record.recordRetrofit

interface FolderRecordingView {
    fun onFolderRecordingSuccess()
    fun onFolderRecordingFailure(code: Int, message: String)
}