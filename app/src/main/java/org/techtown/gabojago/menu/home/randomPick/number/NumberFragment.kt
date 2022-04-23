package org.techtown.gabojago.menu.home.randomPick.number

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.*
import androidx.fragment.app.Fragment
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.FragmentNumberBinding
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.main.MyToast
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.home.contents.HomeMenuFragment
import org.techtown.gabojago.menu.home.contents.RandomService
import org.techtown.gabojago.menu.home.RandomView
import org.techtown.gabojago.menu.record.recordRetrofit.RecordService
import org.techtown.gabojago.menu.record.recordRetrofit.RecordCountView
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.view.updateLayoutParams as updateLayoutParams1

class NumberFragment : Fragment(), RandomView, RecordCountView {
    lateinit var binding: FragmentNumberBinding
    private lateinit var callback: OnBackPressedCallback
    var startNum: Int = 0
    var endNum: Int = 0
    var num: Int = 0
    var isOverlap: Boolean = false
    private var resArray: Array<Int?> = (arrayOf(-1))

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
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentNumberBinding.inflate(layoutInflater)

        var getNumberOption = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == AppCompatActivity.RESULT_OK){
                startNum = result.data?.getIntExtra("start", 0)!!
                endNum = result.data?.getIntExtra("end", 0)!!
                num = result.data?.getIntExtra("num", 0)!!
                isOverlap = result.data?.getBooleanExtra("overlap", true)!!
                resArray = getNumbers()
                Log.d("GETNUMBEROPTION",
                    startNum.toString() + " " + endNum.toString() + " " + num.toString() + " " + isOverlap.toString())
                for(i: Int in 0 until num){
                    Log.d("GETRESARRAY", resArray[i].toString())
                }
            }
        }

        var ballGroundArr = arrayOf(
            binding.numberBall01,
            binding.numberBall02,
            binding.numberBall03,
            binding.numberBall04,
            binding.numberBall05,
            binding.numberBall06,
            binding.numberBall07,
            binding.numberBall08,
            binding.numberBall09,
            binding.numberBall10
        )
        var resTextArr = arrayOf(
            binding.numberResult01Tv,
            binding.numberResult02Tv,
            binding.numberResult03Tv,
            binding.numberResult04Tv,
            binding.numberResult05Tv,
            binding.numberResult06Tv,
            binding.numberResult07Tv,
            binding.numberResult08Tv,
            binding.numberResult09Tv,
            binding.numberResult10Tv
        )

        contentsSize(ballGroundArr)

        val animAlphaStart = AnimationUtils.loadAnimation(activity, R.anim.anim_alpha_start_longer)

        binding.numberBackBtn.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeMenuFragment())
                .commitAllowingStateLoss()
        }

        binding.numberOptionBtn.setOnClickListener {
            val intent = Intent(activity, NumberOptionActivity::class.java)
            intent.putExtra("start", startNum)
            intent.putExtra("end", endNum)
            intent.putExtra("num", num)
            intent.putExtra("overlap", isOverlap)
            getNumberOption.launch(intent)
            activity?.overridePendingTransition(R.anim.anim_up, R.anim.anim_none)
        }

        binding.numberGoBtn.setOnClickListener { //가보자고 버튼을 눌렀을 때
            if(resArray[0] == -1){  //옵션 설정 안했을 때 validation
                MyToast.createToast(
                    requireContext(), "옵션을 설정한 후 실행해 주세요", 90, true
                ).show()
            }
            else{
                Handler().postDelayed({
                    binding.numberContentsView.visibility = View.GONE //초기 화면 없애는 거
                    //
                    binding.numberMainIv.visibility = View.GONE
                    binding.numberOptionBtn.visibility = View.GONE
                    binding.numberGoBtn.visibility = View.GONE
                    //
                }, 30)
                Handler().postDelayed({
                    binding.numberAnimationView.visibility = View.VISIBLE //애니메이션 화면(공 떨어지고 통 돌아가는 화면) 띄우는 거
                    binding.numberAnimationView.startAnimation(animAlphaStart) //띄우는 애니메이션 실행하기
                }, 50)
                Handler().postDelayed({
                    showAnimation(ballGroundArr) //애니메이션 돌아가게 하는 함수
                }, 300)
                Handler().postDelayed({
                    resAnimation(resTextArr) //결과 화면 띄우는 함수
                }, (1000 + 2250 * resArray.size).toLong())
            }
        }

        binding.numberRetryBtn.setOnClickListener {
            for(i in 0..9){
                ballGroundArr[i].visibility = View.GONE
                resTextArr[i].visibility = View.GONE
            }
            binding.numberResultBallIv.visibility = View.GONE
            binding.numberResultView.visibility = View.GONE
            binding.numberRetryBtn.visibility = View.INVISIBLE
            binding.numberSaveBtn.visibility = View.INVISIBLE
            //
            binding.numberMainIv.visibility = View.VISIBLE
            binding.numberOptionBtn.visibility = View.VISIBLE
            binding.numberGoBtn.visibility = View.VISIBLE
            //
            binding.numberContentsView.visibility = View.VISIBLE
            binding.numberContentsView.startAnimation(animAlphaStart)
            binding.numberSaveBtn.setOnClickListener {
                saveNumbers()
            }
        }

        binding.numberSaveBtn.setOnClickListener {
            saveNumbers()
        }
        return binding.root
    }

    private fun contentsSize(ballGroundArr: Array<AppCompatImageView>){
        val display = requireActivity().windowManager.defaultDisplay // in case of Fragment
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y

        binding.numberMainIv.layoutParams.height = height*50/100
        binding.numberAnimationMainIv.layoutParams.height = height*45/100
        binding.numberAnimationBallIv.layoutParams.height = height*30/100
        binding.numberGroundIv.layoutParams.height = height*22/100
        binding.numberDropBallIv.layoutParams.height = height*38/1000
        for (i in 0..9){
            ballGroundArr[i].layoutParams.height = height*38/1000
        }
        binding.numberResultMainIv.layoutParams.height = height*60/100
    }

    //Get the RESULT NUMBER -> RESARRAY
    private fun getNumbers(): Array<Int?> {
        var resNumbers = arrayOfNulls<Int>(num)
        val random = Random()
        val bound = endNum - startNum + 1
        if(isOverlap){
            for(i: Int in 0 until num){
                val res = startNum + random.nextInt(bound)
                resNumbers[i] = res
            }
        }
        else{
            var i = 0
            while(i < num){
                val res = startNum + random.nextInt(bound)
                resNumbers[i] = res
                for(j: Int in 0 until i){
                    if(resNumbers[j] == resNumbers[i]){
                        i--
                    }
                }
                i++
            }
        }
        return resNumbers
    }

    private fun showAnimation(ballArr: Array<AppCompatImageView>){
        val dropBall = AnimationUtils.loadAnimation(activity, R.anim.anim_ball_drop)
        val resetBall = AnimationUtils.loadAnimation(activity, R.anim.anim_ball_reset)
        val animAlphaStart = AnimationUtils.loadAnimation(activity, R.anim.anim_alpha_start_longer)
        for(i in 0..resArray.size - 1){
            Handler().postDelayed({
                spinDrawBoxAnimation()
            }, 2250 * i.toLong())
            Handler().postDelayed({
                binding.numberDropBallIv.visibility = View.VISIBLE
                binding.numberDropBallIv.startAnimation(animAlphaStart)
            }, 650 + 2250 * i.toLong())
            Handler().postDelayed({
                binding.numberDropBallIv.startAnimation(dropBall) //400ms
            }, 1450 + 2250 * i.toLong())
            Handler().postDelayed({
                binding.numberDropBallIv.visibility = View.GONE
                binding.numberDropBallIv.startAnimation(resetBall)
                ballArr[i].visibility = View.VISIBLE
                ballArr[i].startAnimation(animAlphaStart) //500ms
            }, 1750 + 2250 * i.toLong())
        }
    }

    private fun spinDrawBoxAnimation(){
        var numberBallsArr = arrayOf(
            R.drawable.number_balls_01,
            R.drawable.number_balls_02,
            R.drawable.number_balls_03,
            R.drawable.number_balls_04,
            R.drawable.number_balls_05,
            R.drawable.number_balls_06,
            R.drawable.number_balls_07,
            R.drawable.number_balls_08,
            R.drawable.number_balls_09,
            R.drawable.number_balls_10,
            R.drawable.number_balls_11,
            R.drawable.number_balls_12,
            R.drawable.number_balls_13,
            R.drawable.number_balls_14,
            R.drawable.number_balls_15
        )
        for(i in 0..14){
            Handler().postDelayed({
                binding.numberAnimationBallIv.setImageResource(numberBallsArr[i])
            }, 150 * i.toLong())
        }
    }

    private fun resAnimation(resTextArr: Array<AppCompatTextView>){

        var numResBallArr = arrayOf(
            R.drawable.number_result_balls_01,
            R.drawable.number_result_balls_02,
            R.drawable.number_result_balls_03,
            R.drawable.number_result_balls_04,
            R.drawable.number_result_balls_05,
            R.drawable.number_result_balls_06,
            R.drawable.number_result_balls_07,
            R.drawable.number_result_balls_08,
            R.drawable.number_result_balls_09,
            R.drawable.number_result_balls_10
        )

        val animAlphaStart = AnimationUtils.loadAnimation(activity, R.anim.anim_alpha_start_longer)
        binding.numberAnimationView.visibility = View.GONE
        binding.numberResultView.visibility = View.VISIBLE
        binding.numberResultView.startAnimation(animAlphaStart)
        setTextView(resTextArr)
        binding.numberResultBallIv.setImageResource(numResBallArr[resArray.size - 1])
        for(i in 0..resArray.size - 1){
            Handler().postDelayed({
                resTextArr[i].text = resArray[i].toString()
                resTextArr[i].visibility = View.VISIBLE
                binding.numberResultBallIv.visibility = View.VISIBLE
                resTextArr[i].startAnimation(animAlphaStart) //200ms
                binding.numberResultBallIv.startAnimation(animAlphaStart) //200ms
            }, 700)
        }
        Handler().postDelayed({
            binding.numberRetryBtn.visibility = View.VISIBLE
            binding.numberSaveBtn.visibility = View.VISIBLE
        }, 1000)
    }

    private fun setTextView(resTextArr: Array<AppCompatTextView>){

        data class Coord( val ht: Float, val vt: Float)
        val resultBallPosArr = arrayOf(
            arrayOf(Coord(0.5f, 0.4f)), //1
            arrayOf(Coord(0.055f, 0.47f), Coord(0.94f, 0.47f)), //2
            arrayOf(Coord(0.03f, 0.5f), Coord(0.5f, 0.5f), Coord(0.97f, 0.5f)), //3
            arrayOf(Coord(0.06f, 0.14f), Coord(0.94f, 0.14f), Coord( 0.06f,0.86f), Coord(0.94f,0.86f)), //4
            arrayOf(Coord(0.5f,0.08f), Coord(0.035f,0.5f), Coord(0.5f,0.5f), Coord(0.965f,0.5f), Coord(0.5f,0.92f)), //5
            arrayOf(Coord(0.03f,0.15f), Coord(0.495f,0.15f), Coord(0.97f,0.15f), Coord(0.262f,0.5f), Coord(0.732f,0.5f), Coord(0.495f,0.84f)), //6
            arrayOf(Coord(0.266f,0.14f), Coord(0.734f,0.14f), Coord(0.035f,0.5f), Coord(0.5f,0.5f), Coord(0.965f,0.5f), Coord(0.266f,0.86f), Coord(0.734f,0.86f)), //7
            arrayOf(Coord(0.03f,0.15f), Coord(0.495f,0.15f), Coord(0.968f,0.15f), Coord(0.265f,0.5f), Coord(0.73f,0.5f), Coord(0.03f,0.84f), Coord(0.4955f,0.84f), Coord(0.97f,0.84f)), //8
            arrayOf(Coord(0.5f,0.08f), Coord(0.268f,0.29f), Coord(0.732f,0.29f), Coord(0.035f,0.5f), Coord(0.5f,0.5f), Coord(0.966f,0.5f), Coord(0.27f,0.71f), Coord(0.73f,0.71f), Coord(0.5f,0.92f)), //9
            arrayOf(Coord(0.265f,0.11f), Coord(0.735f,0.11f), Coord(0.03f,0.36f), Coord(0.5f,0.36f), Coord(0.97f,0.36f), Coord(0.26f,0.62f), Coord(0.73f,0.623f), Coord(0.025f,0.875f), Coord(0.495f,0.875f), Coord(0.97f,0.875f)), //10
        )

        for(i in 0..resArray.size-1) {
            if((resArray[i]!! / 10000) > 0){
                resTextArr[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.5f)
            }
            else{
                resTextArr[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            }
            resTextArr[i].updateLayoutParams1<ConstraintLayout.LayoutParams> {
                horizontalBias = resultBallPosArr[resArray.size-1][i].ht;
                verticalBias = resultBallPosArr[resArray.size-1][i].vt
            }
        }
    }

    private fun saveNumbers(){
        val recordService = RecordService()
        recordService.setRecordCountView(this@NumberFragment)

        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
        val stringDate = dateFormat.format(date)
        val userJwt = getJwt(requireContext(), "userJwt")
        recordService.recordCount(userJwt, stringDate)
    }

    private fun saveWithValidation(count: Int) {
        if(resArray.isEmpty()){
            MyToast.createToast(
                requireContext(), "다시 실행 후 저장해 주세요.", 90, true
            ).show()
        }
        else if(count >= 30){
            MyToast.createToast(
                requireContext(), "오늘은 더 이상 저장할 수 없어!", 90, true
            ).show()
        }
        else{
            val randomService = RandomService()
            randomService.setRandomView(this@NumberFragment)

            var numberResString = ""
            for(i in 0..resArray.size - 1){
                numberResString = when(i){
                    0 -> resArray[i].toString()
                    else -> numberResString + "," + resArray[i].toString()
                }
            }
            Log.d("NUMBERRESULT", numberResString)

            val userJwt = getJwt(requireContext(), "userJwt")
            randomService.storeResult(userJwt, numberResString, 4)
        }
    }

    override fun onPause() {
        super.onPause()
        val animationOpen = AnimationUtils.loadAnimation(activity, R.anim.anim_open_scale)
        binding.numberOptionBtn.visibility = View.INVISIBLE
        binding.numberGoBtn.visibility = View.INVISIBLE
        binding.numberContentsView.startAnimation(animationOpen)
    }

    override fun onResume() {
        super.onResume()
        val animationClose = AnimationUtils.loadAnimation(activity, R.anim.anim_close_scale)
        binding.numberOptionBtn.visibility = View.VISIBLE
        binding.numberGoBtn.visibility = View.VISIBLE
        binding.numberContentsView.startAnimation(animationClose)
    }

    override fun onRandomLoading() {
        binding.numberLoadingView.visibility = View.VISIBLE
        for(i in 0..5){
            Handler().postDelayed({
                binding.numberLoadingIv.setImageResource(R.drawable.loading_02)
            }, (500 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.numberLoadingIv.setImageResource(R.drawable.loading_03)
            }, (1000 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.numberLoadingIv.setImageResource(R.drawable.loading_01)
            }, (1500 + 1500 * i).toLong())
        }
    }

    override fun onRandomResultSuccess() {
        binding.numberLoadingView.visibility = View.GONE
        MyToast.createToast(
            requireContext(), "뽑기 결과가 저장됐어!", 90, false
        ).show()
        binding.numberSaveBtn.setOnClickListener {
            MyToast.createToast(
                requireContext(), "이미 결과가 저장되었습니다.", 90, true
            ).show()
        }
    }

    override fun onRandomResultFailure(code: Int, message: String) {
        binding.numberLoadingView.visibility = View.GONE
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
    }

    override fun onRecordCountLoading() {
        binding.numberLoadingView.visibility = View.VISIBLE
        for(i in 0..5){
            Handler().postDelayed({
                binding.numberLoadingIv.setImageResource(R.drawable.loading_02)
            }, (500 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.numberLoadingIv.setImageResource(R.drawable.loading_03)
            }, (1000 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.numberLoadingIv.setImageResource(R.drawable.loading_01)
            }, (1500 + 1500 * i).toLong())
        }
    }

    override fun onRecordCountSuccess(result: Int) {
        binding.numberLoadingView.visibility = View.GONE
        saveWithValidation(result)
    }

    override fun onRecordCountFailure(code: Int, message: String) {
        binding.numberLoadingView.visibility = View.GONE
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
    }
}