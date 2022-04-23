package org.techtown.gabojago.menu.record

import HorizontalItemDecorator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ActivityMainBinding
import org.techtown.gabojago.databinding.FragmentRecordBinding
import org.techtown.gabojago.databinding.ItemRecordFoldernameBinding
import org.techtown.gabojago.databinding.ItemRecordWeekBinding
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.main.MyToast
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.home.contents.HomeFragment
import org.techtown.gabojago.menu.record.calendar.CalendarActivity
import org.techtown.gabojago.menu.record.dialog.DialogFolderDelete
import org.techtown.gabojago.menu.record.dialog.DialogFolderModify
import org.techtown.gabojago.menu.record.dialog.DialogFolderSelect
import org.techtown.gabojago.menu.record.dialog.DialogRealBreakup
import org.techtown.gabojago.menu.record.look.RecordLookFragment
import org.techtown.gabojago.menu.record.recordRetrofit.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule


//기록하자 메인화면
class RecordFragment : Fragment(), RecordCountView, SingleResultListView, FolderResultListView{
    private lateinit var callback: OnBackPressedCallback
    lateinit var binding: FragmentRecordBinding
    lateinit var binding2: ItemRecordFoldernameBinding
    lateinit var binding3: ItemRecordWeekBinding

    var records = ArrayList<SingleResultListResult>()
    var folders = ArrayList<FolderResultList>()
    var add: Boolean = true
    val recordService = RecordService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        binding2 = ItemRecordFoldernameBinding.inflate(inflater, container, false)
        binding3 = ItemRecordWeekBinding.inflate(inflater, container, false)

        recordService.setRecordCountView(this@RecordFragment)
        recordService.setSingleResultListView(this@RecordFragment)
        recordService.setFolderResultListView(this@RecordFragment)

        val userJwt = getJwt(requireContext(), "userJwt")
        //캘린더 액티비티에서 값이 넘어왔을 경우
        val specialDate = arguments?.getString("changeDate")
        val pickDate = arguments?.getString("pickDate")
        if (specialDate != null) {
            //날짜, 구분선, 메시지 조정
            binding.recordMonthTv.setText("< "+specialDate.substring(4,6).toInt().toString()+"월")
            binding.recordDateTv.setText(specialDate.substring(0,4)+"년 "+specialDate.substring(4,6).toInt().toString()+"월 "+specialDate.substring(6).toInt().toString()+"일")
            binding.recordDivisionView.visibility = View.VISIBLE
            binding.recordEmptyTv.visibility = View.VISIBLE
            binding.recordNotifyTv.visibility = View.GONE

            //주간캘린더 다시 불러오기
            binding.recordWeekRecyclerview.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val recordWeekRVAdapter = RecordWeekRVAdapter(specialDate)
            binding.recordWeekRecyclerview.adapter = recordWeekRVAdapter
            //주간캘린더 클릭이벤트
            recordWeekRVAdapter.setMyItemClickListener(object :
                RecordWeekRVAdapter.MyItemClickListener {
                override fun onItemClick(day: String) {
                    binding.recordDivisionView.visibility = View.VISIBLE
                    binding.recordEmptyTv.visibility= View.VISIBLE
                    binding.recordNotifyTv.visibility = View.GONE

                    //날짜별로 메인화면 레트로핏 다시 불러오기
                    recordService.getSingleResultList(userJwt,day)
                    recordService.getFolderResultList(userJwt,day)
                    recordService.recordCount(userJwt,day)
                    binding.recordMonthTv.setText("< "+day.substring(4,6).toInt().toString()+"월")
                    binding.recordDateTv.setText(day.substring(0,4)+"년 "+day.substring(4,6).toInt().toString()+"월 "+day.substring(6).toInt().toString()+"일")
                }
            })

            //메인화면 레트로핏 다시 불러오기
            recordService.recordCount(userJwt,specialDate)
            recordService.getSingleResultList(userJwt, specialDate)
            recordService.getFolderResultList(userJwt, specialDate)

        }else if(pickDate!=null){
            //날짜, 구분선, 메시지 조정
            binding.recordMonthTv.setText("< "+pickDate.substring(4,6).toInt().toString()+"월")
            binding.recordDateTv.setText(pickDate.substring(0,4)+"년 "+pickDate.substring(4,6).toInt().toString()+"월 "+pickDate.substring(6).toInt().toString()+"일")
            binding.recordDivisionView.visibility = View.VISIBLE
            binding.recordEmptyTv.visibility = View.VISIBLE
            binding.recordNotifyTv.visibility = View.GONE
            //주간캘린더 다시 불러오기
            binding.recordWeekRecyclerview.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val recordWeekRVAdapter = RecordWeekRVAdapter(pickDate)
            binding.recordWeekRecyclerview.adapter = recordWeekRVAdapter
            //주간캘린더 클릭이벤트
            recordWeekRVAdapter.setMyItemClickListener(object :
                RecordWeekRVAdapter.MyItemClickListener {
                override fun onItemClick(day: String) {
                    binding.recordDivisionView.visibility = View.VISIBLE
                    binding.recordEmptyTv.visibility= View.VISIBLE
                    binding.recordNotifyTv.visibility = View.GONE

                    //날짜별로 메인화면 레트로핏 다시 불러오기
                    recordService.getSingleResultList(userJwt,day)
                    recordService.getFolderResultList(userJwt,day)
                    recordService.recordCount(userJwt,day)
                    binding.recordMonthTv.setText("< "+day.substring(4,6).toInt().toString()+"월")
                    binding.recordDateTv.setText(day.substring(0,4)+"년 "+day.substring(4,6).toInt().toString()+"월 "+day.substring(6).toInt().toString()+"일")
                }
            })

            //메인화면 레트로핏 다시 불러오기
            recordService.recordCount(userJwt,pickDate)
            recordService.getSingleResultList(userJwt, pickDate)
            recordService.getFolderResultList(userJwt, pickDate)
        } else{
            //캘린더액티비티에서 값이 넘어오지 않는경우(오늘날짜 default)
            init()
            val now: Long = System.currentTimeMillis()
            val date = Date(now)
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
            val stringDate = dateFormat.format(date)

            binding.recordWeekRecyclerview.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            val recordWeekRVAdapter = RecordWeekRVAdapter(stringDate)
            binding.recordWeekRecyclerview.adapter = recordWeekRVAdapter

            recordWeekRVAdapter.setMyItemClickListener(object :
                RecordWeekRVAdapter.MyItemClickListener {
                override fun onItemClick(day: String) {
                    binding.recordDivisionView.visibility = View.VISIBLE
                    binding.recordEmptyTv.visibility = View.VISIBLE
                    binding.recordNotifyTv.visibility = View.GONE
                    recordService.getSingleResultList(userJwt, day)
                    recordService.getFolderResultList(userJwt, day)
                    recordService.recordCount(userJwt, day)
                    binding.recordMonthTv.setText("< " + day.substring(4, 6).toInt()
                        .toString() + "월")
                    binding.recordDateTv.setText(day.substring(0, 4) + "년 " + day.substring(4,
                        6).toInt().toString() + "월 " + day.substring(6).toInt()
                        .toString() + "일")
                }
            })
            recordService.getSingleResultList(userJwt,stringDate)
            recordService.getFolderResultList(userJwt,stringDate)
            recordService.recordCount(userJwt,stringDate)
        }
        //메인화면 폴더, 개별 리사이클러뷰 레이아웃 매니저
        binding.recordFolderresultRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recordResultRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //화면 넓이
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        binding.recordWeekRecyclerview.addItemDecoration(HorizontalItemDecorator(width/75,height/200))
        //클릭 이벤트 함수
        clickevent()

