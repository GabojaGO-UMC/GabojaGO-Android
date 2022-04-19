package org.techtown.gabojago.menu.record.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.dialog_folderdelete.*
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.DialogRealdeleteBinding
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.randomPick.home.HomeFragment
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

    val folderDelete= mutableListOf<Int>()
    val resultDelete= mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogRealdeleteBinding.inflate(inflater, container, false)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val recordService = RecordService()
        recordService.setFolderDeleteView(this@DialogRealDelete)

        binding.dialogYesBtn.setOnClickListener {
            val userJwt = getJwt(requireContext(), "userJwt")

            for (i in 0 until (isFolderSelectList.size-1)) {
                if(isFolderSelectList[i]!=null) {
                    if (isFolderSelectList[i]) {
                        folderDelete.add(folderList[i].folderIdx)
                    }
                }
            }

            for (i in 0 until (isSingleSelectList.size-1)) {
                if(isSingleSelectList[i]!=null) {
                    if (isSingleSelectList[i]) {
                        resultDelete.add(recordList[i].randomResultListResult.randomResultIdx)
                    }
                }
            }
            Log.e("폴더삭제",resultDelete.toString())
            recordService.putIdx(userJwt,resultDelete,folderDelete)
        }

        binding.dialogNoBtn.setOnClickListener {
            dismiss()
        }
        return binding.root
    }


    override fun onFolderDeleteSuccess() {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, RecordFragment())
            .commitAllowingStateLoss()
        Log.e("성공","성공")
        dismiss()
    }

    override fun onFolderDeleteFailure(code: Int, message: String) {
        Toast.makeText(
            activity, message, Toast.LENGTH_SHORT
        ).show()
        dismiss()
    }
}


