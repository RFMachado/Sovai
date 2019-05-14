package com.rafael.sovai

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity: AppCompatActivity(), OnCustomEventListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        activityLayout.mListener = this
    }

    override fun onBackMenu() {
        val intent = MainActivity.launchIntent(this)
        startActivity(intent)
        finish()
    }

}