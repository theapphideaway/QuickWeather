package com.theapphideaway.quickweather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.KeyEvent
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.app.ProgressDialog
import com.google.android.gms.location.*
import com.theapphideaway.quickweather.Services.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class MainActivity : AppCompatActivity() {

    var weather = WeatherService()
    val weatherClass = WeatherClass()
    val forcastService = ForcastService()
    val locationService = LocationService(this)
    var url :String? = null

    var TAG: String = "MainActivity"
    var FINE_LOCATION_REQUEST: Int = 888


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

                return@OnKeyListener true

            }
            false
        })

        swipeContainer.setOnRefreshListener {
            locationService.initLocationUpdate()

            swipeContainer.isRefreshing = false
        }

        if(locationService.checkPermissions()) {
            locationService.initLocationUpdate()
        }
        mDialog.dismiss()
    }

    fun getCurrentDetails(location: Location?, city:String?){

        if(city != null) {


            GlobalScope.launch(Dispatchers.Main) {
                val response = weatherClass.getJSONApi().getCurrentWeather(
                    city!!,
                    "imperial", "c6afdab60aa89481e297e0a4f19af055"
                ).await()
                temperature_text_view.text = response.main.temp.toInt().toString()

                city_text_view.text = response.name.toString()
                hi_text_view.text = "HI " + response.main.temp_max.toInt().toString() + "℉"
                low_text_view.text = "LO " + response.main.temp_min.toInt().toString() + "℉"
                var uri = "@drawable/fog"

                if (response.weather[0].description.contains("rain")) {
                    uri = "@drawable/shower3"
                } else if (response.weather[0].description.contains("clear")) {
                    uri = "@drawable/sunny"
                } else if (response.weather[0].description.contains("snow")) {
                    uri = "@drawable/snow5"
                } else if (response.weather[0].description.contains("cloud")) {
                    uri = "@drawable/cloudy2"
                }

                var imageResource = getResources().getIdentifier(uri, null, getPackageName());

                var res = resources.getDrawable(imageResource)
                main_weather_image.setImageDrawable(res)


                println(response)


                val forcastRespose = forcastService.getJSONApi().getForcast(
                    city,
                    15, "imperial", "c6afdab60aa89481e297e0a4f19af055"
                ).await()

                println(forcastRespose)
                grid_view_main.adapter = WeatherAdapter(this@MainActivity, forcastRespose)
            }
        }
    }
    
    // TODO Create a function that gets the weather of the current lat/long coords of the device

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == FINE_LOCATION_REQUEST) {
            // Received permission result for Location permission.
            Log.i(TAG, "Received response for Location permission request.")

            // Check if the only required permission has been granted
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i(TAG, "Location permission has now been granted. Now call initLocationUpdate")
                locationService.initLocationUpdate()
            } else {


            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}