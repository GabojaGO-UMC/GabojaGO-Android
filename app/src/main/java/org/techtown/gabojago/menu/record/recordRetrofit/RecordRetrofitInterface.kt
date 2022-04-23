package org.techtown.gabojago.menu.record.recordRetrofit

import retrofit2.Call
import retrofit2.http.*

interface RecordRetrofitInterface {
    //개별기록(메인)
    @GET("/app/recordingList/each/{date}")
    fun getSingleResultList(
        @Header("x-access-token") xAccessToken: String,
        @Path("date") date: String
    ): Call<SingleResultListResponse>

    //폴더생성
    @POST("/app/folder/new")
    fun putFolderMakeIdx(
        @Header("x-access-token") xAccessToken: String,
        @Body randomResultIdx : randomResultRequest
    ): Call<RecordFolderMakeResponse>

    //기록개수
    @GET("/app/randomresultcount/{date}")
    fun recordCount(
        @Header("x-access-token") xAccessToken: String,
        @Path("date") date: String
    ): Call<RecordCountResponse>

    //폴더기록(메인)
    @GET("/app/recordingList/folder/{date}")
    fun getFolderResultList(
        @Header("x-access-token") xAccessToken: String,
        @Path("date") date: String
    ): Call<FolderResultListResponse>

    //폴더기록하기
   @POST("/app/folderrecording/{folderIdx}")
    fun putFolderRecord(
        @Header("x-access-token") xAccessToken: String,
        @Path("folderIdx") folderIdx: Int,
        @Body folderRecording : FolderRecordingRequest
    ): Call<FolderRecordResponse>

    //개별기록하기
    @POST("/app/eachrecording/{randomResultIdx}")
    fun putSingleRecord(
        @Header("x-access-token") xAccessToken: String,
        @Path("randomResultIdx") randomResultIdx: Int,
        @Body singleRecording : SingleRecordingRequest
    ): Call<SingleRecordResponse>


    //폴더기록조회
    @GET("/app/recording/folder/{folderIdx}")
    fun getFolderLook(
        @Header("x-access-token") xAccessToken: String,
        @Path("folderIdx") folderIdx: Int
    ): Call<FolderLookResponse>

    //개별기록조회
    @GET("/app/recording/eachContent/{randomResultIdx}")
    fun getSingleLook(
        @Header("x-access-token") xAccessToken: String,
        @Path("randomResultIdx") randomResultIdx: Int
    ): Call<SingleLookResponse>

    //폴더 삭제
    @HTTP(method = "DELETE", path = "/app/randomResult", hasBody = true)
    fun putIdx(
        @Header("x-access-token") xAccessToken: String,
        @Body folderDelete : FolderDeleteRequest
    ): Call<FolderDeleteResponse>

    //폴더목록수정
    @PATCH("/app/folder/update")
    fun putUpdateFolderIdx(
        @Header("x-access-token") xAccessToken: String,
        @Body folderUpdate : FolderUpdateRequest
    ): Call<FolderUpdateResponse>

    //폴더해체
    @DELETE("/app/folder/delete/{folderIdx}")
    fun putBreakFolderIdx(
        @Header("x-access-token") xAccessToken: String,
        @Path("folderIdx") folderIdx: Int
    ): Call<FolderBreakResponse>

    //폴더기록수정
    @PATCH("/app/recording/folderCorrection/{folderIdx}")
    fun putFolderModify(
        @Header("x-access-token") xAccessToken: String,
        @Body folderModify : FolderModifyRequest,
        @Path("folderIdx") folderIdx: Int
    ): Call<FolderModifyResponse>

    //개별기록수정
    @PATCH("/app/recording/eachCorrection/{randomResultIdx}")
    fun putSingleModify(
        @Header("x-access-token") xAccessToken: String,
        @Body singleModify : SingleModifyRequest,
        @Path("randomResultIdx") randomResultIdx: Int
    ): Call<SingleModifyResponse>

    //기록하기프래그먼트 내 기록삭제(폴더)
    @PATCH("/app/recording/folderrecorddeletion/{folderIdx}")
    fun putFolderDelete(
        @Header("x-access-token") xAccessToken: String,
        @Path("folderIdx") folderIdx: Int
    ): Call<FolderrecordingDeleteResponse>

    //기록하기프래그먼트 내 기록삭제(개별)
    @PATCH("/app/recording/eachrecorddeletion/{randomResultIdx}")
    fun putSingleDelete(
        @Header("x-access-token") xAccessToken: String,
        @Path("randomResultIdx") randomResultIdx: Int
    ): Call<SinglerecordingDeleteResponse>


}