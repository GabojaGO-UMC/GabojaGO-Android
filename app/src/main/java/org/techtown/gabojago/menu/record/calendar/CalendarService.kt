package org.techtown.gabojago.menu.record.calendar

import android.util.Log
import org.techtown.gabojago.main.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarService {
    private lateinit var nicknameAdventureView: NicknameAdventureView
    private lateinit var adventureTimeView: AdventureTimeView

    fun setNicknameAdventureView(nicknameAdventureView: NicknameAdventureView){
        this.nicknameAdventureView = nicknameAdventureView
    }
    fun setAdventureTimeView(adventureTimeView: AdventureTimeView){
        this.adventureTimeView = adventureTimeView
    }

    fun getNicknameAdventure(userJwt: String) {
        val calendarService = getRetrofit().create(CalendarRetrofitInterface::class.java)
        calendarService.getNicknameAdventure(userJwt).enqueue(object : Callback<NicknameAdventureResponse> {
            override fun onResponse(call: Call<NicknameAdventureResponse>, response: Response<NicknameAdventureResponse>) {
                if (response.body() == null) {
                    nicknameAdventureView.onNicknameAdventureFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
                    Log.d("NICKNAMEAD/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("NICKNAMEAD/Code", resp.code.toString())

                    if (resp.isSuccess) {
                        nicknameAdventureView.onNicknameAdventureSuccess(resp.result!!)
                    } else {
                        nicknameAdventureView.onNicknameAdventureFailure(resp.code, resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<NicknameAdventureResponse>, t: Throwable) {
                nicknameAdventureView.onNicknameAdventureFailure(400, "Network Error")
            }
        })
    }

    fun getAdventureTime(userJwt: String, yearMonth: String) {
        val calendarService = getRetrofit().create(CalendarRetrofitInterface::class.java)
        adventureTimeView.onAdventureTimeLoading()
        calendarService.getAdventureTime(userJwt, yearMonth).enqueue(object : Callback<AdventureTimeResponse> {
            override fun onResponse(call: Call<AdventureTimeResponse>, response: Response<AdventureTimeResponse>) {
                if (response.body() == null) {
                    adventureTimeView.onAdventureTimeFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
                    Log.d("TIME/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("TIME/Code", resp.code.toString())
                    if (resp.isSuccess) {
                        adventureTimeView.onAdventureTimeSuccess(resp.result!!)
                    } else {
                        when (resp.code) {
                            2012 -> adventureTimeView.onAdventureTimeFailure(resp.code,
                                "회원 정보가 잘못되었습니다.")
                            2000 -> adventureTimeView.onAdventureTimeFailure(resp.code,
                                resp.message)
                            3000 -> adventureTimeView.onAdventureTimeFailure(resp.code,
                                resp.message)
                            5007 -> adventureTimeView.onAdventureTimeFailure(resp.code,
                                resp.message)
                            2013 -> adventureTimeView.onAdventureTimeFailure(resp.code,
                                resp.message)
                            5003 -> adventureTimeView.onAdventureTimeFailure(resp.code,
                                resp.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<AdventureTimeResponse>, t: Throwable) {
                adventureTimeView.onAdventureTimeFailure(400, t.toString())
                Log.d("CALENDARGETADV", t.toString())
            }
        })
    }

}