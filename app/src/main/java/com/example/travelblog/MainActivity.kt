package com.example.travelblog

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker


class MainActivity : AppCompatActivity() {

    lateinit var mapsActivity: MapsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapsActivity = MapsActivity()
        showMap()
    }

    private fun showMap() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

}
