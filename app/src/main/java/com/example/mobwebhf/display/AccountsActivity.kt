package com.example.mobwebhf.display

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobwebhf.R
import com.example.mobwebhf.adapter.AccountAdapter
import com.example.mobwebhf.data.Account
import com.example.mobwebhf.data.TransactionDatabase
import com.example.mobwebhf.databinding.ActivityAccountsBinding
import com.example.mobwebhf.display.fragments.AddAccountDialogFragment
import java.util.*
import kotlin.concurrent.thread

class AccountsActivity : AppCompatActivity(), AccountAdapter.OnAccountSelectedListener,
    AddAccountDialogFragment.AddAccountDialogListener {

    private lateinit var binding: ActivityAccountsBinding
    private lateinit var database: TransactionDatabase
    private lateinit var accountAdapter: AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = TransactionDatabase.getDatabase(applicationContext)

        initButtons()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadItemsInBackgroundAccount()
    }

    private fun initButtons(){
        binding.fabNewAccount.setOnClickListener {
            AddAccountDialogFragment().show(supportFragmentManager, AddAccountDialogFragment::class.java.simpleName)
        }

        binding.ibStocks.setOnClickListener {
            val showStocksIntent = Intent()
            showStocksIntent.setClass(this@AccountsActivity, StocksActivity::class.java)
            startActivity(showStocksIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val toolbarMenu: Menu = binding.toolbar.menu
        menuInflater.inflate(R.menu.toolbar_menu, toolbarMenu)

        for(i in 0 until toolbarMenu.size()){
            val menuItem: MenuItem = toolbarMenu.getItem(i)
            menuItem.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.mnProfile -> {
                val showProfileIntent = Intent()
                showProfileIntent.setClass(this@AccountsActivity, ProfileActivity::class.java)
                startActivity(showProfileIntent)
                true
            }
            R.id.mnLogOut -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView(){
        accountAdapter = AccountAdapter(this)
        binding.rvAccounts.layoutManager = LinearLayoutManager(this)
        binding.rvAccounts.adapter = accountAdapter
        loadItemsInBackgroundAccount()
    }

    private fun loadItemsInBackgroundAccount(){
        thread{
            val items = database.accountDao().getAll()
            runOnUiThread{
                accountAdapter.update(items)
            }
        }
    }

    override fun onAccountSelected(account: Account?) {
        val showTransactionsIntent = Intent()

        showTransactionsIntent.setClass(this@AccountsActivity, TransactionActivity::class.java)
        showTransactionsIntent.putExtra(
            TransactionActivity.EXTRA_ACCOUNT_ID,
            account!!.id.toString().toInt()
        )
        showTransactionsIntent.putExtra(
            TransactionActivity.EXTRA_ACCOUNT_NUMBER,
            account.account_number
        )
        startActivity(showTransactionsIntent)
    }

    override fun onAccountAdded(account: Account) {
        thread{
            val hasAccountLikeThis = database.accountDao().getAccountByNumber(account.account_number)
            if(hasAccountLikeThis.isNotEmpty())//már van ilyen számlaszáma
                runOnUiThread {
                    Toast.makeText(this@AccountsActivity, "Hiba: Ilyen számlaszámot már fevettél korábban!", Toast.LENGTH_LONG).show()
                }
            else{
                val insertId = database.accountDao().insert(account)
                account.id = insertId
                runOnUiThread {
                    accountAdapter.addAccount(account)
                }
            }
        }
    }
}