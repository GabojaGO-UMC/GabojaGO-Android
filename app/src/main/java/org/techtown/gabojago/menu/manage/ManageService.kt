package org.techtown.gabojago.menu.manage

import android.util.Log
import org.techtown.gabojago.main.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageService {

    private lateinit var nicknameView: NicknameView
    private lateinit var newNicknameView: NewNicknameView


    fun setNicknameView(nicknameView: NicknameView){
        this.nicknameView = nicknameView
    }
    fun setNewNicknameView(newNicknameView: NewNicknameView){
        this.newNicknameView = newNicknameView
    }

    fun getNickname(userJwt: String) {
        nicknameView.onNicknameLoading()
        val manageService = getRetrofit().create(ManageRetrofitInterface::class.java)
        manageService.getNickname(userJwt).enqueue(object : Callback<NicknameResponse> {
            override fun onResponse(call: Call<NicknameResponse>, response: Response<NicknameResponse>) {
                Log.d("NICKNAMEACT/Response", response.toString())
                val resp = response.body()!!
                Log.d("NICKNAMEACT/Code", resp.code.toString())

                if(resp.isSuccess){
                        nicknameView.onNicknameSuccess(resp.result.userNickname)
                    }
                else {
                        nicknameView.onNicknameFailure(resp.code, resp.message)
                }
            }
            override fun onFailure(call: Call<NicknameResponse>, t: Throwable) {
                nicknameView.onNicknameFailure(400, t.message!!)
            }
        })
    }

    fun modifyNickname(userJwt: String, newNickname: String) {
        newNicknameView.onModifyNicknameLoading()
        val manageService = getRetrofit().create(ManageRetrofitInterface::class.java)

        manageService.modifyNickname(userJwt, NewNickName(newNickname)).enqueue(object : Callback<NewNicknameResponse> {
            override fun onResponse(call: Call<NewNicknameResponse>, response: Response<NewNicknameResponse>) {
                Log.d("MODIFYACT/Response", response.toString())
                val resp = response.body()!!
                Log.d("MODIFYACT/Code", resp.code.toString())

                if (resp.isSuccess) {
                        newNicknameView.onModifyNicknameSuccess(newNickname)
                }
                else {
                        newNicknameView.onModifyNicknameFailure(resp.code, resp.message)
                }
            }
            override fun onFailure(call: Call<NewNicknameResponse>, t: Throwable) {
                newNicknameView.onModifyNicknameFailure(400, t.message!!)
            }
        })
    }
}