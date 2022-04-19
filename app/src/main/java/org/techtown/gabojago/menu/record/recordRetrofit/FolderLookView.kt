package org.techtown.gabojago.menu.record.recordRetrofit

interface FolderLookView {
    fun onFolderLookSuccess(result: FolderLookResult)
    fun onFolderLookFailure(code: Int, message: String)
}