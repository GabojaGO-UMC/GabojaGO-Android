package org.techtown.gabojago.menu.home.contents

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.FragmentHomeBinding
import org.techtown.gabojago.main.MyToast
import org.techtown.gabojago.menu.home.info.HomeInfoFragment
import kotlin.system.exitProcess

class HomeFragment : Fragment(){
    lateinit var binding: FragmentHomeBinding
    private lateinit var loopAnim: LoopAnimation
    private lateinit var callback: OnBackPressedCallback
    private var backPressedTime : Long = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    ActivityCompat.finishAffinity(requireActivity())
                    exitProcess(0)
                }

                MyToast.createToast(
                    requireContext(), "뒤로가기 버튼을 한 번 더 누르면 앱이 종료됩니다.", 90, false
                ).show()
                backPressedTime = System.currentTimeMillis()
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        1
        binding.homeStartIv.layoutParams.height = height*3/8
        binding.homeStartIv.layoutParams.width = binding.homeStartIv.layoutParams.height*4/5

        binding.homeDice01Iv.layoutParams.height = height/5
        binding.homeDice01Iv.layoutParams.width = binding.homeDice01Iv.layoutParams.height*4/5

        binding.homeDice02Iv.layoutParams.height = height/9
        binding.homeDice02Iv.layoutParams.width = binding.homeDice02Iv.layoutParams.height*8/9

        //Set the text color
        var orangeText = SpannableStringBuilder("랜덤에 내 발길을 맡겨볼까?\n새로운 장소를 발견하고 탐험해봐!")
        orangeText.setSpan(ForegroundColorSpan(Color.rgb(255,103,69)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.homeInfoTv.text = orangeText

        binding.homeStartIv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeMenuFragment())
                .commitAllowingStateLoss()
        }

        binding.homeDice01Iv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeInfoFragment())
                .commitAllowingStateLoss()
        }

        rotateSet()
        loopAnim = LoopAnimation()
        loopAnim.start()

        return binding.root
    }

    private fun rotateSet(){
        val rotate = RotateAnimation(0f, 21.92f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.fillAfter = true
        binding.homeStartIv.startAnimation(rotate)

        val rotate02 = RotateAnimation(0f, -21.02f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate02.fillAfter = true
        binding.homeDice01Iv.startAnimation(rotate02)

        val rotate03 = RotateAnimation(0f, -45f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate03.fillAfter = true
        binding.homeDice02Iv.startAnimation(rotate03)
    }

    inner class LoopAnimation : Thread() {

        @SuppressLint("SetTextI18n")
        override fun run() {
            try {
                val startAnimation01 = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_dice_start_01)
                val endAnimation01 = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_dice_end_01)
                val startAnimation02 = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_dice_start_02)
                val endAnimation02 = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_dice_end_02)
                while (true) {
                    binding.homeStartIv.startAnimation(startAnimation01)
                    binding.homeDice01Iv.startAnimation(startAnimation02)
                    sleep(1700)
                    binding.homeStartIv.startAnimation(endAnimation01)
                    binding.homeDice01Iv.startAnimation(endAnimation02)
                    sleep(1700)
                }
            } catch (e: InterruptedException) {
                Log.d("SONG", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }
}