package com.example.weatherapp.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adaptors.viewholders.SunInfoViewHolder
import com.example.weatherapp.databinding.SunitemlayoutBinding
import com.example.weatherapp.dataclasses.Daily
import com.example.weatherapp.utils.getDate
import com.example.weatherapp.utils.getTime

class SunAdaptor(val context: Context) : RecyclerView.Adapter<SunInfoViewHolder>() {

    private var mList: List<Daily> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SunInfoViewHolder {
        return SunInfoViewHolder(
            SunitemlayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SunInfoViewHolder, position: Int) {
        val singleItem = mList[position]
        with(holder.binding) {
            tvSunRise.text = singleItem.sunrise.toLong().getTime()
            tvSunSet.text = singleItem.sunset.toLong().getTime()
            tvTime.text = singleItem.dt.toLong().getDate()
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