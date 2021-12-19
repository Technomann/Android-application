package com.example.mobwebhf.display.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.mobwebhf.R
import com.example.mobwebhf.data.Transaction
import com.example.mobwebhf.databinding.DialogNewTransactionBinding
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddTransactionDialogFragment: AppCompatDialogFragment() {
    private lateinit var binding: DialogNewTransactionBinding
    private lateinit var listener: AddTransactionDialogListener

    interface AddTransactionDialogListener{
        fun onTransactionAdded(transaction: Transaction?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = DialogNewTransactionBinding.inflate(LayoutInflater.from(context))

        try{
            listener = if(targetFragment != null){
                targetFragment as AddTransactionDialogListener
            }else {
                activity as AddTransactionDialogListener
            }
        }catch (e: Exception){
            throw RuntimeException(e)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding.spExpenseIncome.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.expense_income)
        )

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.newTransaction)
            .setView(binding.root)
            .setPositiveButton(R.string.commit) { _, _ ->
                if(isValid()) {
                    listener.onTransactionAdded(
                        getTransactionItem()
                    )
                }
            }
            .setNegativeButton(R.string.cancelTransaction, null)
            .create()
    }

    private fun isValid(): Boolean{
        return (binding.etTransactionTitle.text.toString() != "" && binding.etAmount.text.toString() != "")
    }

    private fun getTransactionItem(): Transaction {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        return Transaction(
            id = null,
            title = binding.etTransactionTitle.text.toString(),
            amount = binding.etAmount.text.toString().toDouble(),
            date = time,
            income = binding.spExpenseIncome.selectedItemPosition != 0,
            accountId = -1
        )
    }
}