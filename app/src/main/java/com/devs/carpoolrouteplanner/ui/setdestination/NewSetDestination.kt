package com.devs.carpoolrouteplanner.ui.startroute

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.devs.carpoolrouteplanner.AccountSignIn
import com.devs.carpoolrouteplanner.R
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class NewSetDestination : Fragment(){
    //val my_url = getConfigValue("backend_url")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.activity_set_route, container, false)
        //val my_url = getConfigValue("backend_url")
        val my_url = "http://10.45.228.103:3306/"
        // username and password from companion object
        val userN = AccountSignIn.creds[0]
        val passW = AccountSignIn.creds[1]


        var button = root.findViewById(R.id.startRoute) as Button
        button.setOnClickListener {
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
                    destinationString = client.get<List<Pair<Double,Double>>>(my_url + "get_group_routes/${httpResponse.first()}").joinToString("|"){ "${it.first},${it.second}" }

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
                    Toast.makeText(activity?.applicationContext, "Please install a maps application", Toast.LENGTH_LONG)
                        .show()
                }
            }

            /**
             *
             * showNoticeDialog()
            val gmmIntentUri =
            Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
             **/

        }
        return root
    }
}
