package org.techtown.gabojago.menu.home.randomPick.clock

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ActivitySelectBinding
import org.techtown.gabojago.main.MyToast

class ClockSelectActivity : AppCompatActivity() {

    lateinit var binding: ActivitySelectBinding
    var res: Int = 1
    var resCode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        initNumberPicker()
        setPosition()
        binding.selectNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            res = newVal
        }

        binding.selectCompBtn.setOnClickListener{
            var intent = Intent()
            intent.putExtra("clock", res)
            setResult(resCode, intent)
            finish()
            overridePendingTransition(R.anim.anim_down, R.anim.anim_none)
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

    private fun setPosition(){
        if(intent.hasExtra("position")){
            var position = intent.getIntExtra("position", -1)
            if(position == 0){
                resCode = 100
            }
            else if(position == 1){
                resCode = 101
            }
        }
        else{
            MyToast.createToast(
                this, "Error", 90, true
            ).show()
        }
    }
}