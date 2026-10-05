package com.example.movieexplorer.ui.splash

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.movieexplorer.MainActivity
import com.example.movieexplorer.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkInternetAndContinue()

        binding.btnRetry.setOnClickListener {
            checkInternetAndContinue()
        }
    }

    private fun checkInternetAndContinue() {

        if (isInternetAvailable()) {

            // Show loading
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.tvInternetError.visibility = android.view.View.GONE
            binding.btnRetry.visibility = android.view.View.GONE

            // Wait 2 seconds for splash screen
            handler.postDelayed({

                val intent = android.content.Intent(
                    this,
                    MainActivity::class.java
                )

                startActivity(intent)
                finish()

            }, 2000)

        } else {

            // Hide loading
            binding.progressBar.visibility = android.view.View.GONE

            // Show error
            binding.tvInternetError.visibility = android.view.View.VISIBLE
            binding.btnRetry.visibility = android.view.View.VISIBLE
        }
    }

    private fun isInternetAvailable(): Boolean {

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
            ?: return false

        val capabilities =
            connectivityManager.getNetworkCapabilities(network)
                ?: return false

        return capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        ) &&
                capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}