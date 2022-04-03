package org.techtown.gabojago.menu.randomPick.home

import retrofit2.Call
import retrofit2.http.*

interface RandomRetrofitInterface {
    @POST("/app/randomResult")
    fun storeRandom(
        @Header("x-access-token") xAccessToken: String,
        @Body randomResultContent: RandomResultRequest
    ): Call<RandomResultResponse>
}
