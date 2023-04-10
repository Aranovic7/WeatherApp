package com.example.weatherapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import com.example.weatherapp.R

class SplashActivity : BaseActivity() {
    private var handler: Handler? = null
    private val TAG = "SplashActivity"
    private var runnable: Runnable? = null

    var checkpermision = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        cam_checkPermission()

        handler = Handler(mainLooper)
        runnable = Runnable { launchHome() }
        handler!!.postDelayed(runnable!!, 3000)
    }

    private fun launchHome() {
        if (!checkpermision) {
            val intent = Intent(this@SplashActivity, Permissions::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun cam_checkPermission() {
        checkpermision = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

}