package org.techtown.gabojago.menu.record.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import org.techtown.gabojago.R

import org.techtown.gabojago.databinding.DialogFoldermodifyBinding
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.record.RecordFragment
import org.techtown.gabojago.menu.record.RecordResultRVAdapter
import org.techtown.gabojago.menu.record.recordRetrofit.FolderResultList
import org.techtown.gabojago.menu.record.recordRetrofit.FolderUpdateView
import org.techtown.gabojago.menu.record.recordRetrofit.InFolderListResult
import org.techtown.gabojago.menu.record.recordRetrofit.RecordService
import java.util.ArrayList



class DialogFolderModify(private val folder : FolderResultList) : DialogFragment() ,FolderUpdateView{
    val minus = ArrayList<InFolderListResult>()
    val plus = ArrayList<InFolderListResult>()

    val plusUpdate= mutableListOf<Int>()
    val minusUpdate= mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않음
        isCancelable = true
        minus.clear()
        for (i in 0 until folder.randomResultListResult.size) {
            minus.add(i,folder.randomResultListResult[i])
        }
    }
    private lateinit var binding: DialogFoldermodifyBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogFoldermodifyBinding.inflate(inflater, container, false)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val recordService = RecordService()
        recordService.setFolderUpdateView(this@DialogFolderModify)

        if(folder.folderTitle !=null) {
            binding.folderModifyTitleTv.text = folder.folderTitle
        }else{
            binding.folderModifyTitleTv.text = "제목을 입력해줘!"
        }
        val dialogModifyMinusRVAdpater = DialogModifyMinusRVAdapter(minus)
        binding.folderModifyRecyclerview.adapter = dialogModifyMinusRVAdpater

        val dialogModifyPlusRVAdpater = DialogModifyPlusRVAdapter(plus)
        binding.dialogModifyRecyclerview.adapter = dialogModifyPlusRVAdpater

        dialogModifyMinusRVAdpater.setMyItemClickListener(object :
            DialogModifyMinusRVAdapter.MyItemClickListener {
            override fun onItemClick(position:Int) {
                plus.add(position,minus[position])
                minus.removeAt(position)
                dialogModifyPlusRVAdpater.notifyDataSetChanged()
                dialogModifyMinusRVAdpater.notifyDataSetChanged()
            }
        })

        dialogModifyPlusRVAdpater.setMyItemClickListener(object :
            DialogModifyPlusRVAdapter.MyItemClickListener {
            override fun onItemClick(position:Int) {
                minus.add(plus[position])
                plus.removeAt(position)
                dialogModifyMinusRVAdpater.notifyDataSetChanged()
                dialogModifyPlusRVAdpater.notifyDataSetChanged()
            }
        })

        binding.dialogCompleteBtn.setOnClickListener{
            val userJwt = getJwt(requireContext(), "userJwt")

            for (i in 0 until (minus.size)) {
                if(minus[i]!=null) {
                    plusUpdate.add(minus[i].resultIdx)
                }
            }

            for (i in 0 until (plus.size)) {
                if(plus[i]!=null) {
                    minusUpdate.add(plus[i].resultIdx)
                }
            }
            recordService.putUpdateFolderIdx(userJwt,folder.folderIdx,plusUpdate,minusUpdate)
        }


        return binding.root
    }

    override fun onFolderUpdateSuccess() {

        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, RecordFragment())
            .commitAllowingStateLoss()
        Log.e("성공","성공")
        dismiss()
    }

    override fun onFolderUpdateFailure(code: Int, message: String) {
        Toast.makeText(
            activity, message, Toast.LENGTH_SHORT
        ).show()
        dismiss()
    }
}