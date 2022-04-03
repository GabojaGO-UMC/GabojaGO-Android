package org.techtown.gabojago.menu.randomPick.number

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import org.intellij.lang.annotations.JdkConstants
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.FragmentNumberBinding
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.randomPick.home.HomeMenuFragment
import org.techtown.gabojago.menu.randomPick.home.RandomService
import org.techtown.gabojago.menu.randomPick.home.RandomView
import org.techtown.gabojago.menu.record.recordRetrofit.RecordService
import org.techtown.gabojago.menu.record.recordRetrofit.RecordCountView
import java.util.*
import androidx.core.view.updateLayoutParams as updateLayoutParams1

class NumberFragment : Fragment(), RandomView, RecordCountView {
    lateinit var binding: FragmentNumberBinding
    var startNum: Int = 0
    var endNum: Int = 0
    var num: Int = 0
    var isOverlap: Boolean = false
    private var resArray: Array<Int?> = (arrayOf(-1))

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
        val animAlphaStart = AnimationUtils.loadAnimation(activity, R.anim.anim_alpha_start_longer)

        binding.numberBackBtn.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeMenuFragment().apply {
                    arguments = Bundle().apply {
                    }
                })
                .addToBackStack(null)
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

        binding.numberGoBtn.setOnClickListener {
            if(resArray[0] == -1){
                Toast.makeText(
                    context, "옵션을 설정한 후 실행해 주세요", Toast.LENGTH_SHORT
                ).show()
            }
            else{
                Handler().postDelayed({
                    binding.numberContentsView.visibility = View.GONE
                }, 50)
                Handler().postDelayed({
                    binding.numberAnimationView.visibility = View.VISIBLE
                    binding.numberAnimationView.startAnimation(animAlphaStart)
                }, 50)
                Handler().postDelayed({
                    showAnimation(ballGroundArr)
                }, 300)
                Handler().postDelayed({
                    resAnimation(resTextArr)
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
            arrayOf(Coord(0.5f, 0.49f)), //1
            arrayOf(Coord(0.38f, 0.5f), Coord(0.62f, 0.5f)), //2
            arrayOf(Coord(0.26f, 0.5f), Coord(0.5f, 0.5f), Coord(0.74f, 0.5f)), //3
            arrayOf(Coord(0.38f, 0.385f), Coord(0.62f, 0.385f), Coord( 0.38f,0.615f), Coord(0.62f,0.615f)), //4
            arrayOf(Coord(0.262f,0.5f), Coord(0.5f,0.275f), Coord(0.5f,0.5f), Coord(0.5f,0.725f), Coord(0.739f,0.5f)), //5
            arrayOf(Coord(0.256f,0.4f), Coord(0.5f,0.4f), Coord(0.744f,0.4f), Coord(0.378f,0.5f), Coord(0.621f,0.5f), Coord(0.5f,0.595f)), //6
            arrayOf(Coord(0.382f,0.385f), Coord(0.62f,0.385f), Coord(0.262f,0.5f), Coord(0.5f,0.5f), Coord(0.741f,0.5f), Coord(0.382f,0.615f), Coord(0.62f,0.615f)), //7
            arrayOf(Coord(0.255f,0.4f), Coord(0.5f,0.4f), Coord(0.745f,0.4f), Coord(0.378f,0.5f), Coord(0.622f,0.5f), Coord(0.255f,0.6f), Coord(0.5f,0.6f), Coord(0.745f,0.6f)), //8
            arrayOf(Coord(0.5f,0.275f), Coord(0.382f,0.39f), Coord(0.62f,0.39f), Coord(0.262f,0.5f), Coord(0.5f,0.5f), Coord(0.739f,0.5f), Coord(0.382f,0.615f), Coord(0.62f,0.615f), Coord(0.5f,0.725f)), //9
            arrayOf(Coord(0.378f,0.35f), Coord(0.622f,0.35f), Coord(0.255f,0.45f), Coord(0.5f,0.45f), Coord(0.745f,0.45f), Coord(0.378f,0.545f), Coord(0.622f,0.548f), Coord(0.255f,0.645f), Coord(0.5f,0.645f), Coord(0.745f,0.645f)), //10
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

        val userJwt = getJwt(requireContext(), "userJwt")
        recordService.recordCount(userJwt)
    }

    private fun saveWithValidation(count: Int) {
        if(resArray.isEmpty()){
            Toast.makeText(
                context, "다시 실행 후 저장해 주세요.", Toast.LENGTH_SHORT
            ).show()
        }
        else if(count >= 30){
            Toast.makeText(
                activity, "오늘은 더 이상 저장할 수 없어!", Toast.LENGTH_SHORT
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
        binding.numberMainIv.startAnimation(animationOpen)
    }

    override fun onResume() {
        super.onResume()
        val animationClose = AnimationUtils.loadAnimation(activity, R.anim.anim_close_scale)
        binding.numberMainIv.startAnimation(animationClose)
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
        Toast.makeText(
            context, "뽑기 결과가 저장됐어!", Toast.LENGTH_SHORT
        ).show()
        binding.numberSaveBtn.setOnClickListener {
            Toast.makeText(
                context, "이미 결과가 저장되었습니다.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRandomResultFailure(code: Int, message: String) {
        binding.numberLoadingView.visibility = View.GONE
        Toast.makeText(
            activity, message, Toast.LENGTH_SHORT
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
        Toast.makeText(
            activity, message, Toast.LENGTH_SHORT
        ).show()
    }
}