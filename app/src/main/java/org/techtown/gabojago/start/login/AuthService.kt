package org.techtown.gabojago.start.login

import android.util.Log
import org.techtown.gabojago.main.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {

    private lateinit var loginView: LoginView
    private lateinit var remainLoginView: RemainLoginView

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun setRemainLoginView(remainLoginView: RemainLoginView){
        this.remainLoginView = remainLoginView
    }

    fun login(loginToken: String) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.login(AuthRequest(loginToken)).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.body() == null) {
                    remainLoginView.onRemainLoginFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
                    Log.d("LOGINACT/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("LOGINACT/Code", resp.code.toString())

                    if (resp.isSuccess) {
                        loginView.onLoginSuccess(resp.result.jwt)
                    } else {
                        loginView.onLoginFailure(resp.code, resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                loginView.onLoginFailure(400, t.message!!)
            }
        })
    }

    fun remainLogin(loginToken: String) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.remainLogin(loginToken).enqueue(object : Callback<AuthRemainResponse> {
            override fun onResponse(
                call: Call<AuthRemainResponse>,
                response: Response<AuthRemainResponse>
            ) {
                if (response.body() == null) {
                    remainLoginView.onRemainLoginFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
                    Log.d("RELOGINACT/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("RELOGINACT/Code", resp.code.toString())

                    if (resp.isSuccess) {
                        remainLoginView.onRemainLoginSuccess(resp.result)
                    } else {
                        remainLoginView.onRemainLoginFailure(resp.code, resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<AuthRemainResponse>, t: Throwable) {
                remainLoginView.onRemainLoginFailure(400, t.message!!)
            }
        })
    }
}