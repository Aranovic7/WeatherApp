package com.example.weatherapp.activities;

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityPermissionsBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class Permissions : BaseActivity() {
    private val cam_PERMISSION_REQUEST_CODE: Int = 12

    var cam_counter = 0

    var cam_per = false
    private lateinit var binding: ActivityPermissionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<MaterialCardView>(R.id.cam_card1).setBackgroundResource(R.color.tansparent)
        binding.btnCamPermision.setOnClickListener {
            cam_checkPermission()
        }
        binding.btnAcceptAllpermisions.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun btn_enable(cam: Boolean) {
        cam_per = cam

        if (cam) {
            binding.btnAcceptAllpermisions.apply {
                isEnabled = true
                setBackgroundColor(resources.getColor(R.color.colorPrimary));
            }
        } else {


        }

    }

    private fun cam_checkPermission() {
        val result3 = ContextCompat.checkSelfPermission(
            this@Permissions,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (result3 == PackageManager.PERMISSION_GRANTED) {

        } else {
            cam_requestPermission()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cam_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    binding.animCam.visibility = View.VISIBLE
                    binding.btnCamPermision.visibility = View.INVISIBLE
                    binding.btnCamPermision.isEnabled = false
                    cam_per = true
                    btn_enable(cam_per)

                } else {
                    // cam_chak = true
                    cam_counter++
                    if (cam_counter >= 3) {
                        permissions_intent()


                    }
                    //cam_checkPermission()

                }


            } else {


                //cam_requestPermission()
            }
        }

    }

    private fun permissions_intent() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun cam_requestPermission() {

        ActivityCompat.requestPermissions(
            this@Permissions, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            cam_PERMISSION_REQUEST_CODE
        )
    }


}

