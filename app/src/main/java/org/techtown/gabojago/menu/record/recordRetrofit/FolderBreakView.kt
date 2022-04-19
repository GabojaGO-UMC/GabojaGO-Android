package org.techtown.gabojago.menu.record.recordRetrofit

interface FolderBreakView {
    fun onFolderBreakSuccess()
    fun onFolderBreakFailure(code: Int, message: String)
}