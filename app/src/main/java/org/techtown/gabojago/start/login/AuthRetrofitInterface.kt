package org.techtown.gabojago.start.login

import retrofit2.Call
import retrofit2.http.*

interface AuthRetrofitInterface {
    @POST("/auth/naver")
    fun login(
        @Body access_token: AuthRequest
    ): Call<AuthResponse>

    @GET("/app/user/autologin")
    fun remainLogin(
        @Header("x-access-token") xAccessToken: String
    ): Call<AuthRemainResponse>
}