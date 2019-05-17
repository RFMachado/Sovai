package com.rafael.sovai.rules

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rafael.sovai.R
import kotlinx.android.synthetic.main.activity_rules.*

class RulesActivity: AppCompatActivity() {

    companion object {
        fun launchIntent(context: Context) = Intent(context, RulesActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        webView.settings.javaScriptEnabled = true
        webView.loadUrl("file:///android_asset/rules.html")

        bindListeners()

    }

    private fun bindListeners() {
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }

}