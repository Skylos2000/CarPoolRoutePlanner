package com.devs.carpoolrouteplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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

class CreateGroup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.creategroup)

        val createGroupButton: Button = findViewById(R.id.createGroupButton)
        val myUrl = getConfigValue("backend_url")

        createGroupButton.setOnClickListener {
            createGroupButton.isClickable = false
            lifecycleScope.launch {
                val response: HttpResponse = httpClient.post("$myUrl/groups/create") {}
            }
            finish()
            createGroupButton.isClickable = true
        }
    }
}