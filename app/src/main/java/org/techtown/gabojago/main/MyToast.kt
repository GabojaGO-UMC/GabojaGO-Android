@file:Suppress("DEPRECATION")
package org.techtown.gabojago.main

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ToastBoardWhiteBinding

object MyToast {

    fun createToast(context: Context, message: String, position: Int, isWarning: Boolean): Toast {

        val binding: ToastBoardWhiteBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.toast_board_white, null, false)

        binding.toastBoardTv.text = message

        if(isWarning){
            binding.toastBoardTv.setTextColor(Color.parseColor("#ff6745"))
        }

        return Toast(context).apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, position.toPx())
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}