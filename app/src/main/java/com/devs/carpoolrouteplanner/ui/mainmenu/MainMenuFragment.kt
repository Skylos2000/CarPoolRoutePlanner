package com.devs.carpoolrouteplanner.ui.mainmenu

import android.widget.TextView
import androidx.lifecycle.Observer
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.*
import com.devs.carpoolrouteplanner.R
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
import com.devs.carpoolrouteplanner.ui.MainActivity
import com.devs.carpoolrouteplanner.utils.NoticeDialogFragment
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.utils.httpClient
import com.devs.carpoolrouteplanner.databinding.FragmentMainmenuBinding // fragment_mainmenu.xml ~= "Fragment"+"mainmenu"+"Binding"


class MainMenuFragment : Fragment() {
    private lateinit var mainMenuViewModel: MainMenuViewModel
    private var _binding: FragmentMainmenuBinding? = null // just naming it
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainMenuViewModel = ViewModelProvider(this).get(MainMenuViewModel::class.java)
        _binding = FragmentMainmenuBinding.inflate(inflater,container,false)
        val root: View = binding.root

        val textView: TextView = binding.textMainMenu
        mainMenuViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


























   /* NoticeDialogFragment.NoticeDialogListener {

    val DEFAULT_UPDATE_INTERVAL: Long = 10
    val FAST_UPDATE_INTERVAL: Long = 5

    val PERMISSIONS_FINE_LOCATION = 69

    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback

    //val backendUrl = getConfigValue("backend_url")
    val backendUrl = "http://138.47.132.58:8080"

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


        val solo: Button = findViewById(R.id.solomon)
        val solomonIntent = Intent(this@MainMenu, Solomon::class.java)
        solo.setOnClickListener {
            startActivity(solomonIntent)
        }

        val pre: Button = findViewById(R.id.preview)
        val preview = Intent(this@MainMenu, MainActivity::class.java)
        pre.setOnClickListener {
            startActivity(preview)
        }

        // sets the update intervals for the location requests
        locationRequest.interval = 1000 * DEFAULT_UPDATE_INTERVAL
        locationRequest.fastestInterval = 1000 * FAST_UPDATE_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY // sets the location request to use the GPS

        // TODO: Is this even needed?
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                // loop "cycles" through all the newly generated locations
                for (location in locationResult.locations) {
                    lifecycleScope.launch {

                        // sends location to backend
                        httpClient.post("$backendUrl/set_my_pickup_location_by_text") {
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

        // pulls user's current GPS location and sends it to the backend
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
                    val httpResponse: List<Int> = httpClient.get("$backendUrl/list_my_groups/") // i have no idea what this routes to now
                    //val stringBody: String = httpResponse.receive()
                    destinationString = httpClient.get<List<Pair<Double,Double>>>(backendUrl + "/get_group_routes/${httpResponse.first()}").joinToString("|"){ "${it.first},${it.second}" }

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
            lifecycleScope.launch { setRegDest(69.00, 96.00, false) }
        }

        btn_PriorityDest.setOnClickListener {
            lifecycleScope.launch { setRegDest(69.00, 96.00, true) }
        }

        btn_ViewCurrentMembers.setOnClickListener {
            startActivity(intent6)
        }
    }


    suspend fun setRegDest(lat: Double, long: Double, isPriority: Boolean){
        httpClient.post<HttpResponse>("$backendUrl/submit_location") { // cant find the route for this
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
            // sends location to backend
            httpClient.post<HttpResponse>("$backendUrl/delete_group") {
                body = "505"
            }
        }
    }
}
    */