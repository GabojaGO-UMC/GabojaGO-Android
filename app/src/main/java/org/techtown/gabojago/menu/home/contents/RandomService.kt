package org.techtown.gabojago.menu.home.contents

import android.util.Log
import org.techtown.gabojago.main.getRetrofit
import org.techtown.gabojago.menu.home.RandomResultRequest
import org.techtown.gabojago.menu.home.RandomResultResponse
import org.techtown.gabojago.menu.home.RandomRetrofitInterface
import org.techtown.gabojago.menu.home.RandomView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RandomService {

    private lateinit var randomView: RandomView

    fun setRandomView(randomView: RandomView){
        this.randomView = randomView
    }

    fun storeResult(userJwt: String, result: String, type: Int) {
        val randomService = getRetrofit().create(RandomRetrofitInterface::class.java)

        randomView.onRandomLoading()

        randomService.storeRandom(userJwt, RandomResultRequest(result, type)).enqueue(object : Callback<RandomResultResponse> {
            override fun onResponse(call: Call<RandomResultResponse>, response: Response<RandomResultResponse>) {
                if (response.body() == null) {
                    randomView.onRandomResultFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
                    Log.d("RANDOMSTOREACT/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("RANDOMSTOREACT/Code", resp.code.toString())

                    if (resp.isSuccess) {
                        randomView.onRandomResultSuccess()
                    } else {
                        when (resp.code) {
                            6012 -> randomView.onRandomResultFailure(resp.code, "회원 정보가 잘못되었습니다.")
                            6007 -> randomView.onRandomResultFailure(resp.code, resp.message)
                            6008 -> randomView.onRandomResultFailure(resp.code, resp.message)
                            6009 -> randomView.onRandomResultFailure(resp.code, resp.message)
                            2013 -> randomView.onRandomResultFailure(resp.code, resp.message)
                            6010 -> randomView.onRandomResultFailure(resp.code, "잘못된 형식의 메뉴 접근입니다.")
                            4000 -> randomView.onRandomResultFailure(resp.code, "시스템에 문제가 생겼습니다.")
                        }
                    }
                }
            }
            override fun onFailure(call: Call<RandomResultResponse>, t: Throwable) {
                randomView.onRandomResultFailure(400, t.message!!)
            }
        })
    }
}