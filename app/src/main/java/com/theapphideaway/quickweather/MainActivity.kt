package com.theapphideaway.quickweather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.location.*
import com.theapphideaway.quickweather.Model.LocationDetails
import com.theapphideaway.quickweather.Services.WeatherAdapter
import com.theapphideaway.quickweather.Services.WeatherService

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.app.ProgressDialog



const val REQUEST_LOCATION_PERMISSION = 1

class MainActivity : AppCompatActivity() {

    var weather = WeatherService()
    var url :String? = null
    var forcastUrl :String? = null

    var TAG: String = "MainActivity"
    var FINE_LOCATION_REQUEST: Int = 888


    lateinit var locationRequest: LocationRequest
    private var locationManager : LocationManager? = null
    var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val mDialog = ProgressDialog(this)
        mDialog.setMessage("Please wait...")
        mDialog.setCancelable(false)
        mDialog.show()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        url_search_edit_text.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                var searchText = url_search_edit_text.text.toString()

                getCurrentDetails(null, searchText)

                //Temp isnt changing. Make sure ime options are enabled, check url, and make sure the data is being sent

                var details =  weather.getCurrentWeather(url!!)

                city_text_view.text = details.City
                temperature_text_view.text = details.CurrentTemp.toString() + "℉"
                hi_text_view.text = "HI " + details.HighTemp.toString() + "℉"
                low_text_view.text = "LO " + details.LowTemp.toString() + "℉"
                return@OnKeyListener true

            }
            false
        })

        swipeContainer.setOnRefreshListener {
            initLocationUpdate()

            swipeContainer.isRefreshing = false

        }



        if(checkPermissions()) {
            initLocationUpdate()

        }


        mDialog.dismiss()

    }


    //TODO Create a geolocation service and add these methods in there.


    @SuppressLint("MissingPermission")
    //Start Location update as define intervals
    fun initLocationUpdate(){

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val locationSettingBuilder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        locationSettingBuilder.addLocationRequest(locationRequest)
        val locationSetting: LocationSettingsRequest = locationSettingBuilder.build()

        val settingsClient: SettingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSetting)

        val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {

                if (locationResult != null) {
                    getCurrentDetails(locationResult.lastLocation, null)
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
            }
        },
            Looper.myLooper())


    }

    fun getCurrentDetails(location: Location?, city:String?){

        if(location != null){
            var lat = location!!.latitude
            var long = location!!.longitude
            url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$long&units=imperial&appid=c6afdab60aa89481e297e0a4f19af055"
            forcastUrl = "https://api.openweathermap.org/data/2.5/forecast/daily?lat=$lat&lon=$long&cnt=15&units=imperial&appid=c6afdab60aa89481e297e0a4f19af055"

        } else if(city != null){
            url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=imperial&appid=c6afdab60aa89481e297e0a4f19af055"
            forcastUrl = "https://api.openweathermap.org/data/2.5/forecast/daily?q=$city&cnt=15&units=imperial&appid=c6afdab60aa89481e297e0a4f19af055"
        }
        var geoTemp = weather.getCurrentWeather(url!!)
        var geoForcast = weather.getForcast(forcastUrl!!)

        city_text_view.text = geoTemp.City
        temperature_text_view.text = geoTemp.CurrentTemp.toString() + "℉"
        hi_text_view.text = "HI " + geoTemp.HighTemp.toString() + "℉"
        low_text_view.text = "LO " + geoTemp.LowTemp.toString() + "℉"

        var uri = "@drawable/fog"

        if(geoForcast.forcasts!![0].Description!!.contains("rain")){
             uri = "@drawable/shower3"
        }
        else uri = "@drawable/overcast"

  // where myresource (without the extension) is the file

        var imageResource = getResources().getIdentifier(uri, null, getPackageName());

        var res = resources.getDrawable(imageResource)
        main_weather_image.setImageDrawable(res)



        grid_view_main.adapter = WeatherAdapter(this, geoForcast)
        //TODO add forcast textviews here:



    }






    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            requestPermissions()
            return false
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            FINE_LOCATION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == FINE_LOCATION_REQUEST) {
            // Received permission result for Location permission.
            Log.i(TAG, "Received response for Location permission request.")

            // Check if the only required permission has been granted
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i(TAG, "Location permission has now been granted. Now call initLocationUpdate")
                initLocationUpdate()
            } else {


            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}