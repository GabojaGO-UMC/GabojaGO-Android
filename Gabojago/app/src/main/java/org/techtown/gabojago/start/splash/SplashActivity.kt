package org.techtown.gabojago.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ActivitySplashBinding
import org.techtown.gabojago.start.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            binding.splashView.visibility = View.VISIBLE
            val animation01 = AnimationUtils.loadAnimation(this, R.anim.anim_toleft_splash_01)
            binding.splashView.startAnimation(animation01)
        }, 200)

        Handler().postDelayed({
            val animationAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_splash)
            binding.splashLogoBarIv.startAnimation(animationAlpha)
        }, 700)

        Handler().postDelayed({
            val animationAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_toleft_splash_02)
            binding.splashLogoIv.startAnimation(animationAlpha)
        }, 1800)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.anim_alpha_start_longer, R.anim.anim_none)
            finish()
        }, 2500)
    }
}