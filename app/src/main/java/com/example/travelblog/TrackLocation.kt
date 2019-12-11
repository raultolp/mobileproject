package com.example.travelblog

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat


class TrackLocation(private val mContext: Context) : LocationListener {

    // Flag for GPS status
    var isGPSEnabled = false
    // Flag for network status
    var isNetworkEnabled = false
    // Flag for GPS status
    var canGetLocation = false
    lateinit var location: Location  // Location
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters
    // The minimum time between updates in milliseconds
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong() // 1 minute
    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null

    init {
        getCurrentLocation()
    }

    fun getCurrentLocation(): Location {
        try {
            locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            //check permission
            if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG ","No permission Granted")
            }else{

                if (!isNetworkEnabled) {
                } else {
                    this.canGetLocation = true
                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                    )
                    if (locationManager != null) {
                        location = locationManager!!
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    }
                }

                if (!isGPSEnabled) {
                } else {
                    this.canGetLocation = true
                    // If GPS enabled, get latitude/longitude using GPS Services
                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                    )
                    if (locationManager != null) {
                        location = locationManager!!
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return location
    }

    fun canWeGetLocation(): Boolean {
        return this.canGetLocation
    }

    override fun onLocationChanged(location: Location) {}
    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

}