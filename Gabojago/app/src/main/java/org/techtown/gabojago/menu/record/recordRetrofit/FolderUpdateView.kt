package org.techtown.gabojago.menu.record.recordRetrofit

interface FolderUpdateView {
    fun onFolderUpdateSuccess()
    fun onFolderUpdateFailure(code: Int, message: String)
}