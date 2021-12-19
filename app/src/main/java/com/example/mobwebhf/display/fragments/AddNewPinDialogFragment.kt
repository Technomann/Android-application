package com.example.mobwebhf.display.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.mobwebhf.R
import com.example.mobwebhf.databinding.DialogNewPinBinding
import java.lang.Exception
import java.lang.RuntimeException

class AddNewPinDialogFragment : AppCompatDialogFragment() {
    private lateinit var binding: DialogNewPinBinding
    private lateinit var listener: AddNewPinDialogListener

    interface AddNewPinDialogListener{
        fun onNewPinAdded(pin: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = DialogNewPinBinding.inflate(LayoutInflater.from(context))

        try{
            listener = if(targetFragment != null){
                targetFragment as AddNewPinDialogListener
            }else{
                activity as AddNewPinDialogListener
            }
        }catch (e: Exception){
            throw RuntimeException(e)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.newPinProfile)
            .setView(binding.root)
            .setPositiveButton(R.string.okPinProfile) { _, _ ->
                listener.onNewPinAdded(binding.etNewPin.text.toString())
            }
            .setNegativeButton(R.string.cancelPinProfile, null)
            .create()
    }
}