/*
package com.example.travelblog

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var marker: Marker
    lateinit var trackLocation: TrackLocation
    val TAG = "MapsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
    }

    */
/**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *//*

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMyLocationEnabled(true)
        mMap.setOnMyLocationClickListener(this)
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setMinZoomPreference(11f)

        val delta = LatLng(58.385254, 26.725064)
        mMap.addMarker(MarkerOptions().position(delta).title("Delta Centre"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(delta, 12F))

        val markerInfoWindowAdapter = WindowAdapter(applicationContext)
        googleMap.setInfoWindowAdapter(markerInfoWindowAdapter)

        mMap.clear()
        val markerOptions = MarkerOptions()
        markerOptions.position(delta)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(delta))
        marker = mMap.addMarker(markerOptions)

        googleMap.setOnInfoWindowClickListener(this)

        val location = getCurrentLocation()

        if (location != null) {
            val lat = getCurrentLocation()!!.latitude
            val lon = getCurrentLocation()!!.longitude
            val currentLocation = LatLng(lat, lon)

            mMap.addMarker(MarkerOptions().position(currentLocation).title("You are here "))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15F))
        }

        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(
                MarkerOptions()
                    .position(it)
//                    .title("New location!")
//                    .snippet("Population: 4,137,400")
//                .title("Location Details")
//                .snippet("I am custom Location Marker.")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }
        mMap.setOnInfoWindowClickListener {
            onInfoWindowClick(marker)
        }
    }

    fun getCurrentLocation(): Location? {
        val track_location = TrackLocation(this)
        val location = track_location.getCurrentLocation()
        return location
    }


    override fun onInfoWindowClick(p0: Marker?) {
        Toast.makeText(this, "Clicked info box", Toast.LENGTH_SHORT).show()
    }

    override fun onMyLocationClick(location: Location) {
        mMap.clear()
        mMap.addMarker(
            MarkerOptions()
                .position(LatLng(location.latitude, location.longitude))
        )
    }
}*/
