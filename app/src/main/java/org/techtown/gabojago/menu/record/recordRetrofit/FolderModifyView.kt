package org.techtown.gabojago.menu.record.recordRetrofit

interface FolderModifyView {
    fun onFolderModifySuccess()
    fun onFolderModifyFailure(code: Int, message: String)
}