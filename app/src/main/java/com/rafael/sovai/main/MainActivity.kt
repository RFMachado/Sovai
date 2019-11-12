package com.rafael.sovai.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rafael.sovai.R
import com.rafael.sovai.game.GameActivity
import com.rafael.sovai.rules.RulesActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        fun launchIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLeave.setOnClickListener {
            finish()
        }

        btnRules.setOnClickListener {
            val intent = RulesActivity.launchIntent(this)
            startActivity(intent)
        }
    }

}
