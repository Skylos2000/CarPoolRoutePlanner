package com.devs.carpoolrouteplanner.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.devs.carpoolrouteplanner.R
import com.devs.carpoolrouteplanner.databinding.ActivityMainGroupBinding
import kotlinx.serialization.descriptors.PrimitiveKind
import com.google.android.libraries.places.api.Places

import com.google.android.libraries.places.api.net.PlacesClient





class MainGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainGroupBinding
    //val gid = intent.extras?.getInt("groupId")
    var gid = 456
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gid = intent.extras?.getInt("groupId")!!

        binding = ActivityMainGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main_group)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
             R.id.navigation_group_manage_destinations, R.id.navigation_group_manage_members,R.id.navigation_group_voting))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val apiKey = getString(R.string.api_key)


        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

// Create a new Places client instance.

// Create a new Places client instance.
        val placesClient = Places.createClient(this)

    }
}