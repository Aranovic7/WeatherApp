package com.example.weatherapp.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.adaptors.viewholders.HourlyViewHolder
import com.example.weatherapp.databinding.HourlyitemlayoutBinding
import com.example.weatherapp.dataclasses.Hourly
import com.example.weatherapp.utils.getCelsiusTemperature
import com.example.weatherapp.utils.getTime
import com.example.weatherapp.utils.setImage

class HourlyAdaptor(val context: Context) : RecyclerView.Adapter<HourlyViewHolder>() {

    private var mList: List<Hourly> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(
            HourlyitemlayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val singleItem = mList[position]
        with(holder.binding) {
            tvWeatherTem.text = singleItem.temp.toFloat().getCelsiusTemperature()
            weatherIcon.setImage(R.drawable.light_cloud)
            tvTime.text = singleItem.dt.toLong().getTime()
        }
    }

    fun submitData(newList: List<Hourly>) {
        mList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}