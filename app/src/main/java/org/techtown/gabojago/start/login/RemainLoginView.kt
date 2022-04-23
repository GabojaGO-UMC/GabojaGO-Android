package org.techtown.gabojago.start.login

interface RemainLoginView {
    fun onRemainLoginSuccess(isRemain: Boolean)
    fun onRemainLoginFailure(code: Int, message: String)
}