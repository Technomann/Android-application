package com.example.mobwebhf.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobwebhf.R
import com.example.mobwebhf.data.Account
import com.example.mobwebhf.databinding.ItemAccountBinding
import com.example.mobwebhf.databinding.ItemTransactionBinding

class AccountAdapter(private val listener: OnAccountSelectedListener)
    : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>(){

    private var selectedItem: Int = -1
    private val accounts: MutableList<Account> = mutableListOf()

    interface OnAccountSelectedListener{
        fun onAccountSelected(account: Account?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :AccountViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val item = accounts[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int = accounts.size

    fun addAccount(account: Account){
        accounts.add(account)
        notifyItemInserted(accounts.size - 1)
    }

    fun update(newAccounts: List<Account>) {
        accounts.clear()
        accounts.addAll(newAccounts)
        notifyDataSetChanged()
    }

    inner class AccountViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding = ItemAccountBinding.bind(itemView)
        var newAccount: Account? = null

        init{
            binding.root.setOnClickListener {
                listener.onAccountSelected(newAccount)
                selectedItem = adapterPosition
                notifyDataSetChanged()
            }
        }

        fun bind(na: Account?, position: Int){
            newAccount = na

            binding.tvAccountItemNumber.text = newAccount?.account_number
            binding.tvAccountItemAmount.text = "${newAccount?.value} Ft"

            if(selectedItem == position)
                itemView.setBackgroundColor(Color.parseColor("#30c25a"))
            else
                itemView.setBackgroundColor(Color.parseColor("#454545"))
        }
    }

}