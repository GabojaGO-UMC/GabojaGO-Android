package org.techtown.gabojago.menu.record

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.annotations.SerializedName
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.FragmentFolderrecordBinding
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.record.recordRetrofit.FolderRecordingRequest
import org.techtown.gabojago.menu.record.recordRetrofit.FolderRecordingView
import org.techtown.gabojago.menu.record.recordRetrofit.InFolderListResult
import org.techtown.gabojago.menu.record.recordRetrofit.RecordService

class FolderRecordFragment(private val folderIdx :Int, private val resultList:ArrayList<InFolderListResult>) : Fragment(), FolderRecordingView {
    lateinit var binding: FragmentFolderrecordBinding
    val url= mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFolderrecordBinding.inflate(inflater, container, false)

        val bind =0
        val dk = "왜 바로 반영 안되쥬..dho "

        val recordFolderResultRVAdapter = RecordFolderResultRVAdapter(true, resultList)
        binding.recordResultRecyclerview.adapter = recordFolderResultRVAdapter

        binding.folderRecordCompleteView.setOnClickListener {
            val recordService = RecordService()
            recordService.setFolderRecordingView(this@FolderRecordFragment)
            val userJwt = getJwt(requireContext(), "userJwt")
            recordService.putFolderRecord(userJwt, folderIdx, FolderRecordingRequest(
                binding.folderRecordStarscore.rating.toDouble(),
                binding.folderRecordWriteEt.text.toString(),
                binding.folderRecordTitleTv.text.toString(),
                url
            ))

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, RecordFragment().apply {
                    arguments = Bundle().apply {
                    }
                })
                .addToBackStack(null)
                .commitAllowingStateLoss()

        }
        binding.folderRecordCompleteTv.setOnClickListener {
            val recordService = RecordService()
            recordService.setFolderRecordingView(this@FolderRecordFragment)
            val userJwt = getJwt(requireContext(), "userJwt")
            recordService.putFolderRecord(userJwt, folderIdx, FolderRecordingRequest(
                binding.folderRecordStarscore.rating.toDouble(),
                binding.folderRecordWriteEt.text.toString(),
                binding.folderRecordTitleTv.text.toString(),url
            ))

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, RecordFragment().apply {
                    arguments = Bundle().apply {
                    }
                })
                .addToBackStack(null)
                .commitAllowingStateLoss()

        }
        clickevent()
        init()

        return binding.root
    }

    private fun clickevent(){
        binding.folderRecordBackarrow.setOnClickListener{
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

    override fun onFolderRecordingSuccess() {
        Log.e("폴더기록","성공")
    }

    override fun onFolderRecordingFailure(code: Int, message: String) {
        Toast.makeText(
            activity, message, Toast.LENGTH_SHORT
        ).show()
    }

}