package com.example.weatherapp.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adaptors.viewholders.ButtonsViewHolder
import com.example.weatherapp.databinding.ButtonsitemlayoutBinding
import com.example.weatherapp.dataclasses.ButtonDataClass
import com.example.weatherapp.utils.setImage

class ButtonsAdaptor(val context: Context) : RecyclerView.Adapter<ButtonsViewHolder>() {

    private var buttonList: List<ButtonDataClass> = ArrayList()
    var onButtonClick: ((ButtonDataClass, Int) -> Unit?)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonsViewHolder {
        return ButtonsViewHolder(
            ButtonsitemlayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ButtonsViewHolder, position: Int) {
        val singleItem = buttonList[position]
        with(holder.binding) {
            tvButtonText.text = singleItem.buttonName
            buttonImage.setImage(singleItem.buttonImage)
            mButton.setOnClickListener {
                onButtonClick?.invoke(singleItem, position)
            }
        }
    }

    fun submitButtons(newList: List<ButtonDataClass>) {
        buttonList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return buttonList.size
    }

}