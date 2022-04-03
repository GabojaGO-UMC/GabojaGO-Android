package org.techtown.gabojago.menu.record.look

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.FragmentRecordLookBinding
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.record.RecordFragment
import org.techtown.gabojago.menu.record.recordRetrofit.*
import java.util.*

class RecordLookFragment(private val folderIdx:Int): Fragment() , FolderLookView {
    lateinit var binding: FragmentRecordLookBinding
    private val MIN_SCALE = 0.95f
    private val MIN_ALPHA = 0.5f
    var isOpened: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordLookBinding.inflate(inflater, container, false)

        val recordService = RecordService()
        recordService.setFolderLookView(this@RecordLookFragment)
        val userJwt = getJwt(requireContext(), "userJwt")

        recordService.getFolderLook(userJwt, folderIdx)

        //Set Viewpager and Indicator
        var imageArr = getImageList()
        binding.recordLookPictureVp.adapter = RecordLookViewpagerAdapter(imageArr)
        binding.recordLookPictureVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.recordLookPictureVp.setPageTransformer(ZoomOutPageTransformer())
        binding.recordLookCircleIndicator.setViewPager2(binding.recordLookPictureVp)



        //Open RecyclerView Event
        binding.recordLookView.setOnClickListener {
            randomIsOpened(isOpened)
        }

        //Go to the Previous page
        binding.recordLookBackBtn.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, RecordFragment().apply {
                    arguments = Bundle().apply {
                    }
                })
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
        return binding.root
    }

    //Animation on the ViewPager
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

    //Set the state of RecyclerView
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

    //Function to store the Image in the ViewPager
    private fun getImageList(): ArrayList<Int> {

        var imageList = ArrayList<Int>()

        imageList.add(R.drawable.lookbase1)
        imageList.add(R.drawable.lookbase2)
        imageList.add(R.drawable.lookbase3)
        imageList.add(R.drawable.lookbase4)
        imageList.add(R.drawable.lookbase5)
        imageList.add(R.drawable.lookbase6)

        return imageList
    }



    //Function to set the star rate
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

        for(i in 0..(star*2).toInt() - 1){
            starArr[i].visibility = View.VISIBLE
        }
    }

    override fun onFolderLookSuccess(result: FolderLookResult) {
        try {
            binding.recordLookNameTv.text = result.folderContentResult.recordingTitle
            setStarState(result.folderContentResult.recordingStar)
            binding.recordLookContentsTv.text = result.folderContentResult.recordingContent
            val recordLookRVAdapter = RecordLookRVAdapter(result.folderResultList)
            binding.recordResultRecyclerview.adapter = recordLookRVAdapter
        } catch (e: NullPointerException) {
            binding.recordLookNameTv.text = "제목이 비어있어!"
            setStarState(3.5)
            binding.recordLookContentsTv.text = "내용이 비어있어!"
            val emptyResult = ArrayList<FolderRecordResultList>()
            emptyResult.add(FolderRecordResultList("", "", 0))

            val recordLookRVAdapter = RecordLookRVAdapter(emptyResult)
            binding.recordResultRecyclerview.adapter = recordLookRVAdapter
        }
    }

    override fun onFolderLookFailure(code: Int, message: String) {
        Toast.makeText(
            activity, message, Toast.LENGTH_SHORT
        ).show()

        binding.recordLookNameTv.text = "제목이 비어있어!"
        setStarState(3.5)
        binding.recordLookContentsTv.text = "내용이 비어있어!"

        val emptyResult = ArrayList<FolderRecordResultList>()
        emptyResult.add(FolderRecordResultList("", "", 0))

        val recordLookRVAdapter = RecordLookRVAdapter(emptyResult)
        binding.recordResultRecyclerview.adapter = recordLookRVAdapter
    }

}