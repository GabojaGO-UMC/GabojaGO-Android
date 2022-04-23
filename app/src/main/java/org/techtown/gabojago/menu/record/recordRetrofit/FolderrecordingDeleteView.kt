package org.techtown.gabojago.menu.record.recordRetrofit

interface FolderrecordingDeleteView {
    fun onFolderrecordingDeleteSuccess()
    fun onFolderrecordingDeleteFailure(code: Int, message: String)
}