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
    }
}