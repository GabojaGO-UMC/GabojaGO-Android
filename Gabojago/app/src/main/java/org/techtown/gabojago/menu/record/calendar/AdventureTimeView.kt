package org.techtown.gabojago.menu.record.calendar

interface AdventureTimeView {
    fun onAdventureTimeSuccess(adventureTime: AdventureTimeResult)
    fun onAdventureTimeLoading()
    fun onAdventureTimeFailure(code: Int, message: String)
}