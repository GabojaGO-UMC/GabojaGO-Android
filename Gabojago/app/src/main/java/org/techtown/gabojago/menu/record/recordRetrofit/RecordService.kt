package org.techtown.gabojago.menu.record.recordRetrofit

import android.util.Log
import org.techtown.gabojago.main.getRetrofit
import org.techtown.gabojago.menu.record.RecordFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class RecordService {
    private lateinit var recordCountView: RecordCountView

    fun setRecordCountView(recordCountView: RecordCountView) {
        this.recordCountView = recordCountView
    }

    private lateinit var singleResultListView: SingleResultListView

    fun setSingleResultListView(singleResultListView: SingleResultListView) {
        this.singleResultListView = singleResultListView
    }

    private lateinit var recordFolderMakeView: RecordFolderMakeView

    fun setRecordFolderMakeView(recordFolderMakeView: RecordFolderMakeView) {
        this.recordFolderMakeView = recordFolderMakeView
    }

    private lateinit var folderResultListView: FolderResultListView

    fun setFolderResultListView(folderResultListView: FolderResultListView) {
        this.folderResultListView = folderResultListView
    }

    private lateinit var folderRecordingView: FolderRecordingView

    fun setFolderRecordingView(folderRecordingView: FolderRecordingView) {
        this.folderRecordingView = folderRecordingView
    }

    private lateinit var folderLookView: FolderLookView

    fun setFolderLookView(folderLookView: FolderLookView) {
        this.folderLookView = folderLookView
    }

    private lateinit var folderDeleteView: FolderDeleteView

    fun setFolderDeleteView(folderDeleteView: FolderDeleteView) {
        this.folderDeleteView = folderDeleteView
    }

    private lateinit var folderUpdateView: FolderUpdateView

    fun setFolderUpdateView(folderUpdateView: FolderUpdateView) {
        this.folderUpdateView = folderUpdateView
    }

    private lateinit var folderBreakView: FolderBreakView

    fun setFolderBreakView(folderBreakView: FolderBreakView) {
        this.folderBreakView = folderBreakView
    }

    //개별메인
    fun getSingleResultList(userJwt: String, date: String) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.getSingleResultList(userJwt, date).enqueue(object :
            Callback<SingleResultListResponse> {
            override fun onResponse(
                call: Call<SingleResultListResponse>,
                response: Response<SingleResultListResponse>
            ) {
                Log.d("SINGLERESULT/Response", response.toString())
                val resp = response.body()!!
                Log.d("SINGLERESULT/Code", resp.code.toString())

                if (resp.isSuccess) {
                    singleResultListView.onSingleResultListSuccess(resp.result!!)
                } else {
                    when (resp.code) {
                        2001 -> singleResultListView.onSingleResultListFailure(resp.code,
                            "회원 정보가 잘못되었습니다.")
                        2034 -> singleResultListView.onSingleResultListFailure(resp.code,
                            resp.message)
                        2002 -> singleResultListView.onSingleResultListFailure(resp.code,
                            resp.message)
                        3034 -> singleResultListView.onSingleResultListFailure(resp.code,
                            resp.message)
                        2000 -> singleResultListView.onSingleResultListFailure(resp.code,
                            resp.message)
                        3000 -> singleResultListView.onSingleResultListFailure(resp.code,
                            resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<SingleResultListResponse>, t: Throwable) {
                singleResultListView.onSingleResultListFailure(400, t.toString())
                Log.d("CHECK", t.toString())
            }
        })
    }
    //폴더생성
    fun putFolderMakeIdx(userJwt: String, randomResultIdx: List<Int>) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.putFolderMakeIdx(userJwt, randomResultRequest(randomResultIdx))
            .enqueue(object :
                Callback<RecordFolderMakeResponse> {
                override fun onResponse(
                    call: Call<RecordFolderMakeResponse>,
                    response: Response<RecordFolderMakeResponse>
                ) {
                    Log.d("RECORDRESULT/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("RECORDRESULT/Code", resp.code.toString())

                    if (resp.isSuccess) {
                        recordFolderMakeView.onRecordFolderMakeSuccess()
                    } else {
                        when (resp.code) {
                            2002 -> recordFolderMakeView.onRecordFolderMakeFailure(resp.code,
                                "회원 정보가 잘못되었습니다.")
                            2010 -> recordFolderMakeView.onRecordFolderMakeFailure(resp.code,
                                resp.message)
                            3007 -> recordFolderMakeView.onRecordFolderMakeFailure(resp.code,
                                resp.message)
                            3016 -> recordFolderMakeView.onRecordFolderMakeFailure(resp.code,
                                resp.message)
                            4000 -> recordFolderMakeView.onRecordFolderMakeFailure(resp.code,
                                resp.message)
                            2001 -> recordFolderMakeView.onRecordFolderMakeFailure(resp.code,
                                resp.message)
                            2000 -> recordFolderMakeView.onRecordFolderMakeFailure(resp.code,
                                resp.message)
                            3000 -> recordFolderMakeView.onRecordFolderMakeFailure(resp.code,
                                resp.message)
                        }
                    }
                }

                override fun onFailure(call: Call<RecordFolderMakeResponse>, t: Throwable) {
                    recordFolderMakeView.onRecordFolderMakeFailure(400, t.toString())
                    Log.d("CALENDARGETADV", t.toString())
                }
            })
    }
    //뽑기개수
    fun recordCount(userJwt: String) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordCountView.onRecordCountLoading()

        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
        val stringDate = dateFormat.format(date)

        recordService.recordCount(userJwt, stringDate).enqueue(object :
            Callback<RecordCountResponse> {

            override fun onResponse(
                call: Call<RecordCountResponse>,
                response: Response<RecordCountResponse>
            ) {
                Log.d("RECORDCOUNT/Response", response.toString())
                val resp = response.body()!!
                Log.d("RECORDCOUNT/Code", resp.code.toString())

                if (resp.isSuccess) {
                    recordCountView.onRecordCountSuccess(resp.result)
                } else {
                    when (resp.code) {
                        2001 -> recordCountView.onRecordCountFailure(resp.code, resp.message)
                        2000 -> recordCountView.onRecordCountFailure(resp.code, resp.message)
                        3000 -> recordCountView.onRecordCountFailure(resp.code, resp.message)
                        2033 -> recordCountView.onRecordCountFailure(resp.code, resp.message)
                        2002 -> recordCountView.onRecordCountFailure(resp.code, resp.message)
                        3033 -> recordCountView.onRecordCountFailure(resp.code, resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<RecordCountResponse>, t: Throwable) {
                recordCountView.onRecordCountFailure(400, t.toString())
                Log.d("CALENDARGETADV", t.toString())
            }
        })
    }
    //폴더메인
    fun getFolderResultList(userJwt: String, date: String) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.getFolderResultList(userJwt, date).enqueue(object :
            Callback<FolderResultListResponse> {
            override fun onResponse(
                call: Call<FolderResultListResponse>,
                response: Response<FolderResultListResponse>
            ) {
                Log.d("FOLDERRESULT/Response", response.toString())
                val resp = response.body()!!
                Log.d("FOLDERRESULT/Code", resp.code.toString())

                if (resp.isSuccess) {
                    folderResultListView.onFolderResultListSuccess(resp.result)
                } else {
                    when (resp.code) {
                        2001 -> folderResultListView.onFolderResultListFailure(resp.code,
                            resp.message)
                        2034 -> folderResultListView.onFolderResultListFailure(resp.code,
                            resp.message)
                        2002 -> folderResultListView.onFolderResultListFailure(resp.code,
                            resp.message)
                        3034 -> folderResultListView.onFolderResultListFailure(resp.code,
                            resp.message)
                        2000 -> folderResultListView.onFolderResultListFailure(resp.code,
                            resp.message)
                        3000 -> folderResultListView.onFolderResultListFailure(resp.code,
                            resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<FolderResultListResponse>, t: Throwable) {
                folderResultListView.onFolderResultListFailure(400, t.toString())
                Log.d("CALENDARGETADV", t.toString())
            }
        })
    }
    //폴더기록
    fun putFolderRecord(userJwt: String, folderIdx: Int, folderRecording: FolderRecordingRequest) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.putFolderRecord(userJwt, folderIdx, folderRecording)
            .enqueue(object :
                Callback<FolderRecordResponse> {
                override fun onResponse(
                    call: Call<FolderRecordResponse>,
                    response: Response<FolderRecordResponse>
                ) {
                    Log.d("RECORDRESULT/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("RECORDRESULT/Code", resp.code.toString())

                    if (resp.isSuccess) {
                        folderRecordingView.onFolderRecordingSuccess()
                    } else {
                        when (resp.code) {
                            2001 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                "회원 정보가 잘못되었습니다.")
                            2002 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            2000 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            3000 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            2031 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            2039 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            2040 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            2041 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            3042 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            3017 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            3006 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            2035 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            2036 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            2038 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            2037 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                            4000 -> folderRecordingView.onFolderRecordingFailure(resp.code,
                                resp.message)
                        }
                    }
                }

                override fun onFailure(call: Call<FolderRecordResponse>, t: Throwable) {
                    folderRecordingView.onFolderRecordingFailure(400, t.toString())
                    Log.d("CALENDARGETADV", t.toString())
                }
            })
    }

    //폴더조회
    fun getFolderLook(userJwt: String, folderIdx: Int) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.getFolderLook(userJwt, folderIdx).enqueue(object :
            Callback<FolderLookResponse> {
            override fun onResponse(
                call: Call<FolderLookResponse>,
                response: Response<FolderLookResponse>
            ) {
                Log.d("FOLDERRESULT/Response", response.toString())
                val resp = response.body()!!
                Log.d("FOLDERRESULT/Code", resp.code.toString())

                if (resp.isSuccess) {
                    folderLookView.onFolderLookSuccess(resp.result)
                } else {
                    when (resp.code) {
                        2001 -> folderLookView.onFolderLookFailure(resp.code,
                            resp.message)
                        2031 -> folderLookView.onFolderLookFailure(resp.code,
                            resp.message)
                        2002 -> folderLookView.onFolderLookFailure(resp.code,
                            resp.message)
                        3017 -> folderLookView.onFolderLookFailure(resp.code,
                            resp.message)
                        3006 -> folderLookView.onFolderLookFailure(resp.code,
                            resp.message)
                        3038 -> folderLookView.onFolderLookFailure(resp.code,
                            resp.message)
                        3040 -> folderLookView.onFolderLookFailure(resp.code,
                            resp.message)
                        3035 -> folderLookView.onFolderLookFailure(resp.code,
                            resp.message)
                        4000 -> folderLookView.onFolderLookFailure(resp.code,
                            resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<FolderLookResponse>, t: Throwable) {
                folderLookView.onFolderLookFailure(400, t.toString())
                Log.d("CALENDARGETADV", t.toString())
            }
        })
    }
    //삭제
    fun putIdx(userJwt: String, resultIdx:List<Int>, folderIdx: List<Int>) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.putIdx(userJwt, FolderDeleteRequest(resultIdx,folderIdx)).enqueue(object :
            Callback<FolderDeleteResponse> {
            override fun onResponse(
                call: Call<FolderDeleteResponse>,
                response: Response<FolderDeleteResponse>
            ) {
                Log.d("FOLDERDELETE/Response", response.toString())
                val resp = response.body()!!
                Log.d("FOLDERDELETE/Code", resp.code.toString())

                if (resp.isSuccess) {
                    folderDeleteView.onFolderDeleteSuccess()
                } else {
                    when (resp.code) {
                        2001 -> folderDeleteView.onFolderDeleteFailure(resp.code,
                            resp.message)
                        2002 -> folderDeleteView.onFolderDeleteFailure(resp.code,
                            resp.message)
                        2009 -> folderDeleteView.onFolderDeleteFailure(resp.code,
                            resp.message)
                        3018 -> folderDeleteView.onFolderDeleteFailure(resp.code,
                            resp.message)
                        3007 -> folderDeleteView.onFolderDeleteFailure(resp.code,
                            resp.message)
                        3017 -> folderDeleteView.onFolderDeleteFailure(resp.code,
                            resp.message)
                        3006 -> folderDeleteView.onFolderDeleteFailure(resp.code,
                            resp.message)
                        4000 -> folderDeleteView.onFolderDeleteFailure(resp.code,
                            resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<FolderDeleteResponse>, t: Throwable) {
                folderDeleteView.onFolderDeleteFailure(400, t.toString())
                Log.d("CALENDARGETADV", t.toString())
            }
        })
    }
    //목록수정
    fun putUpdateFolderIdx(userJwt: String,folderIdx: Int, plus_randomResultIdx:List<Int>, minus_randomResultIdx: List<Int>) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.putUpdateFolderIdx(userJwt, FolderUpdateRequest(folderIdx,plus_randomResultIdx,minus_randomResultIdx)).enqueue(object :
            Callback<FolderUpdateResponse> {
            override fun onResponse(
                call: Call<FolderUpdateResponse>,
                response: Response<FolderUpdateResponse>
            ) {
                Log.d("FOLDERUPDATE/Response", response.toString())
                val resp = response.body()!!
                Log.d("FOLDERUPDATE/Code", resp.code.toString())

                if (resp.isSuccess) {
                    folderUpdateView.onFolderUpdateSuccess()
                } else {
                    when (resp.code) {
                        6012 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                            resp.message)
                        7005 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                            resp.message)
                        7007 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                            resp.message)
                        2013 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                            resp.message)
                        7006 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                            resp.message)
                        7003 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                            resp.message)
                        7008 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                            resp.message)
                        7004 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                            resp.message)
                        4000 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                            resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<FolderUpdateResponse>, t: Throwable) {
                folderUpdateView.onFolderUpdateFailure(400, t.toString())
            }
        })
    }
    //폴더해체
    fun putBreakFolderIdx(userJwt: String, folderIdx: Int) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.putBreakFolderIdx(userJwt, folderIdx).enqueue(object :
            Callback<FolderBreakResponse> {
            override fun onResponse(
                call: Call<FolderBreakResponse>,
                response: Response<FolderBreakResponse>
            ) {
                Log.d("FOLDERBREAK/Response", response.toString())
                val resp = response.body()!!
                Log.d("FOLDERBREAK/Code", resp.code.toString())

                if (resp.isSuccess) {
                    folderBreakView.onFolderBreakSuccess()
                } else {
                    when (resp.code) {
                        2001-> folderBreakView.onFolderBreakFailure(resp.code,
                            "회원 정보가 잘못되었습니다.")
                        2002 -> folderBreakView.onFolderBreakFailure(resp.code,
                            resp.message)
                        2009 -> folderBreakView.onFolderBreakFailure(resp.code,
                            resp.message)
                        3018 -> folderBreakView.onFolderBreakFailure(resp.code,
                            resp.message)
                        3007 -> folderBreakView.onFolderBreakFailure(resp.code,
                            resp.message)
                        3017 -> folderBreakView.onFolderBreakFailure(resp.code,
                            resp.message)
                        3006 -> folderBreakView.onFolderBreakFailure(resp.code,
                            resp.message)
                        4000 -> folderBreakView.onFolderBreakFailure(resp.code,
                            resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<FolderBreakResponse>, t: Throwable) {
                folderBreakView.onFolderBreakFailure(400, t.toString())
                Log.d("CHECK", t.toString())
            }
        })
    }
}
