package com.theapphideaway.quickweather.Services

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.theapphideaway.quickweather.Model.Forcast.ForcastResponse
import com.theapphideaway.quickweather.Model.Forcast.X
import com.theapphideaway.quickweather.R
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.forcast_card.view.*
import java.util.*


class WeatherAdapter( private val mContext: Context, private val forcastResponse: ForcastResponse) : BaseAdapter() {

    var uri: String? = null

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    override fun getCount(): Int {
        return forcastResponse.list!!.size
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var day= forcastResponse.list[position].dt.toLong()
        var sdf = java.text.SimpleDateFormat("EEEE")
        var dateFormat = Date(day * 1000)
        var weekDay = sdf.format(dateFormat)

        var sdfDate = java.text.SimpleDateFormat("MMM dd")
        var forcastDate = sdfDate.format((dateFormat))


        val inflater = mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var weatherCard = inflater.inflate(R.layout.forcast_card,null)

        weatherCard.temp_text_view.text = forcastResponse.list!![position].temp.day.toInt().toString()
        weatherCard.hi_text_view.text = "HI " + forcastResponse.list!![position].temp.max.toInt().toString()
        weatherCard.lo_text_view.text = "LO " + forcastResponse.list!![position].temp.min.toInt().toString()
        weatherCard.day_text_view.text = weekDay
        weatherCard.date_text_view.text = forcastDate
        weatherCard.description_text_view.text = forcastResponse.list!![position].weather[0].description

        if(forcastResponse.list!![position].weather[0].description.contains("rain")){
            uri = "@drawable/shower3"
        }
        else if(forcastResponse.list!![position].weather[0].description.contains("clear")){
            uri = "@drawable/sunny"
        }
        else if(forcastResponse.list!![position].weather[0].description.contains("snow")){
            uri = "@drawable/snow5"
        }
        else if(forcastResponse.list!![position].weather[0].description.contains("cloud")){
            uri = "@drawable/cloudy2"
        }
        else{
            uri = "@drawable/dunno"
        }

        var imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());

        weatherCard.weather_icon.setImageDrawable(ContextCompat.getDrawable(mContext,imageResource))
        return weatherCard
    }


}