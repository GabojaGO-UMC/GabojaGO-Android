package org.techtown.gabojago.menu.record.recordRetrofit
import android.net.Uri
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

data class SingleResult(
    @SerializedName("date") val date: String,
    @SerializedName("recordingList_each") val recordingList_each:ArrayList<SingleResultListResult>
)

//개별기록 리스폰
data class SingleResultListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SingleResult
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

data class FolderResult(
    @SerializedName("date") val date: String,
    @SerializedName("recordingList_folder") val recordingList_folder: ArrayList<FolderResultList>
)

//폴더기록 리스폰
data class FolderResultListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: FolderResult
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
    @SerializedName("recordingImgUrl") val recordingImgUrl:ArrayList<String>
)



//개별기록하기
data class SingleRecordingRequest(
    @SerializedName("recordingStar") val recordingStar:Double,
    @SerializedName("recordingContent") val recordingContent:String,
    @SerializedName("recordingTitle") val recordingTitle:String,
    @SerializedName("recordingImgUrl") val recordingImgUrl:List<String>
)

//개별기록하기리스폰
data class SingleRecordResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

//폴더기록하기 리스폰
data class FolderRecordResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

//폴더기록조회 리스폰
data class FolderLookResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: FolderLookResult
)

//개별기록조회 리스폰
data class SingleLookResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SingleLookResult
)

//개별기록조회
data class SingleLookResult(
    @SerializedName("contentCheck") val contentCheck: Boolean,
    @SerializedName("eachContentResult") val eachContentResult: SingleContentResult,
    @SerializedName("folderContentResult") val folderContentResult: FolderContentResult,
    @SerializedName("imgListCheck") val imageListCheck: Boolean,
    @SerializedName("eachImgListResult") val eachImgListResult: ArrayList<SingleRecordingImgUrl>,
    @SerializedName("eachRandomResult") val eachRandomResult: SingleRecordResult
)
data class SingleRecordingImgUrl(
    @SerializedName("recordingImgUrl") val recordingImgUrl:String
)

//폴더기록조회
data class FolderLookResult(
    @SerializedName("contentCheck") val contentCheck: Boolean,
    @SerializedName("folderContentResult") val folderContentResult: FolderContentResult,
    @SerializedName("imgListCheck") val imageListCheck: Boolean,
    @SerializedName("folderImgListResult") val folderImgListResult: ArrayList<FolderRecordingImgUrl>,
    @SerializedName("folderRandomResult") val folderResultList: ArrayList<FolderRecordResultList>
)

data class FolderRecordingImgUrl(
    @SerializedName("recordingImgUrl") val recordingImgUrl:String
)

//폴더 기록조회 별점, 내용, 제목
data class FolderContentResult(
    @SerializedName("recordingStar") val recordingStar: Double,
    @SerializedName("recordingContent") val recordingContent: String,
    @SerializedName("recordingTitle") val recordingTitle: String
)

//개별 기록조회 별점, 내용, 제목
data class SingleContentResult(
    @SerializedName("recordingStar") val recordingStar: Double,
    @SerializedName("recordingContent") val recordingContent: String,
    @SerializedName("recordingTitle") val recordingTitle: String
)

//폴더 기록 조회 기록리스트
data class FolderRecordResultList(
    @SerializedName("createAt") val creatAt: String,
    @SerializedName("randomResultContent") val randomResultContent: String,
    @SerializedName("randomResultType") val randomResultType: Int
)

//개별 기록 조회 기록리스트
data class SingleRecordResult(
    @SerializedName("createAt") val creatAt: String,
    @SerializedName("randomResultContent") val randomResultContent: String,
    @SerializedName("randomResultType") val randomResultType: Int
)

//폴더 삭제 리스폰
data class FolderDeleteResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

//폴더 삭제 request
data class FolderDeleteRequest(
    @SerializedName("randomResultList") val randomResultList: List<Int>,
    @SerializedName("folderList") val folderList: List<Int>
)

//폴더 목록수정리스폰
data class FolderUpdateResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

//폴더 목록수정 request
data class FolderUpdateRequest(
    @SerializedName("folderIdx") val folderIdx: Int,
    @SerializedName("plus_randomResultIdx") val plus_randomResultIdx: List<Int>,
    @SerializedName("minus_randomResultIdx") val minus_randomResultIdx: List<Int>
)

//폴더 해체 리스폰
data class FolderBreakResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

//폴더 기록수정 request
data class FolderModifyRequest(
    @SerializedName("recordingStar") val recordingStar: Double,
    @SerializedName("recordingContent") val recordingContent: String,
    @SerializedName("recordingTitle") val recordingTitle: String,
    @SerializedName("recordingImgUrl") val recordingImgUrl: ArrayList<String>

)

//폴더 기록수정 리스폰
data class FolderModifyResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

//개별 기록수정 request
data class SingleModifyRequest(
    @SerializedName("recordingStar") val recordingStar: Double,
    @SerializedName("recordingContent") val recordingContent: String,
    @SerializedName("recordingTitle") val recordingTitle: String,
    @SerializedName("recordingImgUrl") val recordingImgUrl: ArrayList<String>
)

//개별 기록수정 리스폰
data class SingleModifyResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)


data class FolderrecordingDeleteResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

data class SinglerecordingDeleteResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

