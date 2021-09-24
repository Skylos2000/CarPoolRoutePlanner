package com.devs.carpoolrouteplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.utils.getConfigValue
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch

class CreateGroup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.creategroup)

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

                val response: HttpResponse = client.post(my_url + "create_group/") {
                }
            }
                    finish()
                    button.isClickable = true
                }
    }
}