package com.udacity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator.INFINITE
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var radioButtonText: String
    private var downloadID: Long = 0
    private lateinit var animator: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        custom_button.setOnClickListener {

            if (radioGroup.checkedRadioButtonId != -1) {
                radioButtonText =
                    findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()
                download()
                animateButton(it as LoadingButton)

                custom_button.isClickable = false

            } else {
                Toast.makeText(this, getString(R.string.select_file_to_download), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun animateButton(it: LoadingButton) {
        custom_button.isLoading = true

        val widthAnimation = PropertyValuesHolder.ofInt("widthSize", 0, it.width)
        val circleAnimation = PropertyValuesHolder.ofFloat("arcAngle", 0f, 360f)
        animator = ObjectAnimator.ofPropertyValuesHolder(it, widthAnimation, circleAnimation)
        animator.duration = 1000

        animator.repeatCount = INFINITE
        animator.start()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {

                if (intent.action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    animator.cancel()
                    custom_button.isClickable = true

                    custom_button.isLoading = false
                    custom_button.invalidate()
                    context?.let { sendNotification(context, radioButtonText) }
                }
            }
        }
    }

    private fun download() {
        val url = when (radioButtonText) {
            getString(R.string.glide_image_loading_library_by_bumptech) -> GLIDE_URL
            getString(R.string.loadapp_current_repository_by_udacity) -> LOAD_APP_URL
            getString(R.string.retrofit_type_safe_http_client_for_android_and_java_by_square_inc) -> RETROFIT_URL
            else -> ""
        }
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val LOAD_APP_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit"
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
