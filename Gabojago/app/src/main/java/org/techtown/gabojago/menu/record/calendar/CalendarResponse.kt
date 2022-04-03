package org.techtown.gabojago.menu.record.calendar

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//캘린더 닉네임 object
data class NicknameAdventureResult(
    @SerializedName("userNickname_in_calendar") val userNicknameAdventure: String
)

//가입 날짜, 모험 횟수, 한달간 모험 날짜 배열 object
data class AdventureTimeResult(
    @SerializedName("userjoindate") val userJoinDate: String,
    @SerializedName("monthlyAdventureTimes") val monthlyAdventureTimes: Int,
    @SerializedName("randomresultdateList") val randomresultdateList: ArrayList<CreateAt>
)

//모험한 날짜 object
data class CreateAt(
    @SerializedName("day") val day: Int
)

//닉네임 response
data class NicknameAdventureResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: NicknameAdventureResult
)

//가입 날짜, 모험 횟수, 한달간 모험 날짜 배열 response
data class AdventureTimeResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: AdventureTimeResult
)
