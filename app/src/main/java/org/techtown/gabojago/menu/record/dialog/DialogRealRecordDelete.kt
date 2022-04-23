package org.techtown.gabojago.menu.record.dialog


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupMenu
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


class DialogRealRecordDelete(private val recordIdx:Int) : DialogFragment() ,SinglerecordingDeleteView{

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
        binding.dialogFolderTitleTv.setText("기록을\n정말 삭제할거야?")

        val recordService = RecordService()
        recordService.setSinglerecordingDeleteView(this@DialogRealRecordDelete)

        binding.dialogYesBtn.setOnClickListener {
            val userJwt = getJwt(requireContext(), "userJwt")
            recordService.putSingleDelete(userJwt, recordIdx)
        }

        binding.dialogNoBtn.setOnClickListener {
            mDismissListener.onDismiss(this)
            dismiss()
        }
        return binding.root
    }


    override fun onSinglerecordingDeleteSuccess() {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, RecordFragment().apply {
                arguments = Bundle().apply {
                }
            })
            .addToBackStack(null)
            .commitAllowingStateLoss()
        mDismissListener.onDismiss(this)
        dismiss()
    }

    override fun onSinglerecordingDeleteFailure(code: Int, message: String) {
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
    }
}


