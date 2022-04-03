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


    //폴더기록조회
    @GET("/app/recording/folder/{folderIdx}")
    fun getFolderLook(
        @Header("x-access-token") xAccessToken: String,
        @Path("folderIdx") folderIdx: Int
    ): Call<FolderLookResponse>

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

    @DELETE("/app/folder/delete/{folderIdx}")
    fun putBreakFolderIdx(
        @Header("x-access-token") xAccessToken: String,
        @Path("folderIdx") folderIdx: Int
    ): Call<FolderBreakResponse>


}