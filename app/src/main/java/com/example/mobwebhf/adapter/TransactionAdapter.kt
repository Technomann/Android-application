package com.example.mobwebhf.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.example.mobwebhf.R
import com.example.mobwebhf.data.Transaction
import com.example.mobwebhf.databinding.ItemTransactionBinding

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    private val items = mutableListOf<Transaction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TransactionViewHolder(
        ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = items[position]

        holder.binding.tvTransactionTitle.text = item.title
        holder.binding.tvTransactionAmount.text = "${item.amount} Ft"
        holder.binding.tvTransactionDate.text = item.date
        holder.binding.ivTransactionIcon.setImageResource(getImageResource(item.income))
    }

    @DrawableRes
    private fun getImageResource(income: Boolean): Int{
        return if(income)
            R.drawable.plus
        else
            R.drawable.minus
    }

    override fun getItemCount(): Int = items.size

    inner class TransactionViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root)

    fun addItem(transaction: Transaction) {
        items.add(transaction)
        notifyItemInserted(items.size - 1)
    }

    fun update(transaction: List<Transaction>) {
        items.clear()
        items.addAll(transaction)
        notifyDataSetChanged()
    }
}