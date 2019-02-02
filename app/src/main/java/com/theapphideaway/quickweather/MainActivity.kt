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
import com.theapphideaway.quickweather.Services.WeatherService

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.app.ProgressDialog
import com.google.android.gms.location.*
import com.theapphideaway.quickweather.Services.LocationService
import com.theapphideaway.quickweather.Services.WeatherClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class MainActivity : AppCompatActivity() {

    var weather = WeatherService()
    val weatherClass = WeatherClass()
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
            locationService.initLocationUpdate()

            swipeContainer.isRefreshing = false
        }

        if(locationService.checkPermissions()) {
            locationService.initLocationUpdate()
        }

        mDialog.dismiss()

    }




    fun getCurrentDetails(location: Location?, city:String?){

        GlobalScope.launch(Dispatchers.Main) {
            val response = weatherClass.getJSONApi().getCurrentWeather("Tampa",
                "imperial", "c6afdab60aa89481e297e0a4f19af055").await()
            temperature_text_view.text = response.main.temp.toInt().toString()

            city_text_view.text = response.name.toString()
            hi_text_view.text = "HI " + response.main.temp_max.toInt().toString() + "℉"
            low_text_view.text = "LO " + response.main.temp_min.toInt().toString() + "℉"
            var uri = "@drawable/fog"

            if(response.weather[0].description.contains("rain")){
                uri = "@drawable/shower3"
            }
            else if(response.weather[0].description.contains("clear")){
                uri = "@drawable/sunny"
            }
            else if(response.weather[0].description.contains("snow")){
                uri = "@drawable/snow5"
            }
            else if(response.weather[0].description.contains("cloud")){
                uri = "@drawable/cloudy2"
            }

            var imageResource = getResources().getIdentifier(uri, null, getPackageName());

            var res = resources.getDrawable(imageResource)
            main_weather_image.setImageDrawable(res)


            println(response)
        }

//        if(location != null){
//            var lat = location!!.latitude
//            var long = location!!.longitude
//            url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$long&units=imperial&appid=c6afdab60aa89481e297e0a4f19af055"
//            forcastUrl = "https://api.openweathermap.org/data/2.5/forecast/daily?lat=$lat&lon=$long&cnt=15&units=imperial&appid=c6afdab60aa89481e297e0a4f19af055"
//
//        } else if(city != null){
//            url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=imperial&appid=c6afdab60aa89481e297e0a4f19af055"
//            forcastUrl = "https://api.openweathermap.org/data/2.5/forecast/daily?q=$city&cnt=15&units=imperial&appid=c6afdab60aa89481e297e0a4f19af055"
//        }
//        var geoTemp = weather.getCurrentWeather(url!!)
//        var geoForcast = weather.getForcast(forcastUrl!!)
//
//        city_text_view.text = geoTemp.City
//        temperature_text_view.text = geoTemp.CurrentTemp.toString() + "℉"
//        hi_text_view.text = "HI " + geoTemp.HighTemp.toString() + "℉"
//        low_text_view.text = "LO " + geoTemp.LowTemp.toString() + "℉"
//
//        var uri = "@drawable/fog"
//
//        if(geoForcast.forcasts!![0].Description!!.contains("rain")){
//             uri = "@drawable/shower3"
//        }
//        else uri = "@drawable/overcast"
//
//  // where myresource (without the extension) is the file
//
//        var imageResource = getResources().getIdentifier(uri, null, getPackageName());
//
//        var res = resources.getDrawable(imageResource)
//        main_weather_image.setImageDrawable(res)
//
//
//
//        grid_view_main.adapter = WeatherAdapter(this, geoForcast)

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
                locationService.initLocationUpdate()
            } else {


            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}