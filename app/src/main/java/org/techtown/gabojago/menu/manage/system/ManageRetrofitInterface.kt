package org.techtown.gabojago.menu.manage.system

import org.techtown.gabojago.menu.manage.*
import retrofit2.Call
import retrofit2.http.*

interface ManageRetrofitInterface {
    @GET("/app/user/nickname")
    fun getNickname(
        @Header("x-access-token") xAccessToken: String
    ): Call<NicknameResponse>

    @PATCH("/app/user/newNickname")
    fun modifyNickname(
        @Header("x-access-token") xAccessToken: String,
        @Body newNickname: NewNickName,
    ): Call<CheckUserResponse>

    @GET("/app/user/logout")
    fun logout(
        @Header("x-access-token") xAccessToken: String
    ): Call<CheckUserResponse>

    @POST("/app/withdrawal")
    fun withdrawal(
        @Header("x-access-token") xAccessToken: String
    ): Call<CheckUserResponse>

    @GET("token")
    fun naverWithdrawal(
        @Query("grant_type") grant_type: String,
        @Query("client_id") client_id: String,
        @Query("client_secret") client_secret: String,
        @Query("access_token") access_token: String,
        @Query("service_provider") service_provider: String
    ): Call<NaverWithdrawalResponse>
}