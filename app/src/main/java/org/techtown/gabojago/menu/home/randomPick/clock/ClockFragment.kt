package org.techtown.gabojago.menu.home.randomPick.clock

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.FragmentClockBinding
import org.techtown.gabojago.main.MyToast
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.home.contents.HomeMenuFragment
import org.techtown.gabojago.menu.home.contents.RandomService
import org.techtown.gabojago.menu.home.RandomView
import org.techtown.gabojago.menu.record.recordRetrofit.RecordService
import org.techtown.gabojago.menu.record.recordRetrofit.RecordCountView
import java.text.SimpleDateFormat
import java.util.*

class ClockFragment : Fragment(), RandomView, RecordCountView {

    lateinit var binding: FragmentClockBinding
    private lateinit var callback: OnBackPressedCallback
    var startNum: Int = 12
    var endNum: Int = 12
    var getResClock: Int = 12
    private var clockAngle = arrayOf(
        30f,
        60f,
        90f,
        120f,
        150f,
        180f,
        210f,
        240f,
        270f,
        300f,
        330f,
        360f
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.e("back","backpress")
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeMenuFragment())
                    .commitAllowingStateLoss()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentClockBinding.inflate(layoutInflater)

        var viewArr = arrayOf(
            binding.clock1,
            binding.clock2,
            binding.clock3,
            binding.clock4,
            binding.clock5,
            binding.clock6,
            binding.clock7,
            binding.clock8,
            binding.clock9,
            binding.clock10,
            binding.clock11,
            binding.clock12
        )

