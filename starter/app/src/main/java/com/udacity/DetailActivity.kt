package com.udacity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        cancelNotifications(this)
        file_name.text = intent.extras?.getString(DOWNLOAD_DESCRIPTION)
        motion_layout.transitionToEnd()


    }


    @Suppress("BlockingMethodInNonBlockingContext")
    fun goToMain(view: View) {
        val context: Context = this
        motion_layout.transitionToStart()
        val applicationScope = CoroutineScope(Dispatchers.Default)
        applicationScope.launch {
            //this makes the user see the transition out animation
            Thread.sleep(2000)
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
