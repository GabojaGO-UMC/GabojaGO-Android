package org.techtown.gabojago.menu.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.techtown.gabojago.databinding.FragmentSinglerecordBinding

import com.google.android.material.bottomnavigation.BottomNavigationView
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.R


class SingleRecordFragment(recordIdx:Int) : Fragment() {
    lateinit var binding: FragmentSinglerecordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSinglerecordBinding.inflate(inflater, container, false)

        val recordPictureRVAdapter = RecordPictureRVAdapter()
        binding.singleRecordPictureRecyclerview.adapter = recordPictureRVAdapter

        clickevent()
        init()

        return binding.root
    }

    private fun clickevent(){
        binding.singleRecordBackarrow.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, RecordFragment().apply {
                    arguments = Bundle().apply {
                    }
                })
                .addToBackStack(null)
                .commitAllowingStateLoss()
            }
    }

    private fun init() {
        hideBottomNavigation(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(false)
    }

    fun hideBottomNavigation(bool: Boolean) {
        val bottomNavigation: BottomNavigationView = requireActivity().findViewById(R.id.main_bnv)
        if (bool == true)
            bottomNavigation.visibility = View.GONE
        else
            bottomNavigation.visibility = View.VISIBLE
    }

}