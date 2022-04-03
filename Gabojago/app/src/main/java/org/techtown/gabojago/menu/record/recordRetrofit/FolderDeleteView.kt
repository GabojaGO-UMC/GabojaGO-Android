package org.techtown.gabojago.menu.record.recordRetrofit

interface FolderDeleteView {
    fun onFolderDeleteSuccess()
    fun onFolderDeleteFailure(code: Int, message: String)
}