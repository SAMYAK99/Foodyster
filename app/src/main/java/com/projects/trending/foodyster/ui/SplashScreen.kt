package com.projects.trending.foodyster.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.ui.onBoarding.viewPagerFragment


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val i = Intent(this , viewPagerFragment::class.java)
            startActivity(i)
            finish()
        },3000)
    }
}