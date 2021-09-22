package com.devs.carpoolrouteplanner

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.utils.getConfigValue
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Solomon: AppCompatActivity() {

    lateinit var groups: String
    //val my_url = getConfigValue("backend_url")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.solomon)
        val my_url = getConfigValue("backend_url")

        runBlocking {
            val client = HttpClient(CIO) {
                install(Auth) {
                    basic {
                        credentials {
                            BasicAuthCredentials(username = "aaa", password = "eee")
                        }
                    }
                }
            }

            val response: String = client.get(my_url + "list_my_groups/") {}
            groups = response
        }
        groups = groups.replace("[", "")
        groups = groups.replace("]", "")
        //groups.drop(1)
        //groups.dropLast(1)
        println(groups)
        val groupsList: List<String> = groups.split(",")

        val lvGroups: ListView = findViewById(R.id.groupList)
        val listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, groupsList)
        lvGroups.adapter = listAdapter

        val intentGroupVote = Intent(this@Solomon, SolomonGroupVote::class.java)


        lvGroups.setOnItemClickListener { adapterView, view, i, l ->
            intentGroupVote.putExtra("group", groupsList[i].toInt())
            startActivity(intentGroupVote)
            //Toast.makeText(this, "you clicked group " + groups[i], Toast.LENGTH_SHORT).show()
        }
    }
}