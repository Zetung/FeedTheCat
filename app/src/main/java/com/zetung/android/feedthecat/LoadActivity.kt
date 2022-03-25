package com.zetung.android.feedthecat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LoadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

        supportActionBar?.hide()

        Handler().postDelayed({
            startActivity(Intent(this@LoadActivity,MainActivity::class.java))
        },2000)
    }
}