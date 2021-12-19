package com.example.mobwebhf

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mobwebhf.databinding.ActivityLoginBinding
import com.example.mobwebhf.display.AccountsActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var pinValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        binding.btnNewPin.setOnClickListener {
            newPinListener()
        }

        binding.btnLogin.setOnClickListener{
            loginListener()
        }

        val login = checkPinExist()
        if(login) {
            binding.btnNewPin.visibility = View.GONE
            binding.etPinCode.hint = ""
        }
        else {
            binding.btnLogin.visibility = View.GONE
            binding.etPinCode.hint =
                "Ne felejtsd el leső megadás után, mert megváltoztatni csak belépés után tudod!"
        }
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        pinValue = sharedPref.getString("pin_code", "-1?")
    }

    private fun checkPinExist(): Boolean{
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        pinValue = sharedPref.getString("pin_code", "-1?")

        return !pinValue.equals("-1?")
    }

    private fun loginListener(){
        if(binding.etPinCode.text.toString() == pinValue)
            startActivity(Intent(this,AccountsActivity::class.java))
        else{
            binding.etPinCode.setText("")
            binding.etPinCode.error = "Rossz PIN kód!"
        }
    }

    private fun newPinListener(){
        if(binding.etPinCode.text.toString() == "")
            binding.etPinCode.error = "Üres PIN kódot nem adhatsz meg!"
        else{
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("pin_code", binding.etPinCode.text.toString())
                putString("profile_name", "Teszt Elek")
                apply()
            }
            startActivity(Intent(this,AccountsActivity::class.java))
        }
    }
}