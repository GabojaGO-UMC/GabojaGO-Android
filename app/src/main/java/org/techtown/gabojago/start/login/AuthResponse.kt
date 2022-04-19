package org.techtown.gabojago.start.login

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("access_token") val access_token: String
)

data class JwtResult(
    @SerializedName("jwt") val jwt: String
)

data class AuthResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: JwtResult
)