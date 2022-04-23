package org.techtown.gabojago.menu.record.look

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.FragmentRecordLookBinding
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.main.MyToast
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.record.RecordFragment
import org.techtown.gabojago.menu.record.recordRetrofit.*
import java.util.*

//기록조회 프래그먼트
class RecordLookFragment(private val hasRecording: Boolean, private val Idx:Int,private val day:String): Fragment() , FolderLookView , SingleLookView {
    private lateinit var callback: OnBackPressedCallback
    lateinit var binding: FragmentRecordLookBinding
    private val MIN_SCALE = 0.95f
    private val MIN_ALPHA = 0.5f
    var isOpened: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRecordLookBinding.inflate(inflater, container, false)

        init()
        val recordService = RecordService()
        recordService.setFolderLookView(this@RecordLookFragment)
        recordService.setSingleLookView(this@RecordLookFragment)
        val userJwt = getJwt(requireContext(), "userJwt")
        var imageList = ArrayList<String>()
        val name = "https://firebasestorage.googleapis.com/v0/b/gabojago-54fc6.appspot.com/o/images%2Fimage_background.png?alt=media&token=8d2965c1-5ab3-4d7f-964b-3918a0d01829"
        imageList.add(name)

        binding.recordLookPictureVp.adapter = RecordLookViewpagerAdapter(imageList)
        binding.recordLookPictureVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.recordLookPictureVp.setPageTransformer(ZoomOutPageTransformer())
        binding.recordLookCircleIndicator.setViewPager2(binding.recordLookPictureVp)
        if(hasRecording){
            recordService.getFolderLook(userJwt, Idx)
            recordService.getSingleLook(userJwt, Idx)
        } else{
            MyToast.createToast(
                requireContext(), "기록이 없는 항목이야!", 90, true
            ).show()
        }

        //기록리스트 여는 클릭이벤트
        binding.recordLookView.setOnClickListener {
            randomIsOpened(isOpened)
        }