        var getClockOption = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                startNum = result.data?.getIntExtra("start", 12)!!
                endNum = result.data?.getIntExtra("end", 12)!!
                if (startNum > endNum) {
                    clockRangeSetter(viewArr, startNum - 1, endNum + 12 - 1)
                } else {
                    clockRangeSetter(viewArr, startNum - 1, endNum - 1)
                }
            }
        }

        contentsSize()

        binding.clockBackBtn.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeMenuFragment())
                .commitAllowingStateLoss()
        }
        binding.clockOptionBtn.setOnClickListener {
            val intent = Intent(activity, ClockOptionActivity::class.java)
            intent.putExtra("start", startNum)
            intent.putExtra("end", endNum)
            getClockOption.launch(intent)
            activity?.overridePendingTransition(R.anim.anim_up, R.anim.anim_none)
        }
        binding.clockGoBtn.setOnClickListener {
            if(startNum == endNum){
                MyToast.createToast(
                    requireContext(), "옵션을 설정한 후에 실행해 주세요", 90, true
                ).show()
            }
            else{
                moveClock()
                binding.clockOptionBtn.visibility = View.GONE
                binding.clockGoBtn.visibility = View.GONE
                binding.clockInfoTitleTv.visibility = View.INVISIBLE
                binding.clockInfoTv.visibility = View.INVISIBLE
                Handler().postDelayed({
                    binding.clockResultTv.text = getResClock.toString() + "시 방향"
                    binding.clockRetryBtn.visibility = View.VISIBLE
                    binding.clockSaveBtn.visibility = View.VISIBLE
                    binding.clockResultTv.visibility = View.VISIBLE
                }, 4700)
            }
        }
        binding.clockRetryBtn.setOnClickListener {
            binding.clockSaveBtn.setOnClickListener {
                saveClock()
            }
            val rotateAnimationResult = RotateAnimation(0F,
                0F,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f)
            rotateAnimationResult.fillAfter = true
            binding.clockArrowIv.startAnimation(rotateAnimationResult)
            binding.clockResultTv.visibility = View.GONE
            binding.clockRetryBtn.visibility = View.GONE
            binding.clockSaveBtn.visibility = View.GONE
            Handler().postDelayed({
                moveClock()
            }, 200)
            Handler().postDelayed({
                binding.clockResultTv.text = getResClock.toString() + "시 방향"
                binding.clockRetryBtn.visibility = View.VISIBLE
                binding.clockSaveBtn.visibility = View.VISIBLE
                binding.clockResultTv.visibility = View.VISIBLE
            }, 4900)
        }
        binding.clockSaveBtn.setOnClickListener {
            saveClock()
        }
        return binding.root
    }

    private fun contentsSize(){
        val display = requireActivity().windowManager.defaultDisplay // in case of Fragment
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y

        Log.d("COLORVENDINGSIZE", width.toString()+", "+height.toString())
        Log.d("COLORCARD", (width*0.1).toString()+", "+(height*0.1).toString())
        binding.clockIv.layoutParams.width = (width*0.8).toInt()
        binding.clockIv.layoutParams.height = (width*0.8).toInt()
    }

    private fun saveClock(){
        val recordService = RecordService()
        recordService.setRecordCountView(this@ClockFragment)

        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
        val stringDate = dateFormat.format(date)
        val userJwt = getJwt(requireContext(), "userJwt")
        recordService.recordCount(userJwt, stringDate)
    }

    private fun saveWithValidation(count: Int) {
        if (getResClock == -1) {
            MyToast.createToast(
                requireContext(), "다시 실행 후 저장해 주세요.", 90, true
            ).show()
        }
        else if(count >= 30){
            MyToast.createToast(
                requireContext(), "오늘은 더 이상 저장할 수 없어!", 90, true
            ).show()
        }
        else {
            val randomService = RandomService()
            randomService.setRandomView(this@ClockFragment)

            val userJwt = getJwt(requireContext(), "userJwt")
            randomService.storeResult(userJwt, getResClock.toString() + "시 방향", 2)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.clockInfoTitleTv.visibility = View.GONE
        binding.clockInfoTv.visibility = View.GONE
        binding.clockOptionBtn.visibility = View.INVISIBLE
        binding.clockGoBtn.visibility = View.INVISIBLE
        val animationOpen = AnimationUtils.loadAnimation(activity, R.anim.anim_open_scale)
        binding.clockContentsView.startAnimation(animationOpen)
    }

    override fun onResume() {
        super.onResume()
        val animationClose = AnimationUtils.loadAnimation(activity, R.anim.anim_close_scale)
        binding.clockContentsView.startAnimation(animationClose)
        binding.clockInfoTitleTv.visibility = View.VISIBLE
        binding.clockInfoTv.visibility = View.VISIBLE
        binding.clockOptionBtn.visibility = View.VISIBLE
        binding.clockGoBtn.visibility = View.VISIBLE
    }

    //startNum, endNum의 범위는 1~12이다.
    //index의 범위는 0~11이다
    //getResClock으로 넘겨주는 값은 0~11이고, getResClock으로 리턴받는 값은 1~12이다.
    //clockRangeSetter로 보내는 값의 범위는 0~11이다
    private fun moveClock() {
        var startAngle: Float
        var endAngle: Float
        var resAngle: Float
        if (startNum > endNum) {
            getResClock = getClockResult(startNum - 1, endNum + 12 - 1)
            startAngle = -360f + clockAngle[startNum - 1]
            endAngle = clockAngle[endNum - 1]
            if (getResClock <= 12) {
                resAngle = -360f + clockAngle[getResClock - 1]
            } else {
                getResClock %= 12
                resAngle = clockAngle[getResClock - 1]
            }
            //    clockRangeSetter(viewArr, startNum - 1, endNum + 12 - 1)
        } else {
            getResClock = getClockResult(startNum - 1, endNum - 1)
            startAngle = clockAngle[startNum - 1]
            endAngle = clockAngle[endNum - 1]
            resAngle = clockAngle[getResClock - 1]
            //     clockRangeSetter(viewArr, startNum - 1, endNum - 1)
        }
        clockAnimation(startAngle, endAngle, resAngle)
    }

    private fun getClockResult(start: Int, end: Int): Int {
        val random = Random()
        val bound = end - start + 1
        val res = start + random.nextInt(bound)
        return res + 1
    }

    private fun clockAnimation(startAngle: Float, endAngle: Float, resAngle: Float) {
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(0f,
                startAngle,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f)
            rotateAnimation.duration = 100
            rotateAnimation.fillAfter = true
            //   rotateAnimation.interpolator =
            binding.clockArrowIv.startAnimation(rotateAnimation)
        }, 100)
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(startAngle,
                endAngle,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f)
            rotateAnimation.duration = 300
            rotateAnimation.fillAfter = true
            //   rotateAnimation.interpolator =
            binding.clockArrowIv.startAnimation(rotateAnimation)
        }, 400)
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(endAngle,
                startAngle,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f)
            rotateAnimation.duration = 300
            rotateAnimation.fillAfter = true
            //   rotateAnimation.interpolator =
            binding.clockArrowIv.startAnimation(rotateAnimation)
        }, 700)
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(startAngle,
                endAngle,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f)
            rotateAnimation.duration = 500
            rotateAnimation.fillAfter = true
            //   rotateAnimation.interpolator =
            binding.clockArrowIv.startAnimation(rotateAnimation)
        }, 1000)
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(endAngle,
                startAngle,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f)
            rotateAnimation.duration = 500
            rotateAnimation.fillAfter = true
            //   rotateAnimation.interpolator =
            binding.clockArrowIv.startAnimation(rotateAnimation)
        }, 1500)
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(startAngle,
                endAngle,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f)
            rotateAnimation.duration = 800
            rotateAnimation.fillAfter = true
            //   rotateAnimation.interpolator =
            binding.clockArrowIv.startAnimation(rotateAnimation)
        }, 2000)
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(endAngle,
                startAngle,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f)
            rotateAnimation.duration = 800
            rotateAnimation.fillAfter = true
            //   rotateAnimation.interpolator =
            binding.clockArrowIv.startAnimation(rotateAnimation)
        }, 2800)
        Handler().postDelayed({
            val rotateAnimationResult = RotateAnimation(startAngle,
                resAngle,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f)
            rotateAnimationResult.fillAfter = true
            rotateAnimationResult.duration = 900
            //   rotateAnimation.interpolator =
            binding.clockArrowIv.startAnimation(rotateAnimationResult)
        }, 3600)
    }

    private fun clockRangeSetter(viewArr: Array<TextView>, start: Int, end: Int) {
        viewArr[start].setTextColor(Color.rgb(255, 103, 69))
        viewArr[end % 12].setTextColor(Color.rgb(255, 103, 69))
        for (i: Int in start..end) {
            viewArr[i % 12].setTextColor(Color.rgb(255, 147, 124))
        }
    }

    override fun onRandomLoading() {
        binding.clockLoadingView.visibility = View.VISIBLE
        for(i in 0..5){
            Handler().postDelayed({
                binding.clockLoadingIv.setImageResource(R.drawable.loading_02)
            }, (500 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.clockLoadingIv.setImageResource(R.drawable.loading_03)
            }, (1000 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.clockLoadingIv.setImageResource(R.drawable.loading_01)
            }, (1500 + 1500 * i).toLong())
        }
    }

    override fun onRandomResultSuccess() {
        binding.clockLoadingView.visibility = View.GONE
        MyToast.createToast(
            requireContext(), "뽑기 결과가 저장됐어!", 90, false
        ).show()
        binding.clockSaveBtn.setOnClickListener {
            MyToast.createToast(
                requireContext(), "이미 결과가 저장되었습니다.", 90, true
            ).show()
        }
    }

    override fun onRandomResultFailure(code: Int, message: String) {
        binding.clockLoadingView.visibility = View.GONE
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
    }

    override fun onRecordCountLoading() {
        binding.clockLoadingView.visibility = View.VISIBLE
        for(i in 0..5){
            Handler().postDelayed({
                binding.clockLoadingIv.setImageResource(R.drawable.loading_02)
            }, (500 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.clockLoadingIv.setImageResource(R.drawable.loading_03)
            }, (1000 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.clockLoadingIv.setImageResource(R.drawable.loading_01)
            }, (1500 + 1500 * i).toLong())
        }
    }

    override fun onRecordCountSuccess(result: Int) {
        binding.clockLoadingView.visibility = View.GONE
        saveWithValidation(result)
    }

    override fun onRecordCountFailure(code: Int, message: String) {
        binding.clockLoadingView.visibility = View.GONE
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
    }
}