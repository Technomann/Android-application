package com.example.mobwebhf.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.example.mobwebhf.R
import com.example.mobwebhf.data.StockItem
import com.example.mobwebhf.data.api.Stock
import com.example.mobwebhf.databinding.ItemStockBinding

class StockAdapter : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    private val items = mutableListOf<StockItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StockViewHolder(
        ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val item = items[position]

        holder.binding.ivStockType.setImageResource(getImageResource(item.name))
        holder.binding.tvFromWhat.text = item.name
        holder.binding.tvBase.text = item.base
        holder.binding.tvRate.text = "${item.value} ${item.base}"
        holder.binding.tvDate.text = item.date
    }

    override fun getItemCount(): Int = items.size

    inner class StockViewHolder(val binding: ItemStockBinding) : RecyclerView.ViewHolder(binding.root)

    @DrawableRes
    private fun getImageResource(name: String): Int{
        return when(name){
            "EUR" -> R.drawable.euro
            "USD" -> R.drawable.dollar
            "GBP" -> R.drawable.gbp
            "NGN" -> R.drawable.naira
            "INR" -> R.drawable.rupee
            "RUB" -> R.drawable.ruble
            "JPY" -> R.drawable.yen
            else -> R.drawable.euro
        }
    }

    fun addItem(stock: StockItem) {
        items.add(stock)
        notifyItemInserted(items.size - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(stock: List<StockItem>) {
        items.clear()
        items.addAll(stock)
        notifyDataSetChanged()
    }
}