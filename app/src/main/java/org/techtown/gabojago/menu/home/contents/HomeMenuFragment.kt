package org.techtown.gabojago.menu.home.contents

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.FragmentHomeMenuBinding
import org.techtown.gabojago.menu.home.randomPick.clock.ClockFragment
import org.techtown.gabojago.menu.home.randomPick.color.ColorFragment
import org.techtown.gabojago.menu.home.randomPick.number.NumberFragment
import org.techtown.gabojago.menu.home.randomPick.wheel.WheelFragment

class HomeMenuFragment : Fragment() {
    lateinit var binding: FragmentHomeMenuBinding
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.e("back","backpress")
                (context as MainActivity).supportFragmentManager.beginTransaction()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeMenuBinding.inflate(inflater, container, false)


        binding.homeMenuBackBtn.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        binding.homeMenuWheelView.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, WheelFragment())
                .commitAllowingStateLoss()
        }
        binding.homeMenuClockView.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, ClockFragment())
                .commitAllowingStateLoss()
        }
        binding.homeMenuColorView.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, ColorFragment())
                .commitAllowingStateLoss()
        }
        binding.homeMenuNumberView.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, NumberFragment())
                .commitAllowingStateLoss()
        }

        return binding.root
    }
}