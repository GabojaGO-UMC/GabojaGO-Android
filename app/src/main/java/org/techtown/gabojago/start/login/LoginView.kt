package org.techtown.gabojago.start.login

interface LoginView {
    fun onLoginSuccess(userJwt: String)
    fun onLoginFailure(code: Int, message: String)
}