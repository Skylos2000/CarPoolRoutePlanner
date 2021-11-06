package com.devs.carpoolrouteplanner.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.devs.carpoolrouteplanner.CreateAccount
import com.devs.carpoolrouteplanner.CreateGroup
import com.devs.carpoolrouteplanner.JoinGroup
import com.devs.carpoolrouteplanner.R
import com.devs.carpoolrouteplanner.databinding.ActivityMainBinding
import com.facebook.login.LoginManager
import java.lang.System.exit

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            /*R.id.mainmenu, R.id.creategroup, R.id.joingroup,*/ R.id.nav_home,R.id.nav_menu,R.id.nav_view_route,R.id.nav_member_manage,R.id.nav_qr_invite), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.homeoptions, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_create -> startActivity(Intent(this, CreateGroup::class.java))
            R.id.action_join -> startActivity(Intent(this, JoinGroup::class.java))
            R.id.action_manage -> Toast.makeText(this,"Manage Selected",Toast.LENGTH_SHORT).show()
            R.id.action_settings -> Toast.makeText(this,"Settings Selected",Toast.LENGTH_SHORT).show()
            R.id.action_logout -> {
                LoginManager.getInstance().logOut()
                AccountSignIn.creds[0]=""
                AccountSignIn.creds[1]=""
                startActivity(Intent(this, AccountSignIn::class.java))}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
