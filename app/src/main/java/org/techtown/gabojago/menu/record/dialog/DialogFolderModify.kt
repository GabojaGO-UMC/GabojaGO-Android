package org.techtown.gabojago.menu.record.dialog

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.DialogFoldermodifyBinding
import org.techtown.gabojago.databinding.FragmentRecordBinding
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.main.MyToast
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.record.RecordFragment
import org.techtown.gabojago.menu.record.recordRetrofit.*


class DialogFolderModify(private val folder : FolderResultList,private val records:ArrayList<SingleResultListResult>) : DialogFragment() ,FolderUpdateView{

    val plusUpdate= mutableListOf<Int>()
    val minusUpdate= mutableListOf<Int>()

    interface onDismissListener {
        fun onDismiss(dialogFragment : DialogFragment)
    }

    private lateinit var mDismissListener: onDismissListener

    fun setOnDismissClickListener(dismissListener: onDismissListener) {
        mDismissListener = dismissListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않음
        isCancelable = true
        plusUpdate.clear()
        minusUpdate.clear()

    }
    private lateinit var binding: DialogFoldermodifyBinding
    private lateinit var binding2: FragmentRecordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogFoldermodifyBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.folderModifyLayout.setBackgroundResource(R.drawable.folderresultbox_selectorange)


        val recordService = RecordService()
        recordService.setFolderUpdateView(this@DialogFolderModify)

        if(folder.folderTitle !=null) {
            binding.folderModifyTitleTv.text = folder.folderTitle
        }else{
            binding.folderModifyTitleTv.text = "제목을 입력해줘!"
        }
        val dialogModifyFolderRVAdpater = DialogModifyFolderRVAdapter(folder.randomResultListResult)
        binding.folderModifyRecyclerview.adapter = dialogModifyFolderRVAdpater

        val dialogModifySingleRVAdpater = DialogModifySingleRVAdapter(records)
        binding.dialogModifyRecyclerview2.adapter = dialogModifySingleRVAdpater

        if(dialogModifyFolderRVAdpater.itemCount>0&& dialogModifySingleRVAdpater.itemCount>0){
            binding.dialogDivisionView.visibility = View.VISIBLE
        }

        binding.dialogCompleteBtn.setOnClickListener{
            val userJwt = getJwt(requireContext(), "userJwt")

            for (i in 0 until (minus.size)) {
                if(minus[i]!=null) {
                    if (minus[i]) {
                        if (!folder.randomResultListResult.isEmpty()) {
                            minusUpdate.add(folder.randomResultListResult[i].resultIdx)
                        }
                    }
                }
            }

            for (i in 0 until (plus.size)) {
                if(plus[i]!=null) {
                    if (plus[i]) {
                        if (!folder.randomResultListResult.isEmpty()&&records[i].randomResultListResult.randomResultIdx!=null) {
                            plusUpdate.add(records[i].randomResultListResult.randomResultIdx)
                        }
                    }
                }
            }
            Log.e("폴더수정",minusUpdate.toString())

            if ((folder.randomResultListResult.size - minusUpdate.size) + plusUpdate.size <= 1) {
                minusUpdate.clear()
                plusUpdate.clear()
                MyToast.createToast(
                    requireContext(), "폴더 내 항목은 2개 이상이어야 해!", 90, true
                ).show()
            }else {
                recordService.putUpdateFolderIdx(userJwt, folder.folderIdx, plusUpdate, minusUpdate)
            }
        }

        binding.dialogModifyCancleIv.setOnClickListener{
            Log.e("dialog","dismiss")
            mDismissListener.onDismiss(this)
            dismiss()
        }


        return binding.root
    }


    override fun onFolderUpdateSuccess() {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, RecordFragment())
            .commitAllowingStateLoss()
        Log.e("성공","성공")
        mDismissListener.onDismiss(this)
        dismiss()
    }

    override fun onFolderUpdateFailure(code: Int, message: String) {
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
        mDismissListener.onDismiss(this)
        dismiss()
    }
}