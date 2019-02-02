package com.theapphideaway.quickweather.Services

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.*
import com.theapphideaway.quickweather.MainActivity
import kotlinx.android.synthetic.main.content_main.*

class LocationService(private val mainActivity: MainActivity) {

    var FINE_LOCATION_REQUEST: Int = 888



    lateinit var locationRequest: LocationRequest

    @SuppressLint("MissingPermission")
    //Start Location update as define intervals
    fun initLocationUpdate(){

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val locationSettingBuilder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        locationSettingBuilder.addLocationRequest(locationRequest)
        val locationSetting: LocationSettingsRequest = locationSettingBuilder.build()

        val settingsClient: SettingsClient = LocationServices.getSettingsClient(mainActivity)
        settingsClient.checkLocationSettings(locationSetting)

        val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mainActivity)

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {

                if (locationResult != null) {
                    mainActivity.getCurrentDetails(locationResult.lastLocation, null)
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
            }
        },
            Looper.myLooper())


    }

    fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(mainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            requestPermissions()
            return false
        }
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(mainActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            FINE_LOCATION_REQUEST)
    }




}