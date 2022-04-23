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
import org.techtown.gabojago.databinding.DialogRealdeleteBinding
import org.techtown.gabojago.databinding.FragmentRecordBinding
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.main.MyToast
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.record.RecordFragment
import org.techtown.gabojago.menu.record.recordRetrofit.FolderDeleteView
import org.techtown.gabojago.menu.record.recordRetrofit.FolderResultList
import org.techtown.gabojago.menu.record.recordRetrofit.RecordService
import org.techtown.gabojago.menu.record.recordRetrofit.SingleResultListResult


class DialogRealDelete(private val recordList: ArrayList<SingleResultListResult>, private val folderList:ArrayList<FolderResultList>) : DialogFragment() ,FolderDeleteView{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    private lateinit var binding: DialogRealdeleteBinding
    private lateinit var binding2: FragmentRecordBinding

    val folderDelete= mutableListOf<Int>()
    val resultDelete= mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogRealdeleteBinding.inflate(inflater, container, false)
        binding2 = FragmentRecordBinding.inflate(inflater,container,false)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val recordService = RecordService()
        recordService.setFolderDeleteView(this@DialogRealDelete)

        binding.dialogYesBtn.setOnClickListener {
            val userJwt = getJwt(requireContext(), "userJwt")

            for (i in 0 until (isFolderSelectList.size)) {
                if(isFolderSelectList[i]!=null) {
                    if (isFolderSelectList[i]) {
                        if (!folderList.isEmpty()) {
                            folderDelete.add(folderList[i].folderIdx)
                        }
                    }
                }
            }

            for (i in 0 until (isSingleSelectList.size)) {
                if(isSingleSelectList[i]!=null) {
                    if (isSingleSelectList[i]) {
                        if(!recordList.isEmpty()) {
                            resultDelete.add(recordList[i].randomResultListResult.randomResultIdx)
                        }
                    }
                }
            }
            recordService.putIdx(userJwt,resultDelete,folderDelete)
        }

        binding.dialogNoBtn.setOnClickListener {
            binding2.recordBlurView.visibility = View.GONE
            dismiss()
        }
        return binding.root
    }


    override fun onFolderDeleteSuccess() {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, RecordFragment())
            .commitAllowingStateLoss()
        binding2.recordBlurView.visibility = View.GONE
        dismiss()
    }

    override fun onFolderDeleteFailure(code: Int, message: String) {
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
        binding2.recordBlurView.visibility = View.GONE
        dismiss()
    }
}


