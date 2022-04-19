package org.techtown.gabojago.menu.manage

import com.google.gson.annotations.SerializedName

data class ManageRequest(
    @SerializedName("x-access-token") val xAccessToken: String
)

data class NicknameResult(
    @SerializedName("userNickname") val userNickname: String
)

data class NewNickName(
    @SerializedName("userNickname") val userNickname: String
)

data class NicknameResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: NicknameResult
)

data class NewNicknameResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)