package org.techtown.gabojago.menu.record.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import org.techtown.gabojago.databinding.DialogFolderdeleteBinding
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.menu.record.recordRetrofit.FolderResultList
import org.techtown.gabojago.menu.record.recordRetrofit.SingleResultListResult

class DialogFolderDelete(private val recordList: ArrayList<SingleResultListResult>,private val folderList:ArrayList<FolderResultList>) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }
    private lateinit var binding: DialogFolderdeleteBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFolderdeleteBinding.inflate(inflater, container, false)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val dialogDeleteRVAdapter = DialogDeleteRVAdapter(folderList)
        binding.dialogFolderdeleteRecyclerview.adapter = dialogDeleteRVAdapter

        val dialogSingleDeleteRVAdapter = DialogDeleteSingleRVAdapter(recordList)
        binding.dialogDeleteRecyclerview.adapter = dialogSingleDeleteRVAdapter

        binding.dialogDeleteBtn.setOnClickListener{
            DialogRealDelete(recordList,folderList).show((context as MainActivity).supportFragmentManager,"dialog")
            dismiss()
        }


        return binding.root
    }
}