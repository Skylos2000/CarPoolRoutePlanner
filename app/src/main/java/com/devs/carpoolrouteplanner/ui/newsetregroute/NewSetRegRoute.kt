package com.devs.carpoolrouteplanner.ui.newsetregroute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.AccountSignIn
import com.devs.carpoolrouteplanner.R
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch


class NewSetRegRoute : Fragment(){
    //val my_url = getConfigValue("backend_url")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.activity_set_group_dest, container, false)
        //val my_url = getConfigValue("backend_url")
        val url = "http://10.45.228.103:3306/"
        // username and password from companion object
        var poi = root.findViewById(R.id.POI) as EditText
        var gid = root.findViewById(R.id.GID) as EditText
        val userN = AccountSignIn.creds[0]
        val passW = AccountSignIn.creds[1]
        val button: Button = root.findViewById(R.id.button1)
        var progressBar = root.findViewById(R.id.progressBar) as ProgressBar
        progressBar.visibility = View.GONE

        //var button = root.findViewById(R.id.startRoute) as Button
        button.setOnClickListener {
            val poi = poi.getText()
            val gid = gid.getText()
            val (lat,long) = poi.split(",")
            if (!poi.toString().equals("") && !gid.toString().equals("")) {
                progressBar.visibility = View.VISIBLE
                button.isClickable = false
                lifecycleScope.launch {
                    val client = HttpClient(CIO) {
                        install(Auth) {
                            basic {
                                credentials {
                                    BasicAuthCredentials(username = userN, password = passW)
                                }
                            }
                        }
                    }
                    try {
                        val response: HttpResponse = client.get("$url/set_group_destination/$gid"){
                            parameter("newLat",lat)
                            parameter("newLong",long)
                            parameter("label","FinalDestination")
                        }
                        if (response.status.value == 404) {
                            Toast.makeText(activity?.applicationContext, "404 not found", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(activity?.applicationContext, "Success!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e: Exception) {
                        Toast.makeText(
                            activity?.applicationContext,
                            "Invalid Input",
                            Toast.LENGTH_LONG
                        ).show()
                    }//if (success) {
                    //startActivity(intent)
                    //finish()
                    progressBar.visibility = View.GONE
                    button.isClickable = true
                }

            } else {
                Toast.makeText(activity?.applicationContext, "One or more fields are empty", Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }
}
