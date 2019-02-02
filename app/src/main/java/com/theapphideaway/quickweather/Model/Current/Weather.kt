package com.theapphideaway.quickweather.Model.Current

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)