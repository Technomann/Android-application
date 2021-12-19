package com.example.mobwebhf.display.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.mobwebhf.R
import com.example.mobwebhf.data.Account
import com.example.mobwebhf.databinding.DialogNewAccountBinding
import java.lang.Exception
import java.lang.RuntimeException

class AddAccountDialogFragment : AppCompatDialogFragment() {
    private lateinit var binding: DialogNewAccountBinding
    private lateinit var listener: AddAccountDialogListener

    interface AddAccountDialogListener{
        fun onAccountAdded(account: Account)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = DialogNewAccountBinding.inflate(LayoutInflater.from(context))

        try{
            listener = if(targetFragment != null){
                targetFragment as AddAccountDialogListener
            }else{
                activity as AddAccountDialogListener
            }
        }catch (e: Exception){
             throw RuntimeException(e)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.newAccount)
            .setView(binding.root)
            .setPositiveButton(R.string.ok) { _, _ ->
                listener.onAccountAdded(
                     Account(null, binding.NewAccountNumber.text.toString(),
                     binding.NewAccountStartingAmount.text.toString().toDouble())
                )
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
}