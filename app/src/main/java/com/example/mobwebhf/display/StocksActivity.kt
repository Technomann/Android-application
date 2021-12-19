package com.example.mobwebhf.display

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobwebhf.adapter.StockAdapter
import com.example.mobwebhf.data.StockItem
import com.example.mobwebhf.data.api.StockData
import com.example.mobwebhf.data.api.StockDataHolder
import com.example.mobwebhf.databinding.ActivityStocksBinding
import com.example.mobwebhf.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

class StocksActivity : AppCompatActivity(), StockDataHolder {
    private lateinit var binding: ActivityStocksBinding
    private var stockData: StockData? = null
    private lateinit var stockAdapter: StockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStocksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
    }

    private fun initRecyclerView(){
        stockAdapter = StockAdapter()
        binding.rvStocks.layoutManager = LinearLayoutManager(this)
        binding.rvStocks.adapter = stockAdapter
        assignStockData(null, false)
        loadStockData()
    }

    private fun loadStockData(){
        NetworkManager.getStock("HUF")?.enqueue(object : Callback<StockData?> {
            override fun onResponse(
                call: Call<StockData?>,
                response: Response<StockData?>
            ) {
                Log.e("Api hívás", "onResponse: " + response.code())
                if (response.isSuccessful) {
                    assignStockData(response.body(), true)
                } else {
                    Toast.makeText(this@StocksActivity, "Hiba: " + response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(
                call: Call<StockData?>,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                Toast.makeText(this@StocksActivity, "Network request error occured, check LOG", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun assignStockData(receivedStockData: StockData?, ready: Boolean){
        val currencies = arrayOf("EUR", "GBP", "USD", "RUB", "JPY", "NGN", "INR")
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        stockData = receivedStockData
        val items = mutableListOf<StockItem>()

        for(i in 0 until currencies.size-1) {
            if(!ready){
                items.add(StockItem(
                    name = "LOADING",
                    value = -1.0,
                    date = time,
                    base = "HUF"
                ))
            }else {
                items.add(
                    StockItem(
                        name = currencies[i],
                        value = ((1.0 / stockData!!.data[currencies[i]]!!) * 100.0).roundToInt() / 100.0,//Azt kapjuk meg, hogy 1 HUF hány "VALAMI", majd kerekítünk 2 tizedesre
                        date = time,
                        base = "HUF" //Beégetetten csak forintra vált
                    )
                )
            }
        }
        stockAdapter.update(items)
    }

    override fun getStockData(): StockData? {
        return stockData
    }
}