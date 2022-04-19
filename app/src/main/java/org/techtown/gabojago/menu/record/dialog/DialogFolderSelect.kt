package org.techtown.gabojago.menu.record.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.DialogFolderselectBinding
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.record.RecordFragment
import org.techtown.gabojago.menu.record.recordRetrofit.RecordCountView
import org.techtown.gabojago.menu.record.recordRetrofit.RecordFolderMakeView
import org.techtown.gabojago.menu.record.recordRetrofit.RecordService
import org.techtown.gabojago.menu.record.recordRetrofit.SingleResultListResult

class DialogFolderSelect(private val recordList: ArrayList<SingleResultListResult>) : DialogFragment(),
    RecordFolderMakeView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }
    private lateinit var binding: DialogFolderselectBinding
    val folderMake= mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFolderselectBinding.inflate(inflater, container, false)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val recordService = RecordService()
        recordService.setRecordFolderMakeView(this@DialogFolderSelect)

        val dialogSelectRVAdapter = DialogSelectRVAdapter(recordList)
        binding.dialogSelectRecyclerview.adapter = dialogSelectRVAdapter

        binding.dialogNextBtn.setOnClickListener{
            val userJwt = getJwt(requireContext(), "userJwt")
            for (i in 0 until isSelectList.size-1) {
                if(isSelectList[i]!=null) {
                    if (isSelectList[i]) {
                        folderMake.add(recordList[i].randomResultListResult.randomResultIdx)
                    }
                }
            }
            Log.e("선택",folderMake.toString())
            recordService.putFolderMakeIdx(userJwt,folderMake)


        }


        return binding.root
    }

    override fun onRecordFolderMakeSuccess() {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, RecordFragment())
            .commitAllowingStateLoss()

        dismiss()
        Log.e("폴더생성","성공")
    }

    override fun onRecordFolderMakeFailure(code: Int, message: String) {
        Toast.makeText(
            activity, message, Toast.LENGTH_SHORT
        ).show()
        dismiss()
    }
}