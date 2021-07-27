package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.pm.PackageManager
import android.location.Location
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class MainMenu : AppCompatActivity() {
    val DEFAULT_UPDATE_INTERVAL: Long = 5
    val FAST_UPDATE_INTERVAL: Long = 5

    val PERMISSIONS_FINE_LOCATION = 69

    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback

    lateinit var btn_location: Button
    lateinit var tv_GPS: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainmenu)

        locationRequest = LocationRequest.create()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        btn_location = findViewById(R.id.button5)
        tv_GPS = findViewById(R.id.tv_GPS)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.startRoute)

        val intent1 = Intent(this@MainMenu, JoinGroup::class.java)
        val intent2 = Intent(this@MainMenu, CreateGroup::class.java)
        val intent3 = Intent(this@MainMenu, GroupManagementType::class.java)
        val intent4 = Intent(this@MainMenu, MainMenuLogOut::class.java)

        // set preferences for locationRequest
        locationRequest.interval = 1000 * DEFAULT_UPDATE_INTERVAL
        locationRequest.fastestInterval = 1000 * FAST_UPDATE_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY // determines how location obtained (by default i think)

        locationCallback = object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
            }
        }

        button1.setOnClickListener {
            startActivity(intent1)
        }

        button2.setOnClickListener {
            startActivity(intent2)
        }

        button3.setOnClickListener {
            startActivity(intent3)
        }

        button4.setOnClickListener {
            startActivity(intent4)
        }

        button5.setOnClickListener {
            //gmap code here
        }

        btn_location.setOnClickListener {
            UpdateGPS()
        }
    }

    // grabs the last known location from the phone's "location cache"
    fun UpdateGPS(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener{ location: Location? -> if(location != null){
            tv_GPS.text = location.latitude.toString() + ", " +location.longitude.toString()}
            }
        }

        else {
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_FINE_LOCATION)
            }
        }
    }
}