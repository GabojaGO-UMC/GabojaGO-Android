package org.techtown.gabojago.menu.record.recordRetrofit

interface FolderResultListView {
    fun onFolderResultListSuccess(result: FolderResult)
    fun onFolderResultListLoading()
    fun onFolderResultListFailure(code: Int, message: String)
}