package org.techtown.gabojago.menu.randomPick.clock

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ActivityClockOptionBinding

class ClockOptionActivity : AppCompatActivity() {

    lateinit var binding: ActivityClockOptionBinding
    var startNum: Int = 12
    var endNum: Int = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setStoredClock()

        binding.clockStartTv.setOnClickListener {
            startActivityForResult(Intent(this, ClockSelectActivity::class.java), 100)
        }
        binding.clockEndTv.setOnClickListener {
            startActivityForResult(Intent(this, ClockSelectActivity::class.java), 101)
        }
        binding.clockCompBtn.setOnClickListener {
            if(startNum == endNum){
                Toast.makeText(
                    this, "방향을 다시 설정해 주세요", Toast.LENGTH_SHORT
                ).show()
            }
            else{
                var intent = Intent()
                intent.putExtra("start", startNum)
                intent.putExtra("end", endNum)
                setResult(RESULT_OK, intent)
                finish()
                overridePendingTransition(R.anim.anim_down, R.anim.anim_none)
            }
        }
    }

    private fun setStoredClock(){
        if(intent.hasExtra("start")){
            startNum = intent.getIntExtra("start", 0)
            binding.clockStartTv.text = startNum.toString()
        }
        else{
            binding.clockStartTv.text = "12"
        }
        if(intent.hasExtra("end")){
            endNum = intent.getIntExtra("end", 0)
            binding.clockEndTv.text = endNum.toString()
        }
        else{
            binding.clockEndTv.text = "12"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100){
            startNum = data?.getIntExtra("clock", 12)!!
            binding.clockStartTv.text = startNum.toString()
        }
        else if(requestCode == 101){
            endNum = data?.getIntExtra("clock", 12)!!
            binding.clockEndTv.text = endNum.toString()
        }
    }
}