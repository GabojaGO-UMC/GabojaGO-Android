package org.techtown.gabojago.menu.randomPick.home

interface RandomView {
    fun onRandomLoading()
    fun onRandomResultSuccess()
    fun onRandomResultFailure(code: Int, message: String)
}