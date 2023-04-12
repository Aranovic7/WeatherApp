package com.example.weatherapp.adaptors

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R

import com.example.weatherapp.adaptors.viewholders.DateViewHolder
import com.example.weatherapp.databinding.DateitemlayoutBinding
import com.example.weatherapp.dataclasses.Daily
import com.example.weatherapp.dataclasses.DateDataClass
import com.example.weatherapp.utils.getDay
import com.example.weatherapp.utils.getDayDate

class DateAdaptor(val context: Context) : RecyclerView.Adapter<DateViewHolder>() {

    private var mList: List<DateDataClass> = ArrayList()
    var onItemClick: ((Daily, Int) -> Unit?)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(
            DateitemlayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val singleItem = mList[position]
        with(holder.binding) {
            tvDay.text = singleItem.daily.dt.toLong().getDay()[0].toString()
            tvDate.text = singleItem.daily.dt.toLong().getDayDate()
            if (singleItem.isSelected) {
                tvDate.selectDate()
            } else {
                tvDate.deSelectDate()
            }
            root.setOnClickListener {
                onItemClick?.invoke(singleItem.daily, position)
                selectDate(position)
            }
        }
    }

    private fun TextView.selectDate() {
        this.apply {
            background = ContextCompat.getDrawable(this@DateAdaptor.context, R.drawable.ecllipsbg)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@DateAdaptor.context,
                    R.color.white
                )
            )
        }
    }

    private fun TextView.deSelectDate() {
        this.apply {
            background = null
        }
    }

    fun selectDate(position: Int) {
        mList.forEach {
            if (it.isSelected) {
                it.isSelected = false
            }
        }
        mList[position].isSelected = true
        notifyDataSetChanged()
    }

    fun submitData(newList: List<DateDataClass>) {
        mList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}