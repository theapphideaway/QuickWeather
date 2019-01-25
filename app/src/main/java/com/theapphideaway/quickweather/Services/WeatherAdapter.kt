package com.theapphideaway.quickweather.Services

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.theapphideaway.quickweather.R
import kotlinx.android.synthetic.main.forcast_card.view.*


class WeatherAdapter( private val mContext: Context, private val groupedForcast: GroupedForcast) : BaseAdapter() {

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    override fun getCount(): Int {
        return groupedForcast.forcasts!!.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        val inflater = mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var weatherCard = inflater.inflate(R.layout.forcast_card,null)

        weatherCard.temp_text_view.text = groupedForcast.forcasts!![position].Temp.toString()
        weatherCard.hi_text_view.text = "HI " + groupedForcast.forcasts!![position].HighTemp.toString()
        weatherCard.lo_text_view.text = "LO " + groupedForcast.forcasts!![position].LowTemp.toString()
        weatherCard.day_text_view.text = groupedForcast.forcasts!![position].Day
        weatherCard.date_text_view.text = groupedForcast.forcasts!![position].Date
        return weatherCard
    }


}