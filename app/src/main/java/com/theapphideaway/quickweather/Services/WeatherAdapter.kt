package com.theapphideaway.quickweather.Services

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.theapphideaway.quickweather.Model.Forcast.X
import com.theapphideaway.quickweather.R
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.forcast_card.view.*


class WeatherAdapter( private val mContext: Context, private val groupedForcast: X) : BaseAdapter() {

    var uri: String? = null

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    override fun getCount(): Int {
        //return groupedForcast.forcasts!!.size
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        val inflater = mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var weatherCard = inflater.inflate(R.layout.forcast_card,null)

//        weatherCard.temp_text_view.text = groupedForcast.forcasts!![position].Temp.toString()
//        weatherCard.hi_text_view.text = "HI " + groupedForcast.forcasts!![position].HighTemp.toString()
//        weatherCard.lo_text_view.text = "LO " + groupedForcast.forcasts!![position].LowTemp.toString()
//        weatherCard.day_text_view.text = groupedForcast.forcasts!![position].Day
//        weatherCard.date_text_view.text = groupedForcast.forcasts!![position].Date
//        weatherCard.description_text_view.text = groupedForcast.forcasts!![position].Description
//
//        if(groupedForcast.forcasts!![position].Description!!.contains("rain")){
//            uri = "@drawable/shower3"
//        }
//        else if(groupedForcast.forcasts!![position].Description!!.contains("clear")){
//            uri = "@drawable/sunny"
//        }
//        else if(groupedForcast.forcasts!![position].Description!!.contains("snow")){
//            uri = "@drawable/snow5"
//        }
//        else if(groupedForcast.forcasts!![position].Description!!.contains("cloud")){
//            uri = "@drawable/cloudy2"
//        }
//        else{
//            uri = "@drawable/dunno"
//        }

        var imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());

        weatherCard.weather_icon.setImageDrawable(ContextCompat.getDrawable(mContext,imageResource))
        return weatherCard
    }


}