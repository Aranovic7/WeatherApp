package com.example.weatherapp.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.adaptors.viewholders.WeeklyViewHolder
import com.example.weatherapp.databinding.WeekltitemlayoutBinding
import com.example.weatherapp.dataclasses.Daily
import com.example.weatherapp.utils.getCelsiusTemperature
import com.example.weatherapp.utils.getDay
import com.example.weatherapp.utils.setImage

class WeeklyAdaptor(val context: Context) : RecyclerView.Adapter<WeeklyViewHolder>() {

    private var mList: List<Daily> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyViewHolder {
        return WeeklyViewHolder(
            WeekltitemlayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeeklyViewHolder, position: Int) {
        val singleItem = mList[position]
        with(holder.binding) {
            tvTemperature.text = singleItem.temp.day.toFloat().getCelsiusTemperature()
            weatherIcon.setImage(R.drawable.light_cloud)
            tvDayName.text = singleItem.dt.toLong().getDay()
        }
    }

    fun submitData(newList: List<Daily>) {
        mList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}