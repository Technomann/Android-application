package com.example.mobwebhf.display

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobwebhf.adapter.AccountAdapter
import com.example.mobwebhf.adapter.TransactionAdapter
import com.example.mobwebhf.data.Account
import com.example.mobwebhf.data.Transaction
import com.example.mobwebhf.data.TransactionDatabase
import com.example.mobwebhf.databinding.ActivityTransactionBinding
import com.example.mobwebhf.display.fragments.AddTransactionDialogFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class TransactionActivity : AppCompatActivity(),
    AddTransactionDialogFragment.AddTransactionDialogListener {
    private lateinit var binding: ActivityTransactionBinding
    private var accountNumber: String? = null
    private var accountId: Int? = null
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var database: TransactionDatabase

    companion object {
        private const val TAG = "TransactionActivity"
        const val EXTRA_ACCOUNT_NUMBER = "extra.account_number"
        const val EXTRA_ACCOUNT_ID = "extra.account_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accountNumber = intent.getStringExtra(EXTRA_ACCOUNT_NUMBER)
        accountId = intent.getIntExtra(EXTRA_ACCOUNT_ID, -1)
        database =
            TransactionDatabase.getDatabase(applicationContext) //nem mondanám túl hatékonynak, de jobb talán, mint a Serializable

        supportActionBar?.title = accountNumber
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initFab()
        initRecycleView()
    }

    private fun initFab() {
        binding.fabNewTransaction.setOnClickListener {
            AddTransactionDialogFragment().show(
                supportFragmentManager,
                AddTransactionDialogFragment::class.java.simpleName
            )
        }
    }

    private fun initRecycleView() {
        transactionAdapter = TransactionAdapter()
        binding.rvTransactions.layoutManager = LinearLayoutManager(this)
        binding.rvTransactions.adapter = transactionAdapter
        loadItemsBackground()
    }

    private fun loadItemsBackground() {
        thread {
            val items = database.transactionDao().getAllByAccountId(accountId.toString().toLong())
            runOnUiThread {
                transactionAdapter.update(items)
            }
        }
    }

    override fun onTransactionAdded(transaction: Transaction?) {
        thread {
            if (!transaction!!.income) {
                val accounts = database.accountDao().getAccountById(accountId!!.toLong())
                if (accounts.isNotEmpty()) {//Lekérjük a számlát
                    val account: Account = accounts[0]

                    if (account.limitId != null) {//Ha számlához van limit
                        val limit = database.limitDao().getLimitById(account.limitId)//Lekérjük a limitet

                        if (limit.isNotEmpty()) {
                            if(transaction.amount <= account.value) {//Ha van elég fedezet
                                if (transaction.amount < limit[0].amount) {//Ha a limit nagyobb, mint a kért tranzakció, mehet a dolog
                                    persistTransaction(transaction)
                                } else //Egyébként nem mehet
                                    runOnUiThread {
                                        Toast.makeText(
                                            this@TransactionActivity,
                                            "Hiba: A limited kisebb ennél!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }else{
                                runOnUiThread {
                                    Toast.makeText(
                                        this@TransactionActivity,
                                        "Hiba: Nincs elég fedezeted ezen a számlán!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    } else {
                        persistTransaction(transaction)
                    }
                }
            }else{
                persistTransaction(transaction)
            }
        }
    }

    private fun persistTransaction(transaction: Transaction?){
        transaction!!.accountId = accountId!!.toLong()

        val insertId = database.transactionDao().insert(transaction)
        transaction.id = insertId

        updateAccountWithNewTransaction(transaction)
        runOnUiThread {
            transactionAdapter.addItem(transaction)
        }
    }

    private fun updateAccountWithNewTransaction(transaction: Transaction?){
        thread{
            val accounts = database.accountDao().getAccountById(accountId!!.toLong())
            if(accounts.isNotEmpty()){
                val account = accounts[0]
                if(transaction!!.income)
                   account.value += transaction.amount
                else
                    account.value -= transaction.amount

                database.accountDao().update(account)
            }
        }
    }
}