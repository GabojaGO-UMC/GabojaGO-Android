package org.techtown.gabojago.menu.manage.auth

interface WithdrawalView {
    fun onWithdrawalSuccess()
    fun onWithdrawalFailure(code: Int, message: String)
}