package com.fictivestudios.ravebae.presentation.activites

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            //  startActivity(Intent(this,ResgistrationActivity::class.java))
            //  finish()

            /*
            Checking User data on local
            * */
            val userData = ConfigurationPref(this@SplashActivity).user
            Log.d("User", userData.toString())
            if (TextUtils.isEmpty(userData)) {
                startActivity(Intent(this, ResgistrationActivity::class.java))
                finish()
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

        }, 3000)
    }


    override fun setMainFrameLayoutID() {

    }
}