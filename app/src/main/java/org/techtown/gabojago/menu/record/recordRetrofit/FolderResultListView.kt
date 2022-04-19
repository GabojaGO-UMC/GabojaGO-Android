package org.techtown.gabojago.menu.record.recordRetrofit

interface FolderResultListView {
    fun onFolderResultListSuccess(result: ArrayList<FolderResultList>)
    fun onFolderResultListFailure(code: Int, message: String)
}