package com.devs.carpoolrouteplanner

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.utils.httpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch

class JoinGroup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.joingroup)

        val password: EditText = findViewById(R.id.password)
        val button: Button = findViewById(R.id.button)
        val intent = Intent(this@JoinGroup, GuestMenu::class.java)

        button.setOnClickListener{
            val code = password.text
            button.isClickable = false

            val username = this.getSharedPreferences("login_details",Context.MODE_PRIVATE).getString("username","")
            val password = this.getSharedPreferences("login_details",Context.MODE_PRIVATE).getString("password","")

            lifecycleScope.launch{
                val apiUrl = getConfigValue("backend_url")

                apiUrl?.let{
//                    val client = HttpClient(CIO)
//                    {
//                        install(Auth) {
//                            basic {
//                                credentials {
//                                    BasicAuthCredentials(username = username!!, password = password!!)
//                                }
//                            }
//                        }
//                    }
                    try {
                        //val response: HttpResponse = httpClient.get(apiUrl + "join_group/$code")
                        val response: HttpResponse = httpClient.post(apiUrl + "/groups/invites/join_group"){
                            body = code.toString()
                        }

                        val gid: String = response.receive()
                        button.isClickable = true

                        if(gid == "EMPTY"){
                            Toast.makeText(applicationContext, "No group with the given invite code", Toast.LENGTH_SHORT).show()
                        }else
                        {
                            Toast.makeText(applicationContext, "Group joined", Toast.LENGTH_SHORT).show()
                            intent.putExtra("GID",gid)
                            startActivity(intent)
                        }
                    } catch (e: Exception) {
                        button.isClickable = false

                        Toast.makeText(applicationContext, "No group with the given invite code", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }


    }
}