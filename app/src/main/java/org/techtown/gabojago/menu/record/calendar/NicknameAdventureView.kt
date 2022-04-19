package org.techtown.gabojago.menu.record.calendar

interface NicknameAdventureView {
    fun onNicknameAdventureSuccess(userNicknameAdventure: NicknameAdventureResult)
    fun onNicknameAdventureFailure(code: Int, message: String)
}