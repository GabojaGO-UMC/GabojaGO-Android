package org.techtown.gabojago.menu.randomPick.home

import com.google.gson.annotations.SerializedName

data class RandomResultRequest(
    @SerializedName("randomResultContent") val randomResultContent: String,
    @SerializedName("randomResultType") val randomResultType: Int
)

data class RandomResultResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)