package com.example.mobwebhf.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobwebhf.R
import com.example.mobwebhf.data.Limit
import com.example.mobwebhf.databinding.ItemLimitBinding

class LimitAdapter(private val listener: OnLimitRemovedListener) : RecyclerView.Adapter<LimitAdapter.LimitViewHolder>(){
    private val limits = mutableListOf<Limit>()

    interface OnLimitRemovedListener {
        fun onLimitRemoved(limit: Limit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LimitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_limit, parent, false)
        return LimitViewHolder(view)
    }

    override fun onBindViewHolder(holder: LimitViewHolder, position: Int) {
        val item = limits[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = limits.size

    inner class LimitViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding = ItemLimitBinding.bind(itemView)
        var newLimit: Limit? = null

        init{
            binding.btRemoveLimit.setOnClickListener {
                listener.onLimitRemoved(newLimit!!)
            }
        }

        fun bind(nl: Limit){
            newLimit = nl
            binding.tvLimitAmount.text = "${newLimit?.amount} Ft"
            binding.tvLimitAccount.text = newLimit?.accountNumber
        }
    }

    fun addLimit(limit: Limit) {
        limits.add(limit)
        notifyItemInserted(limits.size - 1)
    }

    fun update(limit: List<Limit>) {
        limits.clear()
        limits.addAll(limit)
        notifyDataSetChanged()
    }

    fun removeLimit(limit: Limit) {
        val position = limits.indexOf(limit)
        limits.removeAt(position)
        notifyItemRemoved(position)
        if (position < limits.size) {
            notifyItemRangeChanged(position, limits.size - position)
        }
    }
}