        //이전페이지로
        binding.recordLookBackBtn.setOnClickListener {
            var recordFragment = RecordFragment()
            var bundle = Bundle()
            bundle.putString("pickDate", day)
            Log.e("date",day)
            recordFragment.arguments = bundle
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, recordFragment)
                .commitAllowingStateLoss()
        }
        return binding.root
    }
    private fun init() {
        binding.lookRecordBlurView.visibility = View.GONE
        binding.lookRecordLoadingPb.visibility = View.GONE
        hideBottomNavigation(true)
        binding.lookRecordBlurView.setOnTouchListener(OnTouchListener { v, event -> true })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(false)
    }

    //하단 네비게이션바 없애는 함수
    fun hideBottomNavigation(bool: Boolean) {
        val bottomNavigation: BottomNavigationView = requireActivity().findViewById(R.id.main_bnv)
        if (bool == true)
            bottomNavigation.visibility = View.GONE
        else
            bottomNavigation.visibility = View.VISIBLE
    }

    //뷰페이저 애니메이션
    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }

    //기록리스트 열린 여부 판단 함수
    private fun randomIsOpened(toOpen: Boolean) {
        if (toOpen) {
            binding.recordLookArrowBtn.setImageResource(R.drawable.result_look_arrow_clicked)
            binding.recordResultRecyclerview.visibility = View.VISIBLE
        } else {
            binding.recordLookArrowBtn.setImageResource(R.drawable.result_look_arrow)
            binding.recordResultRecyclerview.visibility = View.GONE
        }
        isOpened = !isOpened
    }

    //별점 세팅함수
    private fun setStarState(star: Double) {
        var starArr = arrayOf(
            binding.recordLookStar01LeftIv,
            binding.recordLookStar01RightIv,
            binding.recordLookStar02LeftIv,
            binding.recordLookStar02RightIv,
            binding.recordLookStar03LeftIv,
            binding.recordLookStar03RightIv,
            binding.recordLookStar04LeftIv,
            binding.recordLookStar04RightIv,
            binding.recordLookStar05LeftIv,
            binding.recordLookStar05RightIv
        )

        for(i in 0 until (star*2).toInt()){
            starArr[i].visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.e("back","backpress")
                var recordFragment = RecordFragment()
                var bundle = Bundle()
                bundle.putString("pickDate", day)
                Log.e("date",day)
                recordFragment.arguments = bundle
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, recordFragment)
                    .commitAllowingStateLoss()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onFolderLookLoading() {
        binding.lookRecordBlurView.visibility = View.VISIBLE
        binding.lookRecordLoadingPb.visibility = View.VISIBLE
    }

    //폴더조회성공
    override fun onFolderLookSuccess(result: FolderLookResult) {
        binding.lookRecordBlurView.visibility = View.GONE
        binding.lookRecordLoadingPb.visibility = View.GONE
        var imageList = ArrayList<String>()

        try {
            //기록내용이 존재하는지 여부 체크
            if(!result.contentCheck){
                binding.recordLookContentsTv.visibility = View.GONE
            }else{
                binding.recordLookContentsTv.visibility = View.VISIBLE
            }
            //이미지리스트 존재하는지 여부 체크
            if(!result.imageListCheck){
                binding.recordLookCircleIndicator.visibility = View.GONE
                val name = "https://firebasestorage.googleapis.com/v0/b/gabojago-54fc6.appspot.com/o/images%2Fimage_background.png?alt=media&token=8d2965c1-5ab3-4d7f-964b-3918a0d01829"
                imageList.add(name)
                //이미지 뷰페이저및 어댑터
                binding.recordLookPictureVp.adapter = RecordLookViewpagerAdapter(imageList)
                binding.recordLookPictureVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                binding.recordLookPictureVp.setPageTransformer(ZoomOutPageTransformer())
                binding.recordLookCircleIndicator.setViewPager2(binding.recordLookPictureVp)
            }else{
                val imageList = ArrayList<String>()

                for(i in 0 until result.folderImgListResult.size){
                    imageList.add(i,result.folderImgListResult[i].recordingImgUrl)
                }
                Log.e("폴더이미지조회",imageList.toString())

                //이미지 뷰페이저및 어댑터
                binding.recordLookCircleIndicator.visibility = View.VISIBLE
                binding.recordLookPictureVp.adapter = RecordLookViewpagerAdapter(imageList)
                binding.recordLookPictureVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                binding.recordLookPictureVp.setPageTransformer(ZoomOutPageTransformer())
                binding.recordLookCircleIndicator.setViewPager2(binding.recordLookPictureVp)
            }



            //기록제목, 내용, 별점, 기록리스트 세팅
            binding.recordLookNameTv.text = result.folderContentResult.recordingTitle
            setStarState(result.folderContentResult.recordingStar)
            Log.e("별점",result.folderContentResult.recordingStar.toString())
            binding.recordLookContentsTv.text = result.folderContentResult.recordingContent
            val recordLookRVAdapter = RecordLookRVAdapter(result.folderResultList)
            binding.recordResultRecyclerview.adapter = recordLookRVAdapter
        } catch (e: NullPointerException) {
            var imageList = ArrayList<String>()
            val name = "https://firebasestorage.googleapis.com/v0/b/gabojago-54fc6.appspot.com/o/images%2Fimage_background.png?alt=media&token=8d2965c1-5ab3-4d7f-964b-3918a0d01829"
            imageList.add(name)
            //null값으로 들어왔을 때 오류방지
            binding.recordLookNameTv.text = "제목이 비어있어!"
            setStarState(2.5)
            binding.recordLookContentsTv.visibility = View.GONE
            //기록리스트에 빈 배열 넣어놓기
            val emptyResult = ArrayList<FolderRecordResultList>()
            emptyResult.add(FolderRecordResultList("", "", 0))
            //이미지리스트에 빈 배열 넣어놓기
            val recordLookRVAdapter = RecordLookRVAdapter(emptyResult)
            binding.recordResultRecyclerview.adapter = recordLookRVAdapter
            binding.recordLookCircleIndicator.visibility = View.GONE

            binding.recordLookPictureVp.adapter = RecordLookViewpagerAdapter(imageList)
            binding.recordLookPictureVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            binding.recordLookPictureVp.setPageTransformer(ZoomOutPageTransformer())
            binding.recordLookCircleIndicator.setViewPager2(binding.recordLookPictureVp)


        }
    }

    //폴더기록조회실패
    override fun onFolderLookFailure(code: Int, message: String) {
        binding.lookRecordBlurView.visibility = View.GONE
        binding.lookRecordLoadingPb.visibility = View.GONE
        var imageList = ArrayList<String>()
        val name = "https://firebasestorage.googleapis.com/v0/b/gabojago-54fc6.appspot.com/o/images%2Fimage_background.png?alt=media&token=8d2965c1-5ab3-4d7f-964b-3918a0d01829"
        imageList.add(name)
        Log.e("폴더조회",message)

        binding.recordLookNameTv.text = "제목이 비어있어!"
        setStarState(2.5)
        binding.recordLookContentsTv.visibility = View.GONE
        binding.recordLookCircleIndicator.visibility = View.GONE

        val emptyResult = ArrayList<FolderRecordResultList>()
        emptyResult.add(FolderRecordResultList("", "", 0))

        val recordLookRVAdapter = RecordLookRVAdapter(emptyResult)
        binding.recordResultRecyclerview.adapter = recordLookRVAdapter


        binding.recordLookPictureVp.adapter = RecordLookViewpagerAdapter(imageList)
        binding.recordLookPictureVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.recordLookPictureVp.setPageTransformer(ZoomOutPageTransformer())
        binding.recordLookCircleIndicator.setViewPager2(binding.recordLookPictureVp)



    }

    override fun onSingleLookLoading() {
        binding.lookRecordBlurView.visibility = View.VISIBLE
        binding.lookRecordLoadingPb.visibility = View.VISIBLE
    }

    override fun onSingleLookSuccess(result: SingleLookResult) {
        binding.lookRecordBlurView.visibility = View.GONE
        binding.lookRecordLoadingPb.visibility = View.GONE
        var imageList = ArrayList<String>()
        try {
            if(!result.contentCheck){
                binding.recordLookContentsTv.visibility = View.GONE
            }else{
                binding.recordLookContentsTv.visibility = View.VISIBLE
            }
            if(!result.imageListCheck){
                binding.recordLookCircleIndicator.visibility = View.GONE
                val name = "https://firebasestorage.googleapis.com/v0/b/gabojago-54fc6.appspot.com/o/images%2Fimage_background.png?alt=media&token=8d2965c1-5ab3-4d7f-964b-3918a0d01829"
                imageList.add(name)
            }else{
                binding.recordLookCircleIndicator.visibility = View.VISIBLE

                for(i in 0 until result.eachImgListResult.size){
                    imageList.add(i,result.eachImgListResult[i].recordingImgUrl)
                }
            }
            //이미지 뷰페이저및 어댑터
            binding.recordLookPictureVp.adapter = RecordLookViewpagerAdapter(imageList)
            binding.recordLookPictureVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            binding.recordLookPictureVp.setPageTransformer(ZoomOutPageTransformer())
            binding.recordLookCircleIndicator.setViewPager2(binding.recordLookPictureVp)

            binding.recordLookNameTv.text = result.eachContentResult.recordingTitle
            setStarState(result.eachContentResult.recordingStar)
            binding.recordLookContentsTv.text = result.eachContentResult.recordingContent

            val singleResult = ArrayList<FolderRecordResultList>()
            singleResult.add(FolderRecordResultList(result.eachRandomResult.creatAt, result.eachRandomResult.randomResultContent, result.eachRandomResult.randomResultType))

            val recordLookRVAdapter = RecordLookRVAdapter(singleResult)
            binding.recordResultRecyclerview.adapter = recordLookRVAdapter
        } catch (e: NullPointerException) {
            val imageList = ArrayList<String>()
            val name = "https://firebasestorage.googleapis.com/v0/b/gabojago-54fc6.appspot.com/o/images%2Fimage_background.png?alt=media&token=8d2965c1-5ab3-4d7f-964b-3918a0d01829"
            imageList.add(name)
            binding.recordLookNameTv.text = "제목이 비어있어!"
            setStarState(2.5)
            binding.recordLookContentsTv.text = "내용이 비어있어!"
            val emptyResult = ArrayList<FolderRecordResultList>()
            emptyResult.add(FolderRecordResultList("", "", 0))

            val recordLookRVAdapter = RecordLookRVAdapter(emptyResult)
            binding.recordResultRecyclerview.adapter = recordLookRVAdapter
            binding.recordLookCircleIndicator.visibility = View.GONE

            binding.recordLookPictureVp.adapter = RecordLookViewpagerAdapter(imageList)
            binding.recordLookPictureVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            binding.recordLookPictureVp.setPageTransformer(ZoomOutPageTransformer())
            binding.recordLookCircleIndicator.setViewPager2(binding.recordLookPictureVp)


        }
    }

    override fun onSingleLookFailure(code: Int, message: String) {
        binding.lookRecordBlurView.visibility = View.GONE
        binding.lookRecordLoadingPb.visibility = View.GONE
        var imageList = ArrayList<String>()
        val name = "https://firebasestorage.googleapis.com/v0/b/gabojago-54fc6.appspot.com/o/images%2Fimage_background.png?alt=media&token=8d2965c1-5ab3-4d7f-964b-3918a0d01829"
        imageList.add(name)
        Log.e("개별조회",message)

        binding.recordLookNameTv.text = "제목이 비어있어!"
        setStarState(2.5)
        binding.recordLookContentsTv.visibility = View.GONE

        val emptyResult = ArrayList<FolderRecordResultList>()
        emptyResult.add(FolderRecordResultList("", "", 0))

        val recordLookRVAdapter = RecordLookRVAdapter(emptyResult)
        binding.recordResultRecyclerview.adapter = recordLookRVAdapter

        binding.recordLookCircleIndicator.visibility = View.GONE

        binding.recordLookPictureVp.adapter = RecordLookViewpagerAdapter(imageList)
        binding.recordLookPictureVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.recordLookPictureVp.setPageTransformer(ZoomOutPageTransformer())
        binding.recordLookCircleIndicator.setViewPager2(binding.recordLookPictureVp)

    }

}