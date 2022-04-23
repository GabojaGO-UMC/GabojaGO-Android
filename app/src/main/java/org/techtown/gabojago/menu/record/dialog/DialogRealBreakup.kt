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
import org.techtown.gabojago.menu.record.recordRetrofit.*


class DialogRealBreakup(private val folderIdx: Int) : DialogFragment() ,FolderBreakView{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }
    interface onDismissListener {
        fun onDismiss(dialogFragment : DialogFragment)
    }

    private lateinit var mDismissListener: onDismissListener

    fun setOnDismissClickListener(dismissListener: onDismissListener) {
        mDismissListener = dismissListener
    }

    private lateinit var binding: DialogRealdeleteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogRealdeleteBinding.inflate(inflater, container, false)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.dialogFolderTitleTv.setText("선택한 항목,\n정말 해체할거야?")

        val recordService = RecordService()
        recordService.setFolderBreakView(this@DialogRealBreakup)

        binding.dialogYesBtn.setOnClickListener {
            val userJwt = getJwt(requireContext(), "userJwt")
            recordService.putBreakFolderIdx(userJwt,folderIdx)
        }

        binding.dialogNoBtn.setOnClickListener {
            mDismissListener.onDismiss(this)
            dismiss()
        }
        return binding.root
    }

    override fun onFolderBreakSuccess() {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, RecordFragment())
            .commitAllowingStateLoss()
        mDismissListener.onDismiss(this)
        dismiss()
    }

    override fun onFolderBreakFailure(code: Int, message: String) {
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
        mDismissListener.onDismiss(this)
        dismiss()
    }
}


