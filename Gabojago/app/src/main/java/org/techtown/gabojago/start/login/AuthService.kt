package org.techtown.gabojago.start.login

import android.util.Log
import org.techtown.gabojago.main.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {

    private lateinit var loginView: LoginView

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun login(loginToken: String) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.login(AuthRequest(loginToken)).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGINACT/Response", response.toString())
                val resp = response.body()!!
                Log.d("LOGINACT/Code", resp.code.toString())

                if(resp.isSuccess){
                    loginView.onLoginSuccess(resp.result.jwt)
                }
                else{
                    loginView.onLoginFailure(resp.code, resp.message)
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                loginView.onLoginFailure(400, t.message!!)
            }
        })
    }
}