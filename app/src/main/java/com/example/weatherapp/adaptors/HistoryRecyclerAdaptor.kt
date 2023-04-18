package com.example.weatherapp.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.adaptors.viewholders.HistoryItemViewHolder
import com.example.weatherapp.databinding.HistoryItemLayoutBinding
import com.example.weatherapp.dataclasses.HistoryDataClass
import com.example.weatherapp.utils.getCelsiusTemperature
import com.example.weatherapp.utils.getCityName
import com.example.weatherapp.utils.getTime
import com.example.weatherapp.utils.setImage
import kotlin.math.roundToInt

class HistoryRecyclerAdaptor(val context: Context) : RecyclerView.Adapter<HistoryItemViewHolder>() {
    private var mList: List<HistoryDataClass> = listOf()

    var onDeleteClick: ((HistoryDataClass) -> Unit?)? = null
    fun submitList(newList: List<HistoryDataClass>) {
        mList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(
            HistoryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val singleItem = mList[position]
        holder.binding.apply {
            tvWeatherTem.text = singleItem.temp.toFloat().getCelsiusTemperature()
            val roundedPressure =
                (singleItem.pressure.toFloat() ?: 0f).roundToInt()
            tvPressure.text = "$roundedPressure hPa"
            tvHumadity.text = Math.round(
                singleItem.humidity?.toFloat() ?: 0f
            ).toString() + " %"
            val windSpeed = singleItem.windSpeed.toFloat()
            tvWind.text = Math.round(windSpeed).toString() + " km/h"
            tvVisibility.text = singleItem.visibility.toString()
            weatherIcon.setImage(R.drawable.light_cloud)
            tvCityName.text = singleItem.city
            tvCloudy.text = "Cloudy ${singleItem.clouds}%"
            tvFeels.text = singleItem.feelsLike.toFloat()?.getCelsiusTemperature()
            tvTime.text = System.currentTimeMillis().getTime()
            btnDelete.setOnClickListener {
                onDeleteClick?.invoke(singleItem)
            }
        }
    }
}