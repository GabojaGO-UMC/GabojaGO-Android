package org.techtown.gabojago.menu.record.recordRetrofit

interface FolderLookView {
    fun onFolderLookLoading()
    fun onFolderLookSuccess(result: FolderLookResult)
    fun onFolderLookFailure(code: Int, message: String)
}