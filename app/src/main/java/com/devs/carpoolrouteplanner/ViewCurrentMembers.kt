package com.devs.carpoolrouteplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.utils.httpClient
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class User(val userId: Int, val email: String, val username: String, val groupIds: List<Int>)

class ViewCurrentMembers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewcurrentmembers)

        val button: Button = findViewById(R.id.button)
        val my_url = getConfigValue("backend_url")

        button.setOnClickListener {
            button.isClickable = false
            lifecycleScope.launch {
//                val client = HttpClient(CIO) {
//                    install(Auth) {
//                        basic {
//                            credentials {
//                                BasicAuthCredentials(username = "aaa", password = "eee")
//                            }
//                        }
//                    }
//                }

                // this route must not exist anymore..
                val response: String = httpClient.get(my_url + "list_my_groups/"){
                }
                val data = response

//                val response = httpClient.get<User>(my_url + "/users/me"){
//                }
//                response.groupIds

                Toast.makeText(this@ViewCurrentMembers, data, LENGTH_LONG).show()
            }
            //finish()
            button.isClickable = true
        }
    }
}