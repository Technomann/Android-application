package com.example.mobwebhf.display

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobwebhf.LoginActivity
import com.example.mobwebhf.adapter.LimitAdapter
import com.example.mobwebhf.data.Account
import com.example.mobwebhf.data.Limit
import com.example.mobwebhf.data.TransactionDatabase
import com.example.mobwebhf.databinding.ActivityProfileBinding
import com.example.mobwebhf.display.fragments.AddLimitDialogFragment
import com.example.mobwebhf.display.fragments.AddNewPinDialogFragment
import kotlin.concurrent.thread

class ProfileActivity : AppCompatActivity(), AddLimitDialogFragment.AddLimitDialogListener,
LimitAdapter.OnLimitRemovedListener, AddNewPinDialogFragment.AddNewPinDialogListener{
    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: TransactionDatabase
    private lateinit var limitAdapter: LimitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = TransactionDatabase.getDatabase(applicationContext)

        setName()
        initButtons()
        initRecyclerView()
    }

    private fun setName(){
        val sharedPref = getSharedPreferences(LoginActivity::class.java.simpleName, Context.MODE_PRIVATE)
        val name: String = sharedPref.getString("profile_name", "")!!
        binding.etProfileName.setText(name)
    }

    private fun initButtons(){
        binding.btNewLimit.setOnClickListener {
            AddLimitDialogFragment().show(supportFragmentManager, AddLimitDialogFragment::class.java.simpleName)
        }

        binding.btNewPin.setOnClickListener {
            AddNewPinDialogFragment().show(supportFragmentManager, AddNewPinDialogFragment::class.java.simpleName)
        }

        binding.btSaveName.setOnClickListener {
            if(binding.etProfileName.text.toString() == ""){
                binding.etProfileName.error = "Nem adhatsz meg üres nevet!"
            }else {
                val sharedPref = this.getSharedPreferences(LoginActivity::class.java.simpleName, Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("profile_name", binding.etProfileName.text.toString())
                    apply()
                }
            }
        }
    }

    private fun initRecyclerView(){
        limitAdapter = LimitAdapter(this)
        binding.rvLimits.layoutManager = LinearLayoutManager(this)
        binding.rvLimits.adapter = limitAdapter
        loadItemsInBackgroundLimit()
    }

    private fun loadItemsInBackgroundLimit(){
        thread{
            val items = database.limitDao().getAll()
            runOnUiThread{
                limitAdapter.update(items)
            }
        }
    }

    override fun onLimitAdded(limit: Limit) {
        thread {
            val accounts = database.accountDao().getAccountByNumber(limit.accountNumber)

            if (accounts.isNotEmpty()){//Ha létező számlaszám lett beírva
                if (accounts[0].limitId == null) {//Ha ennek a számlaszámnak még nincs limit-je
                        val insertId = database.limitDao().insert(limit)
                        limit.id =
                            insertId//Elmentjük az adatbázisba az új limitet, és az étvett limit id-ját frissítjük

                        val tempAccount = accounts[0]
                        tempAccount.limitId =
                            insertId//Az accounthoz tartozó limitId-t is frissítjük, majd visszaírjuk az adatbázisba
                        database.accountDao().update(tempAccount)

                        runOnUiThread {
                            limitAdapter.addLimit(limit)
                        }
                } else
                    runOnUiThread {
                        Toast.makeText(
                            this@ProfileActivity,
                            "Hiba: Már létezik ehhez a számlaszámhoz limit!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            } else
                runOnUiThread {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Hiba: Nincs ilyen számlaszám!",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    override fun onLimitRemoved(limit: Limit) {
        thread{
            database.limitDao().deleteLimit(limit)
            database.accountDao().resetLimitId(limit.accountNumber)
            runOnUiThread {
                limitAdapter.removeLimit(limit)
            }
        }
    }

    override fun onNewPinAdded(pin: String) {
        val sharedPref = this.getSharedPreferences(LoginActivity::class.java.simpleName,Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("pin_code", pin)
            apply()
        }
    }
}