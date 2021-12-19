package com.example.mobwebhf.display.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.mobwebhf.R
import com.example.mobwebhf.data.Limit
import com.example.mobwebhf.databinding.DialogNewLimitBinding
import java.lang.Exception
import java.lang.RuntimeException
import android.util.Log

class AddLimitDialogFragment : AppCompatDialogFragment() {
    private lateinit var binding: DialogNewLimitBinding
    private lateinit var listener: AddLimitDialogListener

    interface AddLimitDialogListener{
        fun onLimitAdded(limit: Limit)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = DialogNewLimitBinding.inflate(LayoutInflater.from(context))

        try{
            listener = if(targetFragment != null)
                targetFragment as AddLimitDialogListener
            else
                activity as AddLimitDialogListener
        }catch (e: Exception){
            throw RuntimeException(e)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.newLimit)
            .setView(binding.root)
            .setPositiveButton(R.string.okLimit) { _, _ ->
                Log.e("PositiveButton", "Ide eljutottam")
                listener.onLimitAdded(
                    Limit(null,
                        binding.etLimitAmount.text.toString().toDouble(),
                        binding.etLimitAccount.text.toString())
                )
            }
            .setNegativeButton(R.string.cancelLimit, null)
            .create()
    }
}