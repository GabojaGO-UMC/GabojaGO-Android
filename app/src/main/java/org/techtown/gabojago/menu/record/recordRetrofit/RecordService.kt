package org.techtown.gabojago.menu.record.recordRetrofit

import android.util.Log
import org.techtown.gabojago.main.getRetrofit
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

    private lateinit var singleRecordingView: SingleRecordingView

    fun setSingleRecordingView(singleRecordingView: SingleRecordingView) {
        this.singleRecordingView = singleRecordingView
    }

    private lateinit var folderLookView: FolderLookView

    fun setFolderLookView(folderLookView: FolderLookView) {
        this.folderLookView = folderLookView
    }

    private lateinit var singleLookView: SingleLookView

    fun setSingleLookView(singleLookView: SingleLookView) {
        this.singleLookView = singleLookView
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

    private lateinit var folderModifyView: FolderModifyView

    fun setFolderModifyView(folderModifyView: FolderModifyView) {
        this.folderModifyView = folderModifyView
    }

    private lateinit var singleModifyView: SingleModifyView

    fun setSingleModifyView(singleModifyView: SingleModifyView) {
        this.singleModifyView = singleModifyView
    }

    private lateinit var singlerecordingDeleteView: SinglerecordingDeleteView

    fun setSinglerecordingDeleteView(singlerecordingDeleteView: SinglerecordingDeleteView) {
        this.singlerecordingDeleteView = singlerecordingDeleteView
    }

    private lateinit var folderrecordingDeleteView: FolderrecordingDeleteView

    fun setFolderrecordingDeleteView(folderrecordingDeleteView: FolderrecordingDeleteView) {
        this.folderrecordingDeleteView = folderrecordingDeleteView
    }

    //개별메인
    fun getSingleResultList(userJwt: String, date: String) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        singleResultListView.onSingleResultListLoading()
        recordService.getSingleResultList(userJwt, date).enqueue(object :
            Callback<SingleResultListResponse> {
            override fun onResponse(
                call: Call<SingleResultListResponse>,
                response: Response<SingleResultListResponse>
            ) {
                if (response.body() == null) {
                    singleResultListView.onSingleResultListFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
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
                    if (response.body() == null) {
                        recordFolderMakeView.onRecordFolderMakeFailure(0, "네트워크 연결에 실패하였습니다.")
                    } else {
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
                                2013 -> recordFolderMakeView.onRecordFolderMakeFailure(resp.code,
                                    resp.message)
                            }
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
    fun recordCount(userJwt: String, date:String) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordCountView.onRecordCountLoading()

        recordService.recordCount(userJwt, date).enqueue(object :
            Callback<RecordCountResponse> {

            override fun onResponse(
                call: Call<RecordCountResponse>,
                response: Response<RecordCountResponse>
            ) {
                if (response.body() == null) {
                    recordCountView.onRecordCountFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
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
        folderResultListView.onFolderResultListLoading()
        recordService.getFolderResultList(userJwt, date).enqueue(object :
            Callback<FolderResultListResponse> {
            override fun onResponse(
                call: Call<FolderResultListResponse>,
                response: Response<FolderResultListResponse>
            ) {
                if (response.body() == null) {
                    folderResultListView.onFolderResultListFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
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
            }

            override fun onFailure(call: Call<FolderResultListResponse>, t: Throwable) {
                folderResultListView.onFolderResultListFailure(400, t.toString())
                Log.d("CALENDARGETADV", t.toString())
            }
        })
    }
    //폴더기록하기
    fun putFolderRecord(userJwt: String, folderIdx: Int, folderRecording: FolderRecordingRequest) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.putFolderRecord(userJwt, folderIdx, folderRecording)
            .enqueue(object :
                Callback<FolderRecordResponse> {
                override fun onResponse(
                    call: Call<FolderRecordResponse>,
                    response: Response<FolderRecordResponse>
                ) {
                    if (response.body() == null) {
                        folderRecordingView.onFolderRecordingFailure(0, "네트워크 연결에 실패하였습니다.")
                    } else {
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
                }

                override fun onFailure(call: Call<FolderRecordResponse>, t: Throwable) {
                    folderRecordingView.onFolderRecordingFailure(400, t.toString())
                    Log.d("CALENDARGETADV", t.toString())
                }
            })
    }
    //개별기록하기
    fun putSingleRecord(userJwt: String, randomResultIdx: Int, singlerRecording: SingleRecordingRequest) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.putSingleRecord(userJwt, randomResultIdx, singlerRecording)
            .enqueue(object :
                Callback<SingleRecordResponse> {
                override fun onResponse(
                    call: Call<SingleRecordResponse>,
                    response: Response<SingleRecordResponse>
                ) {
                    if (response.body() == null) {
                        singleRecordingView.onSingleRecordingFailure(0, "네트워크 연결에 실패하였습니다.")
                    } else {
                        Log.d("S_RECORDING/Response", response.toString())
                        val resp = response.body()!!
                        Log.d("S_RECORDING/Code", resp.code.toString())

                        if (resp.isSuccess) {
                            singleRecordingView.onSingleRecordingSuccess()
                        } else {
                            when (resp.code) {
                                2001 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    "회원 정보가 잘못되었습니다.")
                                2002 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                2000 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                3000 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                2032 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                2039 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                2040 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                2041 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                3043 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                3018 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                3007 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                2035 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                2036 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                2038 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                2037 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                                4000 -> singleRecordingView.onSingleRecordingFailure(resp.code,
                                    resp.message)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<SingleRecordResponse>, t: Throwable) {
                    singleRecordingView.onSingleRecordingFailure(400, t.toString())
                    Log.d("CHECK", t.toString())
                }
            })
    }

    //폴더조회
    fun getFolderLook(userJwt: String, folderIdx: Int) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        folderLookView.onFolderLookLoading()
        recordService.getFolderLook(userJwt, folderIdx).enqueue(object :
            Callback<FolderLookResponse> {
            override fun onResponse(
                call: Call<FolderLookResponse>,
                response: Response<FolderLookResponse>
            ) {
                if (response.body() == null) {
                    folderLookView.onFolderLookFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
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
            }

            override fun onFailure(call: Call<FolderLookResponse>, t: Throwable) {
                folderLookView.onFolderLookFailure(400, t.toString())
                Log.d("CALENDARGETADV", t.toString())
            }
        })
    }

    //개별조회
    fun getSingleLook(userJwt: String, randomResultIdx: Int) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        singleLookView.onSingleLookLoading()
        recordService.getSingleLook(userJwt, randomResultIdx).enqueue(object :
            Callback<SingleLookResponse> {
            override fun onResponse(
                call: Call<SingleLookResponse>,
                response: Response<SingleLookResponse>
            ) {
                if (response.body() == null) {
                    singleLookView.onSingleLookFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
                    Log.d("SINGLERESULT/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("SINGLERESULT/Code", resp.code.toString())

                    if (resp.isSuccess) {
                        singleLookView.onSingleLookSuccess(resp.result)
                    } else {
                        when (resp.code) {
                            2001 -> singleLookView.onSingleLookFailure(resp.code,
                                resp.message)
                            2031 -> singleLookView.onSingleLookFailure(resp.code,
                                resp.message)
                            2002 -> singleLookView.onSingleLookFailure(resp.code,
                                resp.message)
                            3017 -> singleLookView.onSingleLookFailure(resp.code,
                                resp.message)
                            3006 -> singleLookView.onSingleLookFailure(resp.code,
                                resp.message)
                            3038 -> singleLookView.onSingleLookFailure(resp.code,
                                resp.message)
                            3040 -> singleLookView.onSingleLookFailure(resp.code,
                                resp.message)
                            3035 -> singleLookView.onSingleLookFailure(resp.code,
                                resp.message)
                            4000 -> singleLookView.onSingleLookFailure(resp.code,
                                resp.message)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SingleLookResponse>, t: Throwable) {
                singleLookView.onSingleLookFailure(400, t.toString())
                Log.d("CHECK", t.toString())
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
                if (response.body() == null) {
                    folderDeleteView.onFolderDeleteFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
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
                if (response.body() == null) {
                    folderUpdateView.onFolderUpdateFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
                    Log.d("FOLDERUPDATE/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("FOLDERUPDATE/Code", resp.code.toString())

                    if (resp.isSuccess) {
                        folderUpdateView.onFolderUpdateSuccess()
                    } else {
                        when (resp.code) {
                            2001 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                                resp.message)
                            2012 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                                resp.message)
                            2011 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                                resp.message)
                            2013 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                                resp.message)
                            2002 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                                resp.message)
                            3017 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                                resp.message)
                            3006 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                                resp.message)
                            3018 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                                resp.message)
                            3007 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                                resp.message)
                            4000 -> folderUpdateView.onFolderUpdateFailure(resp.code,
                                resp.message)
                        }
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
                if (response.body() == null) {
                    folderBreakView.onFolderBreakFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
                    Log.d("FOLDERBREAK/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("FOLDERBREAK/Code", resp.code.toString())

                    if (resp.isSuccess) {
                        folderBreakView.onFolderBreakSuccess()
                    } else {
                        when (resp.code) {
                            2001 -> folderBreakView.onFolderBreakFailure(resp.code,
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
            }

            override fun onFailure(call: Call<FolderBreakResponse>, t: Throwable) {
                folderBreakView.onFolderBreakFailure(400, t.toString())
                Log.d("CHECK", t.toString())
            }
        })
    }

    //폴더기록수정
    fun putFolderModify(userJwt: String,folderModify : FolderModifyRequest, folderIdx: Int) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.putFolderModify(userJwt, folderModify,folderIdx).enqueue(object :
            Callback<FolderModifyResponse> {
            override fun onResponse(
                call: Call<FolderModifyResponse>,
                response: Response<FolderModifyResponse>
            ) {
                if (response.body() == null) {
                    folderModifyView.onFolderModifyFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
                    Log.d("FOLDERMODIFY/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("FOLDERMODIFY/Code", resp.code.toString())

                    if (resp.isSuccess) {
                        folderModifyView.onFolderModifySuccess()
                    } else {
                        when (resp.code) {
                            2001 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            2031 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            2039 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            2035 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            2040 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            2038 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            2041 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            2036 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            2002 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            3017 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            3006 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            3036 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            2037 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                            4000 -> folderModifyView.onFolderModifyFailure(resp.code,
                                resp.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<FolderModifyResponse>, t: Throwable) {
                folderModifyView.onFolderModifyFailure(400, t.toString())
            }
        })
    }

    //개별기록수정
    fun putSingleModify(userJwt: String,singleModify : SingleModifyRequest, singleIdx: Int) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.putSingleModify(userJwt, singleModify,singleIdx).enqueue(object :
            Callback<SingleModifyResponse> {
            override fun onResponse(
                call: Call<SingleModifyResponse>,
                response: Response<SingleModifyResponse>
            ) {
                if (response.body() == null) {
                    singleModifyView.onSingleModifyFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
                    Log.d("SINGLEMODIFY/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("SINGLEMODIFY/Code", resp.code.toString())

                    if (resp.isSuccess) {
                        singleModifyView.onSingleModifySuccess()
                    } else {
                        when (resp.code) {
                            2001 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            2032 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            2039 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            2035 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            2040 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            2038 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            2041 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            2036 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            2013 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            3018 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            3007 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            3037 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            2037 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                            4000 -> singleModifyView.onSingleModifyFailure(resp.code,
                                resp.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<SingleModifyResponse>, t: Throwable) {
                singleModifyView.onSingleModifyFailure(400, t.toString())
            }
        })
    }

    fun putFolderDelete(userJwt: String, folderIdx: Int) {
        val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
        recordService.putFolderDelete(userJwt, folderIdx).enqueue(object :
            Callback<FolderrecordingDeleteResponse> {
            override fun onResponse(
                call: Call<FolderrecordingDeleteResponse>,
                response: Response<FolderrecordingDeleteResponse>
            ) {
                if (response.body() == null) {
                    folderrecordingDeleteView.onFolderrecordingDeleteFailure(0, "네트워크 연결에 실패하였습니다.")
                } else {
                    Log.d("FOLDERDelete/Response", response.toString())
                    val resp = response.body()!!
                    Log.d("FOLDERDelete/Code", resp.code.toString())

                    if (resp.isSuccess) {
                        folderrecordingDeleteView.onFolderrecordingDeleteSuccess()
                    } else {
                        when (resp.code) {
                            2001 -> folderrecordingDeleteView.onFolderrecordingDeleteFailure(resp.code,
                                "회원 정보가 잘못되었습니다.")
                            2031 -> folderrecordingDeleteView.onFolderrecordingDeleteFailure(resp.code,
                                resp.message)
                            2002 -> folderrecordingDeleteView.onFolderrecordingDeleteFailure(resp.code,
                                resp.message)

                            3017 -> folderrecordingDeleteView.onFolderrecordingDeleteFailure(resp.code,
                                resp.message)
                            3006 -> folderrecordingDeleteView.onFolderrecordingDeleteFailure(resp.code,
                                resp.message)
                            3036 -> folderrecordingDeleteView.onFolderrecordingDeleteFailure(resp.code,
                                "삭제할 기록이 존재하지 않아!")
                            4000 -> folderrecordingDeleteView.onFolderrecordingDeleteFailure(resp.code,
                                resp.message)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FolderrecordingDeleteResponse>, t: Throwable) {
                folderrecordingDeleteView.onFolderrecordingDeleteFailure(400, t.toString())
            }
        })
    }

            fun putSingleDelete(userJwt: String, singleIdx: Int) {
                val recordService = getRetrofit().create(RecordRetrofitInterface::class.java)
                recordService.putSingleDelete(userJwt, singleIdx).enqueue(object :
                    Callback<SinglerecordingDeleteResponse> {
                    override fun onResponse(
                        call: Call<SinglerecordingDeleteResponse>,
                        response: Response<SinglerecordingDeleteResponse>
                    ) {
                        if (response.body() == null) {
                            singlerecordingDeleteView.onSinglerecordingDeleteFailure(0, "네트워크 연결에 실패하였습니다.")
                        } else {
                            Log.d("SINGLEDELETE/Response", response.toString())
                            val resp = response.body()!!
                            Log.d("SINGLEDELETE/Code", resp.code.toString())

                            if (resp.isSuccess) {
                                singlerecordingDeleteView.onSinglerecordingDeleteSuccess()
                            } else {
                                when (resp.code) {
                                    2001 -> singlerecordingDeleteView.onSinglerecordingDeleteFailure(
                                        resp.code,
                                        "회원 정보가 잘못되었습니다.")
                                    2032 -> singlerecordingDeleteView.onSinglerecordingDeleteFailure(
                                        resp.code,
                                        resp.message)
                                    2013 -> singlerecordingDeleteView.onSinglerecordingDeleteFailure(
                                        resp.code,
                                        resp.message)
                                    3018 -> singlerecordingDeleteView.onSinglerecordingDeleteFailure(
                                        resp.code,
                                        resp.message)
                                    3037 -> singlerecordingDeleteView.onSinglerecordingDeleteFailure(
                                        resp.code,
                                        "삭제할 기록이 존재하지 않아!")
                                    3007 -> singlerecordingDeleteView.onSinglerecordingDeleteFailure(
                                        resp.code,
                                        resp.message)
                                    4000 -> singlerecordingDeleteView.onSinglerecordingDeleteFailure(
                                        resp.code,
                                        resp.message)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<SinglerecordingDeleteResponse>, t: Throwable) {
                        singlerecordingDeleteView.onSinglerecordingDeleteFailure(400, t.toString())
                    }
                })
            }
}
