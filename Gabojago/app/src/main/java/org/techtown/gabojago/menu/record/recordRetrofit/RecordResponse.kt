package org.techtown.gabojago.menu.record.recordRetrofit
import com.google.gson.annotations.SerializedName


//폴더생성
data class RecordFolderMakeResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

//개별기록결과-리스트
data class RandomResultListResult(
    @SerializedName("randomResultIdx") val randomResultIdx: Int,
    @SerializedName("randomResultType") val randomResultType: Int,
    @SerializedName("randomResultContent") val randomResultContent: String,
    @SerializedName("createAt") val createAt: String
)

//개별기록 hasrecording & 리스트클래스
data class SingleResultListResult(
    @SerializedName("hasRecording") val hasRecording: Boolean,
    @SerializedName("randomResultListResult") val randomResultListResult: RandomResultListResult,
)

//개별기록 리스폰
data class SingleResultListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<SingleResultListResult>
)

//폴더기록 리스트
data class InFolderListResult(
    @SerializedName("randomResultIdx") val resultIdx: Int,
    @SerializedName("randomResultType") val resultType: Int,
    @SerializedName("randomResultContent") val resultContent: String,
    @SerializedName("createAt") val createAt: String
)

//폴더기록 내용& 리스트클래스
data class FolderResultList(
    @SerializedName("folderIdx") val folderIdx: Int,
    @SerializedName("hasRecording") val hasRecording: Boolean,
    @SerializedName("folderTitle") val folderTitle: String,
    @SerializedName("folderListResult") val randomResultListResult: ArrayList<InFolderListResult>,
)

//폴더기록 리스폰
data class FolderResultListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<FolderResultList>
)

//기록개수 리스폰
data class RecordCountResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Int
)

//폴더생성 보내기
data class randomResultRequest(
    @SerializedName("randomResultIdx") val randResultIdx:List<Int>
)
//폴더기록하기
data class FolderRecordingRequest(
    @SerializedName("recordingStar") val recordingStar:Double,
    @SerializedName("recordingContent") val recordingContent:String,
    @SerializedName("recordingTitle") val recordingTitle:String,
    @SerializedName("recordingImgUrl") val recordingImgUrl:List<String>
)

//폴더기록하기 리스폰
data class FolderRecordResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

//폴더기록조회
data class FolderLookResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: FolderLookResult
)

data class FolderLookResult(
    @SerializedName("folderContentResult") val folderContentResult: FolderContentResult,
    @SerializedName("folderRandomResult") val folderResultList: ArrayList<FolderRecordResultList>
)

data class FolderContentResult(
    @SerializedName("recordingStar") val recordingStar: Double,
    @SerializedName("recordingContent") val recordingContent: String,
    @SerializedName("recordingTitle") val recordingTitle: String
)

data class FolderRecordResultList(
    @SerializedName("createAt") val creatAt: String,
    @SerializedName("randomResultContent") val randomResultContent: String,
    @SerializedName("randomResultType") val randomResultType: Int
)

data class FolderDeleteResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

data class FolderDeleteRequest(
    @SerializedName("randomResultList") val randomResultList: List<Int>,
    @SerializedName("folderList") val folderList: List<Int>
)

data class FolderUpdateResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

data class FolderUpdateRequest(
    @SerializedName("folderIdx") val folderIdx: Int,
    @SerializedName("plus_randomResultIdx") val plus_randomResultIdx: List<Int>,
    @SerializedName("minus_randomResultIdx") val minus_randomResultIdx: List<Int>
)

data class FolderBreakResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

