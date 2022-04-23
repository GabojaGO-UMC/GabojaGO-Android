package org.techtown.gabojago.menu.randomPick.color

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.FragmentColorBinding
import org.techtown.gabojago.menu.randomPick.home.HomeMenuFragment

class ColorFragment : Fragment() {
    lateinit var binding: FragmentColorBinding
    var isRunning: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentColorBinding.inflate(layoutInflater)

        binding.colorCard01View.setImageResource(R.drawable.vending_card_selected)
        binding.colorBackBtn.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeMenuFragment().apply {
                    arguments = Bundle().apply {
                    }
                })
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding.colorBtn.setOnClickListener {
            binding.colorBtn.isEnabled = false
            cardColorAnimation()
            binding.colorBtn.visibility = View.GONE
            binding.colorClickedBtn.visibility = View.VISIBLE
            Handler().postDelayed({
                binding.colorBtn.visibility = View.VISIBLE
                binding.colorClickedBtn.visibility = View.GONE
            }, 300)
        }

        return binding.root
    }
    private fun cardColorAnimation(){
        val animAlphaStart = AnimationUtils.loadAnimation(activity, R.anim.anim_alpha_start)
        Handler().postDelayed({
            binding.colorCard01View.setImageResource(R.drawable.vending_card)
            binding.colorCard09View.setImageResource(R.drawable.vending_card_selected)
            binding.colorCard09View.startAnimation(animAlphaStart)
        }, 400)
        Handler().postDelayed({
            binding.colorCard09View.setImageResource(R.drawable.vending_card)
            binding.colorCard07View.setImageResource(R.drawable.vending_card_selected)
            binding.colorCard07View.startAnimation(animAlphaStart)
        }, 800)
        Handler().postDelayed({
            binding.colorCard07View.setImageResource(R.drawable.vending_card)
            binding.colorCard02View.setImageResource(R.drawable.vending_card_selected)
            binding.colorCard02View.startAnimation(animAlphaStart)
        }, 1200)
        Handler().postDelayed({
            binding.colorCard02View.setImageResource(R.drawable.vending_card)
            binding.colorCard06View.setImageResource(R.drawable.vending_card_selected)
            binding.colorCard06View.startAnimation(animAlphaStart)
        }, 1600)
        Handler().postDelayed({
            binding.colorCard06View.setImageResource(R.drawable.vending_card)
            binding.colorCard05View.setImageResource(R.drawable.vending_card_selected)
            binding.colorCard05View.startAnimation(animAlphaStart)
        }, 2000)

        //Drop the card #5
        Handler().postDelayed({
            val anim_drop = AnimationUtils.loadAnimation(activity, R.anim.anim_card_drop)
            binding.colorCard05View.startAnimation(anim_drop)
        }, 2200) //delay + 200
        //For 2nd card #5
        Handler().postDelayed({
            binding.colorCard05View.visibility = View.GONE
        }, 2600) //delay + 400
        //Rotate 2nd card #5
        Handler().postDelayed({
            binding.colorCardDroppedView.visibility = View.VISIBLE
            val anim_drop_02 = AnimationUtils.loadAnimation(activity, R.anim.anim_card_drop_02)
            binding.colorCardDroppedView.startAnimation(anim_drop_02)
        }, 2800) //delay + 200
        Handler().postDelayed({
            val anim_drop_03 = AnimationUtils.loadAnimation(activity, R.anim.anim_card_drop_03)
            binding.colorCardDroppedView.startAnimation(anim_drop_03)
        }, 3400) //delay + 300(stop) + 300
        Handler().postDelayed({
            binding.colorCardDroppedView.visibility = View.GONE
            startActivity(Intent(activity, ColorResultActivity::class.java))
            activity?.overridePendingTransition(R.anim.anim_alpha_start, R.anim.anim_none)
        }, 3630) //delay + 330
        Handler().postDelayed({
            binding.colorBtn.isEnabled = true
        }, 3800) //delay + 70
    }

    override fun onResume() {
        super.onResume()
        binding.colorCard01View.setImageResource(R.drawable.vending_card_selected)
        binding.colorCard05View.visibility = View.VISIBLE
        binding.colorCard05View.setImageResource(R.drawable.vending_card)
    }
}