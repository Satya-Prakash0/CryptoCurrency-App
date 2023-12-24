package com.lokal.cryptocurrencyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lokal.cryptocurrencyapp.R
import com.lokal.cryptocurrencyapp.model.Currency
import javax.inject.Inject

class CurrencyAdapter @Inject constructor() : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private var currencyList: List<Currency> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencyList[position]

        // Set data to views
        holder.textFullName.text = currency.fullName
        holder.textExchangeRate.text = String.format("%.6f", currency.exchangeRate)

        // Load image using Glide library
        Glide.with(holder.itemView.context)
            .load(currency.iconUrl)
            .placeholder(R.mipmap.ic_launcher) // Placeholder image if loading fails
            .error(R.mipmap.ic_launcher) // Error image if loading fails
            .into(holder.imageIcon)
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    fun updateData(newCurrencyList: List<Currency>) {
        currencyList = newCurrencyList
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }


    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageIcon: ImageView = itemView.findViewById(R.id.imageIcon)
        val textFullName: TextView = itemView.findViewById(R.id.textFullName)
        val textExchangeRate: TextView = itemView.findViewById(R.id.textExchangeRate)
    }

}
