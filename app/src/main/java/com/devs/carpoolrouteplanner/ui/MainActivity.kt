package com.devs.carpoolrouteplanner.ui

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.devs.carpoolrouteplanner.JoinGroup
import com.devs.carpoolrouteplanner.MainMenu
import com.devs.carpoolrouteplanner.R
import com.devs.carpoolrouteplanner.databinding.ActivityMainBinding
import com.devs.carpoolrouteplanner.ui.home.HomeFragment
import com.devs.carpoolrouteplanner.ui.home.HomeViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        //setSupportActionBar(toolbar)
        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout= binding.drawerLayout
        navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.mainmenu, R.id.creategroup, R.id.joingroup, R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val intent1 = Intent(this, MainMenu::class.java)

        //Toast.makeText(this, "can anybody see this??", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mainmenu -> {
                print("\n CAN ANYBODY SEE THIS ??????????? \n")
                Toast.makeText(this, "main menu button pressed", Toast.LENGTH_SHORT).show()
            }
            R.id.creategroup -> {
                print("\n CAN ANYBODY SEE THIS ??????????? \n")
                Toast.makeText(this, "main menu button pressed", Toast.LENGTH_SHORT).show()
            }
            R.id.joingroup -> {
                print("\n CAN ANYBODY SEE THIS ??????????? \n")
                Toast.makeText(this, "main menu button pressed", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_home -> {
                print("\n CAN ANYBODY SEE THIS ??????????? \n")
                Toast.makeText(this, "main menu button pressed", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
