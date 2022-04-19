package org.techtown.gabojago.menu.randomPick.clock

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import org.techtown.gabojago.databinding.ActivitySelectBinding

class ClockSelectActivity : AppCompatActivity() {

    lateinit var binding: ActivitySelectBinding
    var res: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        initNumberPicker()
        binding.selectNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            res = newVal
        }

        binding.selectCompBtn.setOnClickListener{
            val intent = Intent()
            intent.putExtra("clock", res)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun initNumberPicker(){
        val data1: Array<String> = Array(12){
            i -> (i+1).toString()
        }
        binding.selectNumberPicker.minValue = 1
        binding.selectNumberPicker.maxValue = 12
        binding.selectNumberPicker.wrapSelectorWheel = false
        binding.selectNumberPicker.displayedValues = data1
        binding.selectNumberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }
}