package com.example.travelblog

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker


class MainActivity : AppCompatActivity() {

    lateinit var mapsActivity: MapsActivity
    private val TAG = "MYAPI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: NB! LÕPUS KASUTA Camera.release()
        checkCameraHardware(this)

        mapsActivity = MapsActivity()
        showMap()
    }

    private fun showMap() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            val cameras = Camera.getNumberOfCameras()
//            val info = Camera.getCameraInfo(0)
            Log.i(TAG, "Cameras found: $cameras")
            true
        } else {
            Log.i(TAG, "Camera NOT FOUND!!!")
            false
        }
    }
}
