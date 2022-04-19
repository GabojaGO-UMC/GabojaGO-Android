package org.techtown.gabojago.menu.randomPick.wheel

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ActivityWheelOptionBinding
import org.techtown.gabojago.menu.randomPick.wheel.WheelOptionRVAdapter
import java.util.ArrayList

class WheelOptionActivity : AppCompatActivity() {

    lateinit var binding: ActivityWheelOptionBinding
    var optionList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWheelOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if(intent.hasExtra("wheel")){
            optionList = intent.getStringArrayListExtra("wheel")!!
        }
        else{
            Toast.makeText(
                this, "못받았는데요", Toast.LENGTH_SHORT
            ).show()
        }

        //Setting RecyclerView
        val rvAdapter = WheelOptionRVAdapter(optionList)
        binding.recordResultRecyclerview.adapter = rvAdapter
        binding.recordResultRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //For when the user set the item num less than two
        rvAdapter.setMyItemClickListener(object : WheelOptionRVAdapter.MyItemClickListener {
            override fun onItemClick(size: Int) {
                if(size < 7){
                    binding.wheelPlusBtn.visibility = View.VISIBLE
                }
                if(size < 2){
                    showToastMsg()
                }
            }
        })
        //Add the Item
        binding.wheelPlusBtn.setOnClickListener {
            rvAdapter.addItem()
            if(optionList.size > 5){
                binding.wheelPlusBtn.visibility = View.GONE
            }
        }
        //Finish
        binding.wheelCompBtn.setOnClickListener {
            var intent = Intent()
            intent.putExtra("wheel", optionList)
            setResult(RESULT_OK, intent)
            finish()
            overridePendingTransition(R.anim.anim_down, R.anim.anim_none)
        }
    }
    fun showToastMsg(){
        Toast.makeText(
            this, "2개이하로줄일수없다는멘트", Toast.LENGTH_SHORT
        ).show()
    }
}