package org.techtown.gabojago.menu.manage.auth

interface LogoutView {
    fun onLogoutSuccess()
    fun onLogoutFailure(code: Int, message: String)
}