package org.techtown.gabojago.menu.randomPick.color

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ActivityColorResultBinding
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.randomPick.home.RandomService
import org.techtown.gabojago.menu.randomPick.home.RandomView
import org.techtown.gabojago.menu.record.recordRetrofit.RecordService
import org.techtown.gabojago.menu.record.recordRetrofit.RecordCountView
import java.util.*

class ColorResultActivity : AppCompatActivity(), RandomView, RecordCountView {

    lateinit var binding: ActivityColorResultBinding
    var randRes: Int = -1

    var randomColor = arrayOf(
        "빨간색 계열",
        "주황색 계열",
        "노란색 계열",
        "초록색 계열",
        "파란색 계열",
        "검은색 계열",
        "갈색 계열",
        "흰색 계열",
        "마젠타 계열",
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityColorResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val animAlphaStart = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_start)

        var randomColorImageArr = arrayOf(
            R.drawable.color_red,
            R.drawable.color_orange,
            R.drawable.color_yellow,
            R.drawable.color_green,
            R.drawable.color_blue,
            R.drawable.color_black,
            R.drawable.color_brown,
            R.drawable.color_white,
            R.drawable.color_magenta,
        )

        binding.colorResultQuestionBtn.setOnClickListener{
            binding.colorResultQuestionView.visibility = View.GONE
            binding.colorResultView.visibility = View.VISIBLE
            binding.colorResultView.startAnimation(animAlphaStart)

            randRes = setRandom()
            binding.colorResultColorTv.text = randomColor[randRes]
            binding.colorResultColorIv.setImageResource(randomColorImageArr[randRes])
        }

        binding.colorResultRetryBtn.setOnClickListener {
            finish()
        }

        binding.colorResultSaveBtn.setOnClickListener {
            val recordService = RecordService()
            recordService.setRecordCountView(this@ColorResultActivity)

            val userJwt = getJwt(this, "userJwt")
            recordService.recordCount(userJwt)
        }
    }

    private fun saveWithValidation(count: Int) {
        if (randRes == -1) {
            Toast.makeText(
                this, "다시 실행 후 저장해 주세요.", Toast.LENGTH_SHORT
            ).show()
        }
        else if(count >= 30){
            Toast.makeText(
                this, "오늘은 더 이상 저장할 수 없어!", Toast.LENGTH_SHORT
            ).show()
        }
        else {
            val randomService = RandomService()
            randomService.setRandomView(this)

            val userJwt = getJwt(this, "userJwt")
            randomService.storeResult(userJwt, randomColor[randRes], 3)
        }
    }

    private fun setRandom(): Int{
        val random = Random()
        return random.nextInt(9)
    }

    override fun onRandomLoading() {
        binding.colorLoadingView.visibility = View.VISIBLE
        for(i in 0..5){
            Handler().postDelayed({
                binding.colorLoadingIv.setImageResource(R.drawable.loading_02)
            }, (500 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.colorLoadingIv.setImageResource(R.drawable.loading_03)
            }, (1000 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.colorLoadingIv.setImageResource(R.drawable.loading_01)
            }, (1500 + 1500 * i).toLong())
        }
    }

    override fun onRandomResultSuccess() {
        binding.colorLoadingView.visibility = View.GONE
        Toast.makeText(
            this, "뽑기 결과가 저장됐어!", Toast.LENGTH_SHORT
        ).show()

        binding.colorResultSaveBtn.setOnClickListener {
            Toast.makeText(
                this, "이미 결과가 저장되었습니다.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRandomResultFailure(code: Int, message: String) {
        binding.colorLoadingView.visibility = View.GONE
        Toast.makeText(
            this, message, Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRecordCountLoading() {
        binding.colorLoadingView.visibility = View.VISIBLE
        for(i in 0..5){
            Handler().postDelayed({
                binding.colorLoadingIv.setImageResource(R.drawable.loading_02)
            }, (500 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.colorLoadingIv.setImageResource(R.drawable.loading_03)
            }, (1000 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.colorLoadingIv.setImageResource(R.drawable.loading_01)
            }, (1500 + 1500 * i).toLong())
        }
    }

    override fun onRecordCountSuccess(result: Int) {
        binding.colorLoadingView.visibility = View.GONE
        saveWithValidation(result)
    }

    override fun onRecordCountFailure(code: Int, message: String) {
        binding.colorLoadingView.visibility = View.GONE
        Toast.makeText(
            this, message, Toast.LENGTH_SHORT
        ).show()
    }
}