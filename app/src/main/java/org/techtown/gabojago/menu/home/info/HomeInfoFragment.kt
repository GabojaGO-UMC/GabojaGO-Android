package org.techtown.gabojago.menu.home.info

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.FragmentHomeInfoBinding
import org.techtown.gabojago.menu.home.contents.HomeFragment
import org.techtown.gabojago.menu.home.contents.HomeMenuFragment

class HomeInfoFragment : Fragment() {
    lateinit var binding: FragmentHomeInfoBinding
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
        binding = FragmentHomeInfoBinding.inflate(inflater, container, false)

        val display = requireActivity().windowManager.defaultDisplay // in case of Fragment
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y

        binding.homeInfoWheelView.layoutParams.height = height*12/100
        binding.homeInfoClockView.layoutParams.height = height*12/100
        binding.homeInfoColorView.layoutParams.height = height*12/100
        binding.homeInfoNumberView.layoutParams.height = height*12/100

        binding.homeInfoBackBtn.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        binding.homeInfoWheelView.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, InfoWheelFragment())
                .commitAllowingStateLoss()
        }
        binding.homeInfoClockView.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, InfoClockFragment())
                .commitAllowingStateLoss()
        }
        binding.homeInfoColorView.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, InfoColorFragment())
                .commitAllowingStateLoss()
        }
        binding.homeInfoNumberView.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, InfoNumberFragment())
                .commitAllowingStateLoss()
        }

        return binding.root
    }
}