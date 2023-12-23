package com.lokal.cryptocurrencyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lokal.cryptocurrencyapp.adapter.CurrencyAdapter
import com.lokal.cryptocurrencyapp.api.CryptoApiService
import com.lokal.cryptocurrencyapp.api.RetrofitHelper
import com.lokal.cryptocurrencyapp.databinding.ActivityMainBinding
import com.lokal.cryptocurrencyapp.model.Currency
import com.lokal.cryptocurrencyapp.model.CurrencyListResponse
import com.lokal.cryptocurrencyapp.model.LiveRatesResponse
import com.lokal.cryptocurrencyapp.model.NetworkModel
import com.lokal.cryptocurrencyapp.repository.CurrencyRepository
import com.lokal.cryptocurrencyapp.viewmodel.MainViewModel
import com.lokal.cryptocurrencyapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.GlobalScope
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var requiredDataList: MutableList<Currency>
    private lateinit var currencyAdapter: CurrencyAdapter
    lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var lastRefreshTimeTextView: TextView
    private lateinit var binding: ActivityMainBinding
    private lateinit var networkModel: NetworkModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        lastRefreshTimeTextView = binding.lastRefreshTextView
        networkModel= NetworkModel()

        val cryptoApi = RetrofitHelper().getInstance().create(CryptoApiService::class.java)
        val repository = CurrencyRepository(cryptoApi)

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        mainViewModel.liveRate.observe(this, Observer {
            if (it != null) {
                Log.d("TAG", it.rates.toString())
            } else {
                Log.e("TAG", "Error fetching live rates: ${it?.rates.toString()}")
                showToast("Error fetching live rates")
            }
        })

        mainViewModel.cryptoCurrency.observe(this, Observer {
            if (it != null) {
                val cryptoData = it.crypto
                val rates = mainViewModel.liveRate.value?.rates
                if (cryptoData != null && rates != null) {
                    requiredDataList = mutableListOf<Currency>()
                    for ((key, value) in cryptoData) {
                        val exchangeRate = rates[key] ?: 0.0
                        requiredDataList.add(
                            Currency(
                                key,
                                value.name,
                                value.icon_url,
                                exchangeRate
                            )
                        )
                    }

                    Log.d("TAG", requiredDataList.toString())
                    // Update the adapter when the data is ready
                    currencyAdapter.updateData(requiredDataList)
                } else {
                    Log.e("TAG", "Crypto data or rates are null")
                    showToast("Error: Crypto data or rates are null")
                }
            } else {
                showToast("Error fetching currencies")
            }
        })

        mainViewModel.dataFetchComplete.observe(this, Observer {
            // Data fetch complete, stop refreshing
            stopRefreshing()
            updateLastRefreshTime()
        })

        initRecyclerView()
        setupAutoRefresh()
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView() {
        swipeRefreshLayout = binding.swipeRefreshLayout
        val recyclerView: RecyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)

        currencyAdapter = CurrencyAdapter(emptyList())
        recyclerView.adapter = currencyAdapter

        // Set up swipe-to-refresh
        swipeRefreshLayout.setOnRefreshListener {
            // Trigger refresh when the user swipes
            if(networkModel.isNetworkAvailable(this)){
                mainViewModel.fetchData()
            }
        }

        // Trigger initial data fetch
        if(networkModel.isNetworkAvailable(this)){
            mainViewModel.fetchData()
        }
    }

    private fun setupAutoRefresh() {
        // Set up auto-refresh every 3 minutes
        val handler = Handler(Looper.getMainLooper())
        val delayMillis: Long = 3 * 60 * 1000 // 3 minutes

        handler.postDelayed(object : Runnable {
            override fun run() {
                // Trigger refresh
                if(networkModel.isNetworkAvailable(applicationContext)){
                    mainViewModel.fetchData()
                }
                handler.postDelayed(this, delayMillis)
            }
        }, delayMillis)
    }

    // Function to stop refreshing layout
    private fun stopRefreshing() {
        swipeRefreshLayout.isRefreshing = false
    }

    private fun updateLastRefreshTime() {
        val currentTime = System.currentTimeMillis()
        val lastRefreshTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(currentTime)
        lastRefreshTimeTextView.text = "Last Refresh: $lastRefreshTime"
    }
}



