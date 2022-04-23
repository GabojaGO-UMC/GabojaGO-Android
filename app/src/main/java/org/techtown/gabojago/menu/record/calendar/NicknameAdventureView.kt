package org.techtown.gabojago.menu.record.calendar

interface NicknameAdventureView {
    fun onNicknameAdventureSuccess(userNicknameAdventure: NicknameAdventureResult)
    fun onNicknameAdventureLoading()
    fun onNicknameAdventureFailure(code: Int, message: String)
}