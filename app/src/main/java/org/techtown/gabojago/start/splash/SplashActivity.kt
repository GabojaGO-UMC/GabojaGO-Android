package org.techtown.gabojago.start.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ActivitySplashBinding
import org.techtown.gabojago.main.*
import org.techtown.gabojago.start.login.AuthService
import org.techtown.gabojago.start.login.LoginActivity
import org.techtown.gabojago.start.login.RemainLoginView

class SplashActivity : AppCompatActivity(), RemainLoginView {

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
            if(getBooleanJwt(this, "remainLogin") && (getJwt(this@SplashActivity, "userJwt") != "")){ //If remain Login checkbox is checked(true)
                val authService = AuthService()
                authService.setRemainLoginView(this@SplashActivity)

                val userJwt = getJwt(this@SplashActivity, "userJwt")
                Log.d("USERJWT", userJwt)
                authService.remainLogin(userJwt)
            }
            else{
                startActivity(Intent(this, LoginActivity::class.java))
                overridePendingTransition(R.anim.anim_alpha_start_longer, R.anim.anim_none)
                finish()
            }
        }, 2500)
    }

    override fun onRemainLoginSuccess(isRemain: Boolean) {
        if(isRemain){
            var intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.anim_alpha_start_longer, R.anim.anim_none)
            finish()
        }
    }

    override fun onRemainLoginFailure(code: Int, message: String) {
        when(code){
            2000, 3000 -> {
                MyToast.createToast(
                    baseContext, "로그인 시스템에 문제가 발생하였습니다.", 50, true
                ).show()
                Log.d("LOGINERROR", message)
            }
            else -> {
                MyToast.createToast(
                    baseContext, message, 50, true
                ).show()
            }
        }
    }
}