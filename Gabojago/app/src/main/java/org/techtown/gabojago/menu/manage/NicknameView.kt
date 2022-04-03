package org.techtown.gabojago.menu.manage

interface NicknameView {
    fun onNicknameLoading()
    fun onNicknameSuccess(userNickname: String)
    fun onNicknameFailure(code: Int, message: String)
}