package org.techtown.gabojago.menu.record.calendar

import retrofit2.Call
import retrofit2.http.*

interface CalendarRetrofitInterface {
    @GET("/app/user/nicknameAdventure")
    fun getNicknameAdventure(
        @Header("x-access-token") xAccessToken: String
    ): Call<NicknameAdventureResponse>

    @GET("/app/calendar/{yearmonth}")
    fun getAdventureTime(
        @Header("x-access-token") xAccessToken: String,
        @Path("yearmonth") yearMonth: String
    ): Call<AdventureTimeResponse>
}