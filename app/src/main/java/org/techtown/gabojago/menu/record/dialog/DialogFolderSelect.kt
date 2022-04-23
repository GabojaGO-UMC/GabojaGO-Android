package org.techtown.gabojago.menu.record.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.DialogFolderselectBinding
import org.techtown.gabojago.databinding.FragmentRecordBinding
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.main.MyToast
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.record.RecordFragment
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
    private lateinit var binding2: FragmentRecordBinding

    val folderMake= mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFolderselectBinding.inflate(inflater, container, false)
        binding2 = FragmentRecordBinding.inflate(inflater,container,false)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding2.recordBlurView.visibility = View.VISIBLE

        val recordService = RecordService()
        recordService.setRecordFolderMakeView(this@DialogFolderSelect)

        val dialogSelectRVAdapter = DialogSelectRVAdapter(recordList)
        binding.dialogSelectRecyclerview.adapter = dialogSelectRVAdapter

        binding.dialogNextBtn.setOnClickListener{
            val userJwt = getJwt(requireContext(), "userJwt")
            for (i in 0 until isSelectList.size) {
                if(isSelectList[i]!=null) {
                    if (isSelectList[i]) {
                        folderMake.add(recordList[i].randomResultListResult.randomResultIdx)
                    }
                }
            }
            if(folderMake.size==1){
                folderMake.removeAt(0)
                MyToast.createToast(
                    requireContext(), "폴더 내 항목은 2개 이상이어야 해!", 90, true
                ).show()
            }else{
                recordService.putFolderMakeIdx(userJwt,folderMake)
            }
        }

        binding.dialogSelectCancleIv.setOnClickListener{
            binding2.recordBlurView.visibility = View.GONE
            dismiss()
        }

        return binding.root
    }

    override fun onRecordFolderMakeSuccess() {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, RecordFragment())
            .commitAllowingStateLoss()
        binding2.recordBlurView.visibility = View.GONE
        dismiss()
        Log.e("폴더생성","성공")
    }

    override fun onRecordFolderMakeFailure(code: Int, message: String) {
        Log.e("폴더생성실패",message)
        binding2.recordBlurView.visibility = View.GONE
        dismiss()
    }
}