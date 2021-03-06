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
                binding.etProfileName.error = "Nem adhatsz meg ??res nevet!"
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

            if (accounts.isNotEmpty()){//Ha l??tez?? sz??mlasz??m lett be??rva
                if (accounts[0].limitId == null) {//Ha ennek a sz??mlasz??mnak m??g nincs limit-je
                        val insertId = database.limitDao().insert(limit)
                        limit.id =
                            insertId//Elmentj??k az adatb??zisba az ??j limitet, ??s az ??tvett limit id-j??t friss??tj??k

                        val tempAccount = accounts[0]
                        tempAccount.limitId =
                            insertId//Az accounthoz tartoz?? limitId-t is friss??tj??k, majd vissza??rjuk az adatb??zisba
                        database.accountDao().update(tempAccount)

                        runOnUiThread {
                            limitAdapter.addLimit(limit)
                        }
                } else
                    runOnUiThread {
                        Toast.makeText(
                            this@ProfileActivity,
                            "Hiba: M??r l??tezik ehhez a sz??mlasz??mhoz limit!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            } else
                runOnUiThread {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Hiba: Nincs ilyen sz??mlasz??m!",
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