package org.techtown.gabojago.start.login

import retrofit2.Call
import retrofit2.http.*

interface AuthRetrofitInterface {
    @POST("/auth/naver")
    fun login(
        @Body access_token: AuthRequest
    ): Call<AuthResponse>
}