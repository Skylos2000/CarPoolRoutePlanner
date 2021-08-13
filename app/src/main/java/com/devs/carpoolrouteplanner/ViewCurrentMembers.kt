package com.devs.carpoolrouteplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.adapters.MemberListAdapter
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.viewmodals.LoginViewModal
import com.devs.carpoolrouteplanner.viewmodals.ViewCurrentMembersViewModal
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch

class ViewCurrentMembers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewcurrentmembers)

        val button: Button = findViewById(R.id.button)
        val my_url = getConfigValue("backend_url")

        button.setOnClickListener {
            button.isClickable = false
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

                val response: String = client.get(my_url + "list_my_groups/"){
                }
                val data = response
                Toast.makeText(this@ViewCurrentMembers, data, LENGTH_LONG).show()
            }
            finish()
            button.isClickable = true
        }
    }
}