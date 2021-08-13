package com.devs.carpoolrouteplanner

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.* //////////////////
import kotlinx.coroutines.runBlocking
import java.util.*
import com.devs.carpoolrouteplanner.AccountSignIn.Companion.creds
import com.devs.carpoolrouteplanner.utils.NoticeDialogFragment


class MainMenu : AppCompatActivity(),// FragmentActivity(),
    NoticeDialogFragment.NoticeDialogListener {

    val DEFAULT_UPDATE_INTERVAL: Long = 10
    val FAST_UPDATE_INTERVAL: Long = 5

    val PERMISSIONS_FINE_LOCATION = 69

    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback

    val my_url = "http://192.168.1.4:8080/"


    // username and password from companion object
    val userN = creds[0]
    val passW = creds[1]

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
        val btn_RegDest: Button = findViewById(R.id.btn_RegDest)
        val btn_PriorityDest: Button = findViewById(R.id.btn_PriorityDest)
        val btn_ViewCurrentMembers: Button = findViewById(R.id.ViewCurrentMembers)

        val intent1 = Intent(this@MainMenu, JoinGroup::class.java)
        val intent2 = Intent(this@MainMenu, CreateGroup::class.java)
        val intent3 = Intent(this@MainMenu, GroupManagementType::class.java)
        val intent4 = Intent(this@MainMenu, MainMenuLogOut::class.java)
        val intent5 = Intent(this@MainMenu, SetRoute::class.java)
        val intent6 = Intent(this@MainMenu, ViewCurrentMembers::class.java)

        // sets the update intervals for the location requests
        locationRequest.interval = 1000 * DEFAULT_UPDATE_INTERVAL
        locationRequest.fastestInterval = 1000 * FAST_UPDATE_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY // sets the location request to use the GPS

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                // loop "cycles" through all the newly generated locations
                for (location in locationResult.locations) {
                    lifecycleScope.launch {

                        // authenticates user
                        val client = HttpClient(CIO) {
                            install(Auth) {
                                basic {
                                    credentials {
                                        BasicAuthCredentials(username = userN, password = passW)
                                    }
                                }
                            }
                        }

                        // sends location to backend
                        val response: HttpResponse = client.post(my_url + "set_my_pickup_location_by_text") {
                            body = location.latitude.toString() + "," + location.longitude.toString()
                        }
                    }
                    // turns off location updates after 1st location is generated (thus ended the loop)
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                }
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

        // pulls user's currnet GPS location and sends it to the backend
        button5.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
            }
            else if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_FINE_LOCATION)
            }
        }

        button6.setOnClickListener {
            var destinationString : String = "";
            //lifecycleScope.launch {
            //showNoticeDialog()
            runBlocking {
                launch {
                    val client = HttpClient(CIO) {
                        install(Auth) {
                            basic {
                                credentials {
                                    BasicAuthCredentials(username = userN, password = passW)
                                }
                            }
                        }
                        install(JsonFeature) {
                            serializer = KotlinxSerializer()
                        }
                    }

                    val httpResponse: List<Int> = client.get(my_url + "list_my_groups/")
                    //val stringBody: String = httpResponse.receive()
                    destinationString += client.get<String>(my_url + "get_group_routes/${httpResponse.first()}").toString()

                    client.close()

                    //tv.text = byteArrayBody.decodeToString() //# if you want the response decoded to a string
                }
            }
            //18.520561,73.872435
            //gmap code here
            val gmmIntentUri =
                Uri.parse("https://www.google.com/maps/dir/?api=1&destination=18.518496,73.879259&travelmode=driving&waypoints=$destinationString")
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
            showNoticeDialog()
/**
            val gmmIntentUri =
                Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
**/

        }
        button7.setOnClickListener {
            startActivity(intent5)
        }

        btn_RegDest.setOnClickListener {
            lifecycleScope.launch { SetRegDest(69.00, 96.00, false) }
        }

        btn_PriorityDest.setOnClickListener {
            lifecycleScope.launch { SetRegDest(69.00, 96.00, true) }
        }

        btn_ViewCurrentMembers.setOnClickListener {
            startActivity(intent6)
        }
    }


    suspend fun SetRegDest(lat: Double, long: Double, isPriority: Boolean){
        val client = HttpClient(CIO) {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(username = userN, password = passW)
                    }
                }
            }
        }

        val response: HttpResponse = client.post(my_url + "submit_location") {
            body = "456,$lat,$long,$isPriority,"
        }
    }

    fun showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        val dialog = NoticeDialogFragment()
        dialog.show(supportFragmentManager, "NoticeDialogFragment")
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    override fun onDialogPositiveClick(dialog: DialogFragment) { // SAVE
        // User touched the dialog's positive button
        //Toast.makeText(this@MainMenu, "positive button pressed", Toast.LENGTH_LONG).show()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) { // DELETE
        // User touched the dialog's negative button
        //Toast.makeText(this@MainMenu, "negative button pressed", Toast.LENGTH_LONG).show()
        lifecycleScope.launch {

            // authenticates user
            val client = HttpClient(CIO) {
                install(Auth) {
                    basic {
                        credentials {
                            BasicAuthCredentials(username = userN, password = passW)
                        }
                    }
                }
            }

            // sends location to backend
            val response: HttpResponse = client.post(my_url + "delete_group") {
                body = "505"
            }
            client.close()
        }
    }
}