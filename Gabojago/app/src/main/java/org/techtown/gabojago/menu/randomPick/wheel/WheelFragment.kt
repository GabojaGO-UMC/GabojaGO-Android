package org.techtown.gabojago.menu.randomPick.wheel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.FragmentWheelBinding
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.randomPick.home.HomeFragment
import org.techtown.gabojago.menu.randomPick.home.HomeMenuFragment
import org.techtown.gabojago.menu.randomPick.home.RandomService
import org.techtown.gabojago.menu.randomPick.home.RandomView
import org.techtown.gabojago.menu.record.recordRetrofit.RecordService
import org.techtown.gabojago.menu.record.recordRetrofit.RecordCountView
import java.util.*

class WheelFragment : Fragment(), RandomView, RecordCountView {
    lateinit var binding: FragmentWheelBinding
    var optionList = ArrayList<String>()
    var res: Int = -1
    private val wheelText = arrayOf(
        arrayOf(2, 8),
        arrayOf(1, 5, 9),
        arrayOf(0, 4, 6, 10),
        arrayOf(0, 3, 5, 7, 10),
        arrayOf(0, 2, 4, 6, 8, 10)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWheelBinding.inflate(layoutInflater)

        var wheelArr = arrayOf(
            binding.wheel02,
            binding.wheel03,
            binding.wheel04,
            binding.wheel05,
            binding.wheel06
        )

        var wheelOptionNameArr = arrayOf(
            binding.wheelOption01Tv,
            binding.wheelOption02Tv,
            binding.wheelOption03Tv,
            binding.wheelOption04Tv,
            binding.wheelOption05Tv,
            binding.wheelOption06Tv,
            binding.wheelOption07Tv,
            binding.wheelOption08Tv,
            binding.wheelOption09Tv,
            binding.wheelOption10Tv,
            binding.wheelOption11Tv,
        )

        var getWheelOptionArray = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                optionList = result.data?.getStringArrayListExtra("wheel")!!
                setWheel(wheelArr)
                setOptionName(wheelOptionNameArr)
            }
        }

        binding.wheelOptionBtn.setOnClickListener{
            val intent = Intent(activity, WheelOptionActivity::class.java)
            intent.putExtra("wheel", optionList)
            getWheelOptionArray.launch(intent)
            activity?.overridePendingTransition(R.anim.anim_up, R.anim.anim_none)
        }

        binding.wheelBackBtn.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeMenuFragment().apply {
                    arguments = Bundle().apply {
                    }
                })
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding.wheelGoBtn.setOnClickListener {
            val animationStart = AnimationUtils.loadAnimation(activity, R.anim.anim_wheel_scale)
            binding.wheelMainView.startAnimation(animationStart)
            binding.wheelOptionBtn.visibility = View.GONE
            binding.wheelGoBtn.visibility = View.GONE
            binding.wheelInfoTv.visibility = View.INVISIBLE
            binding.wheelInfoTitleTv.visibility = View.INVISIBLE
            res = moveWheel()
            Handler().postDelayed({
                binding.wheelResultTv.text = optionList[res]
                binding.wheelRetryBtn.visibility = View.VISIBLE
                binding.wheelSaveBtn.visibility = View.VISIBLE
                binding.wheelResultTv.visibility = View.VISIBLE
            }, 3000)
        }

        binding.wheelRetryBtn.setOnClickListener {
            binding.wheelSaveBtn.setOnClickListener {
                saveWheel()
            }
            binding.wheelResultTv.visibility = View.GONE
            binding.wheelRetryBtn.visibility = View.GONE
            binding.wheelSaveBtn.visibility = View.GONE
            res = moveWheel()
            Handler().postDelayed({
                binding.wheelResultTv.text = optionList[res]
                binding.wheelRetryBtn.visibility = View.VISIBLE
                binding.wheelSaveBtn.visibility = View.VISIBLE
                binding.wheelResultTv.visibility = View.VISIBLE
            }, 3000)
        }

        binding.wheelSaveBtn.setOnClickListener {
            saveWheel()
        }
        
        return binding.root
    }

    private fun saveWheel(){
        val recordService = RecordService()
        recordService.setRecordCountView(this@WheelFragment)

        val userJwt = getJwt(requireContext(), "userJwt")
        recordService.recordCount(userJwt)
    }

    private fun saveWithValidation(count: Int) {
        if (res == -1) {
            Toast.makeText(
                activity, "다시 실행 후 저장해 주세요.", Toast.LENGTH_SHORT
            ).show()
        }
        else if(count >= 30){
            Toast.makeText(
                activity, "오늘은 더 이상 저장할 수 없어!", Toast.LENGTH_SHORT
            ).show()
        }
        else{
            val randomService = RandomService()
            randomService.setRandomView(this@WheelFragment)

            val userJwt = getJwt(requireContext(), "userJwt")
            randomService.storeResult(userJwt, optionList[res], 1)
        }
    }

    override fun onStart() {
        super.onStart()
        optionList.add("옵션 1")
        optionList.add("옵션 2")
    }

    override fun onPause() {
        super.onPause()
        binding.wheelInfoTitleTv.visibility = View.GONE
        binding.wheelInfoTv.visibility = View.GONE
        val animationOpen = AnimationUtils.loadAnimation(activity, R.anim.anim_open_scale)
        binding.wheelMainView.startAnimation(animationOpen)
    }

    override fun onResume() {
        super.onResume()
        val animationClose = AnimationUtils.loadAnimation(activity, R.anim.anim_close_scale)
        binding.wheelMainView.startAnimation(animationClose)
        binding.wheelInfoTitleTv.visibility = View.VISIBLE
        binding.wheelInfoTv.visibility = View.VISIBLE
    }

    private fun setWheel(wheelArr: Array<AppCompatImageView>){
        for(i: Int in 0..4){
            wheelArr[i].visibility = View.GONE
        }
        wheelArr[optionList.size - 2].visibility = View.VISIBLE
    }

    private fun setOptionName(wheelOptionNameArr: Array<AppCompatTextView>){

        for(i: Int in 0..10){
            wheelOptionNameArr[i].visibility = View.GONE
        }

        var opListSizeInIndex = optionList.size - 2
        for(i: Int in 0..(wheelText[opListSizeInIndex].size - 1)){
            wheelOptionNameArr[wheelText[opListSizeInIndex][i]].visibility = View.VISIBLE
            wheelOptionNameArr[wheelText[opListSizeInIndex][i]].text = optionList[i]
        }
    }

    private fun moveWheel(): Int{
        val res: Int = getWheelResult()
        wheelAnimation(res)
        return res
    }

    private fun getWheelResult(): Int{
        val random = Random()
        return random.nextInt(optionList.size)
    }

    private fun wheelAnimation(res: Int){
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotateAnimation.duration = 100
            rotateAnimation.fillAfter = true
            binding.wheelSpinView.startAnimation(rotateAnimation)
        }, 100)
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotateAnimation.duration = 200
            rotateAnimation.fillAfter = true
            binding.wheelSpinView.startAnimation(rotateAnimation)
        }, 200)
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotateAnimation.duration = 300
            rotateAnimation.fillAfter = true
            binding.wheelSpinView.startAnimation(rotateAnimation)
        }, 400)
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotateAnimation.duration = 400
            rotateAnimation.fillAfter = true
            binding.wheelSpinView.startAnimation(rotateAnimation)
        }, 700)
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotateAnimation.duration = 500
            rotateAnimation.fillAfter = true
            binding.wheelSpinView.startAnimation(rotateAnimation)
        }, 1100)
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotateAnimation.duration = 600
            rotateAnimation.fillAfter = true
            binding.wheelSpinView.startAnimation(rotateAnimation)
        }, 1600)
        val resAngle: Float = (360/optionList.size) * (optionList.size - res - 0.5f)
        Handler().postDelayed({
            val rotateAnimation = RotateAnimation(0f, resAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotateAnimation.duration = 700
            rotateAnimation.fillAfter = true
            binding.wheelSpinView.startAnimation(rotateAnimation)
        }, 2200)

    }

    override fun onRandomLoading() {
        binding.wheelLoadingView.visibility = View.VISIBLE
        for(i in 0..5){
            Handler().postDelayed({
                binding.wheelLoadingIv.setImageResource(R.drawable.loading_02)
            }, (500 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.wheelLoadingIv.setImageResource(R.drawable.loading_03)
            }, (1000 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.wheelLoadingIv.setImageResource(R.drawable.loading_01)
            }, (1500 + 1500 * i).toLong())
        }
    }

    override fun onRandomResultSuccess() {
        binding.wheelLoadingView.visibility = View.GONE
        Toast.makeText(
            context, "뽑기 결과가 저장됐어!", Toast.LENGTH_SHORT
        ).show()
        binding.wheelSaveBtn.setOnClickListener {
            Toast.makeText(
                context, "이미 결과가 저장되었습니다.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRandomResultFailure(code: Int, message: String) {
        binding.wheelLoadingView.visibility = View.GONE
        Toast.makeText(
            activity, message, Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRecordCountLoading() {
        binding.wheelLoadingView.visibility = View.VISIBLE
        for(i in 0..5){
            Handler().postDelayed({
                binding.wheelLoadingIv.setImageResource(R.drawable.loading_02)
            }, (500 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.wheelLoadingIv.setImageResource(R.drawable.loading_03)
            }, (1000 + 1500 * i).toLong())
            Handler().postDelayed({
                binding.wheelLoadingIv.setImageResource(R.drawable.loading_01)
            }, (1500 + 1500 * i).toLong())
        }
    }

    override fun onRecordCountSuccess(result: Int) {
        binding.wheelLoadingView.visibility = View.GONE
        saveWithValidation(result)
    }

    override fun onRecordCountFailure(code: Int, message: String) {
        binding.wheelLoadingView.visibility = View.GONE
        Toast.makeText(
            activity, message, Toast.LENGTH_SHORT
        ).show()
    }
}