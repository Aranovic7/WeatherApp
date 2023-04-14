package com.example.weatherapp.adaptors

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.weatherapp.adaptors.viewholders.SunInfoViewHolder
import com.example.weatherapp.databinding.SunitemlayoutBinding
import com.example.weatherapp.dataclasses.Hourly
import com.example.weatherapp.utils.beGone
import com.example.weatherapp.utils.getTimeDate

class HumanidityAdaptor(val context: Context) : RecyclerView.Adapter<SunInfoViewHolder>() {

    private var mList: List<Hourly> = ArrayList()
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
            tvTime.text = singleItem.dt.toLong().getTimeDate()
            tv2.gravity = Gravity.END or Gravity.CENTER_VERTICAL
            tv2.text = "${singleItem.humidity}%"
            secondLayout.beGone()
            tv1.beGone()
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