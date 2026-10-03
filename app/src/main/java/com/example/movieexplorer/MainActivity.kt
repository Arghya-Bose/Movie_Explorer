package com.example.movieexplorer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.movieexplorer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //status bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.topAppBar) { view, insets ->

            val statusBarHeight =
                insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                statusBarHeight,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }

        val navHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Bottom navigation
        binding.bottomNavigation.setupWithNavController(navController)
        binding.topAppBar.setOnMenuItemClickListener { item ->

            when (item.itemId) {

                R.id.action_search -> {

                    if (navController.currentDestination?.id != R.id.searchFragment) {
                        navController.navigate(R.id.searchFragment)
                    }
                    true
                }else -> false
            }
        }

        // Show / hide top bar
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.homeFragment -> {
                    binding.topAppBar.visibility = View.VISIBLE
                }
                R.id.searchFragment,
                R.id.favouritesFragment -> {
                    binding.topAppBar.visibility = View.GONE
                }
                else -> {
                    binding.topAppBar.visibility = View.VISIBLE
                }
            }
        }
    }
}