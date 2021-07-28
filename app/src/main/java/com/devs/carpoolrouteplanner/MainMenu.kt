package com.devs.carpoolrouteplanner

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import com.google.android.gms.location.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext


class MainMenu : AppCompatActivity() {

    val DEFAULT_UPDATE_INTERVAL: Long = 5
    val FAST_UPDATE_INTERVAL: Long = 5

    val PERMISSIONS_FINE_LOCATION = 69

    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainmenu)

        locationRequest = LocationRequest.create()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)



        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.startRoute)
        val button7: Button = findViewById(R.id.setRoute)

        val intent1 = Intent(this@MainMenu, JoinGroup::class.java)
        val intent2 = Intent(this@MainMenu, CreateGroup::class.java)
        val intent3 = Intent(this@MainMenu, GroupManagementType::class.java)
        val intent4 = Intent(this@MainMenu, MainMenuLogOut::class.java)
        val intent5 = Intent(this@MainMenu, SetRoute::class.java)

        // set preferences for locationRequest
        locationRequest.interval = 1000 * DEFAULT_UPDATE_INTERVAL
        locationRequest.fastestInterval = 1000 * FAST_UPDATE_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY // determines how location obtained (by default i think)



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
            UpdateGPS()

        }

        button6.setOnClickListener {
            //gmap code here
            val gmmIntentUri =
                Uri.parse("https://www.google.com/maps/dir/?api=1&destination=shreveport,la&travelmode=driving&waypoints=monroe,la|louisana+tech")
            val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            intent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                try {
                    val unrestrictedIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    startActivity(unrestrictedIntent)
                } catch (innerEx: ActivityNotFoundException) {
                    Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG)
                        .show()
                }
            }

        }
        button7.setOnClickListener {
            startActivity(intent5)
        }
    }

    // grabs the last known location from the phone's "location cache"
    fun UpdateGPS(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Toast.makeText(this, location.latitude.toString() + ", " + location.longitude.toString(), Toast.LENGTH_SHORT).show()

                    lifecycleScope.launch {
                        val client = HttpClient(CIO) {
                            install(Auth) {
                                basic {
                                    credentials {
                                        BasicAuthCredentials(username = "aaa", password = "eee")
                                    }
                                }
                            }
                        }
                        client.post<String>("http://10.0.0.53:8080/set_my_pickup_location_by_text") {
                            //contentType(ContentType.Text.Plain)
                            body = location.latitude.toString() + "," + location.longitude.toString()
                        }
                    }
                }
            }
        }
        else {
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_FINE_LOCATION)
            }
        }
    }
}