        return binding.root
    }

    private fun clickevent() {
        //해당 월 text 클릭 이벤트(캘린터 액티비티로 넘어가기)
        binding.recordMonthTv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, CalendarActivity())
                .commitAllowingStateLoss()
        }
        //더보기 버튼 클릭 이벤트-> 블러뷰 나타내기
        binding.recordMoreIv.setOnClickListener {
            binding.recordBlurView.visibility = View.VISIBLE
            popupMenu()
        }
        //닫기 버튼 클릭 이벤트-> 블러뷰 닫기
        binding.recordCloseIv.setOnClickListener {
            binding.recordBlurView.visibility = View.GONE
            popupMenu()
        }
        //블러뷰 클릭 이벤트-> 블러뷰 닫기
        binding.recordBlurView.setOnClickListener {
            binding.recordBlurView.visibility = View.GONE
            popupMenu()
        }
        binding.recordTodayIv.setOnClickListener{
            binding.recordDivisionView.visibility = View.VISIBLE
            binding.recordEmptyTv.visibility= View.VISIBLE
            binding.recordNotifyTv.visibility = View.GONE
            val now: Long = System.currentTimeMillis()
            val date = Date(now)
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
            val stringDate = dateFormat.format(date)
            val userJwt = getJwt(requireContext(), "userJwt")
            val recordWeekRVAdapter = RecordWeekRVAdapter(stringDate)
            binding.recordWeekRecyclerview.adapter = recordWeekRVAdapter

            recordService.getSingleResultList(userJwt,stringDate)
            recordService.getFolderResultList(userJwt,stringDate)
        }
    }

    private fun init() {
        //시작시 해당월, 날짜 (오늘날짜로 default)
        binding.recordDateTv.setText(setdate().substring(0,4)+"년 "+setdate().substring(4,6).toInt().toString()+"월 "+setdate().substring(6).toInt().toString()+"일")
        binding.recordMonthTv.setText(setMonth())
    }

    //개별기록 프래그먼트 이동 함수
    private fun changeSingleRecordFragment(hasRecording:Boolean,recordIdx: Int,result:RandomResultListResult,day: String) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, SingleRecordFragment(hasRecording,recordIdx,result,day))
            .addToBackStack(null)
            .commitAllowingStateLoss()

    }
    //폴더기록 프래그먼트 이동 함수
    private fun changeFolderRecordFragment(hasRecording: Boolean,folderIdx:Int, resultList:ArrayList<InFolderListResult>,day:String) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, FolderRecordFragment(hasRecording,folderIdx,resultList,day))
            .addToBackStack(null)
            .commitAllowingStateLoss()

    }
    //개별, 폴더 기록조회 프래그먼트 이동 함수
    private fun changeRecordFragment(hasRecording: Boolean,folderIdx: Int,day : String){
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, RecordLookFragment(hasRecording,folderIdx,day))
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
    //오늘 날짜 설정 함수
    private fun setdate(): String {
        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
        val stringDate = dateFormat.format(date)

        return stringDate
    }
    //해당월 설정 함수
    private fun setMonth(): String {
        val now: Long = System.currentTimeMillis()
        val month = Date(now)
        val monthFormat = SimpleDateFormat("MM", Locale("ko", "KR"))
        val stringMonth = "< "+(monthFormat.format(month).toInt()).toString()+"월"

        return stringMonth
    }

    //더보기 메뉴 애니메이션 함수
    private fun popupMenu() {

        var px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, this.resources.displayMetrics)

        if (add) {

            var writeAnimator = ObjectAnimator.ofFloat(binding.recordFolderplusIv, "translationY", 0f, -px)
            writeAnimator.duration = 400
            writeAnimator.interpolator = OvershootInterpolator()
            writeAnimator.target = binding.recordFolderplusIv
            writeAnimator.start()

            var photoAnimator = ObjectAnimator.ofFloat(binding.recordTrashIv, "translationY", 0f, -px*2)
            photoAnimator.duration = 500
            photoAnimator.interpolator = OvershootInterpolator()
            photoAnimator.target = binding.recordTrashIv
            photoAnimator.start()

            binding.recordMoreIv.visibility = View.GONE
            binding.recordCloseIv.visibility = View.VISIBLE
            add = !add
        } else {

            var writeAnimator = ObjectAnimator.ofFloat(binding.recordFolderplusIv, "translationY", -px, 0f)
            writeAnimator.duration = 400
            writeAnimator.interpolator = OvershootInterpolator()
            writeAnimator.target = binding.recordFolderplusIv
            writeAnimator.start()

            var photoAnimator = ObjectAnimator.ofFloat(binding.recordTrashIv, "translationY", -px*2, 0f)
            photoAnimator.duration = 500
            photoAnimator.interpolator = OvershootInterpolator()
            photoAnimator.target = binding.recordTrashIv
            photoAnimator.start()

            binding.recordMoreIv.visibility = View.VISIBLE
            binding.recordCloseIv.visibility = View.GONE

            add = !add
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val mainContext = context as MainActivity
                mainContext.binding.mainBnv.selectedItemId = R.id.homeFragment
                Log.e("back","backpress")
                mainContext.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment())
                    .commitAllowingStateLoss()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onRecordCountLoading() {
        binding.recordCountTv.text = "0"
    }

    override fun onRecordCountSuccess(result: Int) {
        binding.recordCountTv.text = result.toString()
    }

    override fun onRecordCountFailure(code: Int, message: String) {
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
    }

    override fun onSingleResultListSuccess(result: SingleResult) {
        binding.recordLoadingPb.visibility = View.GONE
        binding.recordBlurView.visibility = View.GONE
        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
        val stringDate = dateFormat.format(date)
        Log.e("날짜",result.date)
        Log.e("날짜2",stringDate)

        if(result.date.toInt()!=stringDate.toInt()){
            binding.recordTodayIv.visibility = View.VISIBLE
        }else{
            binding.recordTodayIv.visibility = View.GONE
        }

        records = result.recordingList_each
        val recordResultRVAdapter = RecordResultRVAdapter(records)
        binding.recordResultRecyclerview.adapter = recordResultRVAdapter

        if(recordResultRVAdapter.itemCount==0){
            binding.recordDivisionView.visibility = View.GONE
        }
        if(recordResultRVAdapter.itemCount>0){
            binding.recordNotifyTv.visibility = View.VISIBLE
            binding.recordEmptyTv.visibility = View.GONE
        }



        binding.recordFolderplusIv.setOnClickListener{
            DialogFolderSelect(records).show((context as MainActivity).supportFragmentManager,"dialog")
        }

        binding.recordTrashIv.setOnClickListener{
            DialogFolderDelete(records,folders).show((context as MainActivity).supportFragmentManager,"dialog")
        }

        recordResultRVAdapter.setMyItemClickListener(object :
            RecordResultRVAdapter.MyItemClickListener {
            override fun onItemClick(hasRecording: Boolean,recordIdx :Int,resultList:RandomResultListResult) {
                changeSingleRecordFragment(hasRecording,recordIdx,resultList,result.date)
            }
            override fun onItemView(hasRecording: Boolean,randomResultIdx:Int) {
                changeRecordFragment(hasRecording,randomResultIdx,result.date)
            }
        })
    }

    override fun onSingleResultListLoading() {
        binding.recordBlurView.visibility = View.VISIBLE
        binding.recordLoadingPb.visibility = View.VISIBLE
    }

    override fun onSingleResultListFailure(code: Int, message: String) {

        val empty = ArrayList<SingleResultListResult>()
        val recordResultRVAdapter = RecordResultRVAdapter(empty)
        binding.recordResultRecyclerview.adapter = recordResultRVAdapter
        Log.e("기록하자개별메인api",message)
        binding.recordDivisionView.visibility = View.GONE
        binding.recordTodayIv.visibility = View.GONE
    }

    override fun onFolderResultListSuccess(result: FolderResult) {
        binding.recordLoadingPb.visibility = View.GONE
        binding.recordBlurView.visibility = View.GONE
        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
        val stringDate = dateFormat.format(date)
        if(result.date.toInt()!=stringDate.toInt()){
            binding.recordTodayIv.visibility = View.VISIBLE
        }else{
            binding.recordTodayIv.visibility = View.GONE
        }

        folders = result.recordingList_folder
        val recordFolderResultNameRVAdapter = RecordFolderResultNameRVAdapter(folders)
        binding.recordFolderresultRecyclerview.adapter = recordFolderResultNameRVAdapter

        if(recordFolderResultNameRVAdapter.itemCount==0){
            binding.recordDivisionView.visibility = View.GONE
        }

        if(recordFolderResultNameRVAdapter.itemCount>0){
            binding.recordNotifyTv.visibility = View.VISIBLE
            binding.recordEmptyTv.visibility = View.GONE
        }

        binding.recordTrashIv.setOnClickListener{
            DialogFolderDelete(records,folders).show((context as MainActivity).supportFragmentManager,"dialog")
        }

        recordFolderResultNameRVAdapter.setMyItemClickListener(object :
            RecordFolderResultNameRVAdapter.MyItemClickListener {
            override fun onItemClickPencil(hasRecording: Boolean,folderIdx:Int,resultList:ArrayList<InFolderListResult>) {
                changeFolderRecordFragment(hasRecording,folderIdx,resultList,result.date)
            }

            override fun onItemView(hasRecording: Boolean,folderIdx:Int) {
                changeRecordFragment(hasRecording,folderIdx,result.date)
            }

            override fun onModifyClick(folder: FolderResultList) {
                val dialogFolderModify = DialogFolderModify(folder,records)
                dialogFolderModify.show((context as MainActivity).supportFragmentManager,"dialog")
                binding.recordBlurView.visibility = View.VISIBLE
                dialogFolderModify.setOnDismissClickListener(object :
                DialogFolderModify.onDismissListener{
                    override fun onDismiss(dialogFragment: DialogFragment) {
                        binding.recordBlurView.visibility = View.GONE
                    }
                })
            }

            override fun onBreakUpClick(folderIdx: Int) {
                val dialogRealBreakup = DialogRealBreakup(folderIdx)
                dialogRealBreakup.show((context as MainActivity).supportFragmentManager,"dialog")
                binding.recordBlurView.visibility = View.VISIBLE
                dialogRealBreakup.setOnDismissClickListener(object :
                    DialogRealBreakup.onDismissListener{
                    override fun onDismiss(dialogFragment: DialogFragment) {
                        binding.recordBlurView.visibility = View.GONE
                    }
                })
            }
        })
    }

    override fun onFolderResultListLoading() {
        binding.recordLoadingPb.visibility = View.VISIBLE
        binding.recordBlurView.visibility = View.VISIBLE
    }

    override fun onFolderResultListFailure(code: Int, message: String) {
        val empty = ArrayList<FolderResultList>()
        val recordFolderResultNameRVAdapter = RecordFolderResultNameRVAdapter(empty)
        binding.recordFolderresultRecyclerview.adapter = recordFolderResultNameRVAdapter
        Log.e("기록하자폴더메인api",message)
        binding.recordDivisionView.visibility = View.GONE
        binding.recordTodayIv.visibility = View.GONE
    }
}

