package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.utils.ApiService
import com.devs.carpoolrouteplanner.utils.getConfigValue
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch

class SetRoute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_route)
        var poi = findViewById(R.id.POI) as EditText
        var gid = findViewById(R.id.GID) as EditText
        val button: Button = findViewById(R.id.button1)
        val intent = Intent(this@SetRoute, MainMenu::class.java)
        var progressBar = findViewById(R.id.progressBar) as ProgressBar
        progressBar.visibility = View.GONE
        button.setOnClickListener {
            val poi = poi.getText()
            val gid = gid.getText()
            val (lat,long) = poi.split(",")
            val url = getConfigValue("backend_url")
            if (!poi.toString().equals("") && !gid.toString().equals("")) {
                progressBar.visibility = View.VISIBLE
                button.isClickable = false
                lifecycleScope.launch {
                    val client = HttpClient(CIO) {
                        install(Auth) {
                            basic {
                                credentials {
                                    BasicAuthCredentials(username = "ttt", password = "ttt")
                                }
                            }
                        }
                    }
                    try {
                        val response: HttpResponse = client.get("$url/set_group_destination/$gid"){
                            parameter("newLat",lat)
                            parameter("newLong",long)
                        }
                        if (response.status.value == 404) {
                            Toast.makeText(this@SetRoute, "404 not found", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(this@SetRoute, "Success!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e: Exception) {
                        Toast.makeText(
                            this@SetRoute,
                            "Invalid Input",
                            Toast.LENGTH_LONG
                        ).show()
                    }//if (success) {
                    startActivity(intent)
                    finish()
                    progressBar.visibility = View.GONE
                    button.isClickable = true
                }

            } else {
                Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

