package com.devs.carpoolrouteplanner

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.utils.httpClient
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking

class SolomonGroupVote: AppCompatActivity() {

    lateinit var locationOptions: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.solomon_group_vote)

        val my_url = getConfigValue("backend_url")

        val b = intent.extras
        val gid = b?.getInt("group")

        // submit location
        // cast vote
        // if group leader: start vote button, get result button
        var btnStartVote: Button = findViewById(R.id.btnStartVote)
        var btnEndVote: Button = findViewById(R.id.btnEndVote)
        var btnSubmitLocation: Button = findViewById(R.id.btnSubmitLocation)
        var btnRefresh: Button = findViewById(R.id.btnRefresh)
        var txtEnterLocation: TextView = findViewById(R.id.txtEnterLocation)
        var lvVotingOptions: ListView = findViewById(R.id.lvVotingOptions)

        runBlocking {
            locationOptions = httpClient.post(my_url + "votingOptions") {
                body = gid.toString()
            }
        }

        if (locationOptions == "-1") {
            locationOptions = ""
        }
        locationOptions = locationOptions.replace("[", "")
        locationOptions = locationOptions.replace("]", "")
        locationOptions = locationOptions.replace(34.toChar().toString(), "")
        var locationOptionsList: List<String> = locationOptions.split(",")

        var listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationOptionsList)
        lvVotingOptions.adapter = listAdapter

        // -1: There is no active voting for this group
        // -2: Only the group leader can start/end a poll

        btnStartVote.setOnClickListener {
            runBlocking {
                val response: String = httpClient.post(my_url + "startVote") {
                    body = gid.toString()
                }
                if (response == "-2"){
                    Toast.makeText(this@SolomonGroupVote, "Only the group leader can start/end a poll", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnEndVote.setOnClickListener {
            runBlocking {
                val response: String = httpClient.post(my_url + "voteResult") {
                    body = gid.toString()
                }
                if (response == "-1"){
                    Toast.makeText(this@SolomonGroupVote, "There is no active voting for this group", Toast.LENGTH_SHORT).show()
                }
                if (response == "-2"){
                    Toast.makeText(this@SolomonGroupVote, "Only the group leader can end a poll", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnSubmitLocation.setOnClickListener {
            if (txtEnterLocation.text == ""){
                Toast.makeText(this@SolomonGroupVote, "Location field cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else {
                runBlocking {
                    val response: String = httpClient.post(my_url + "addVotingLocation") {
                        body = gid.toString() + "," + txtEnterLocation.text
                    }
                    if (response == "-1") {
                        Toast.makeText(this@SolomonGroupVote,
                            "Voting has not started",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // make new route for this in the backend
        btnRefresh.setOnClickListener {
            runBlocking {
                // authenticates user
                if (lvVotingOptions.isEnabled) {
                    locationOptions = httpClient.post(my_url + "votingOptions") {
                        body = gid.toString()
                    }
                }
                else{
                    locationOptions = httpClient.post(my_url + "votingScores") {
                        body = gid.toString()
                    }
                }
            }
            if (locationOptions == "-1"){
                Toast.makeText(this@SolomonGroupVote, "There is no active voting for this group", Toast.LENGTH_SHORT).show()
            }
            else {
                locationOptions = locationOptions.replace("[", "")
                locationOptions = locationOptions.replace("]", "")
                locationOptions = locationOptions.replace(34.toChar().toString(), "")
                locationOptionsList = locationOptions.split(",")

                listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationOptionsList)
                lvVotingOptions.adapter = listAdapter
            }
        }

        // fuuuuucckk, make a new route for this too
        lvVotingOptions.setOnItemClickListener { adapterView, view, i, l ->
            runBlocking {

                if(locationOptionsList[i] == "") { locationOptions = "-3" }
                else {

                    val response: HttpResponse = httpClient.post(my_url + "castVote") {
                        body = gid.toString() + "," + locationOptionsList[i]
                    }

                    locationOptions = httpClient.post(my_url + "votingScores") {
                        body = gid.toString()
                    }
                }
            }
            if (locationOptions == "-3"){
                Toast.makeText(this@SolomonGroupVote, "There are no voting locations, try refreshing", Toast.LENGTH_SHORT).show()
            }
            else {
                locationOptions = locationOptions.replace("[", "")
                locationOptions = locationOptions.replace("]", "")
                locationOptions = locationOptions.replace(34.toChar().toString(), "")
                locationOptionsList = locationOptions.split(",")

                listAdapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, locationOptionsList)
                lvVotingOptions.adapter = listAdapter

                lvVotingOptions.isClickable = false
                lvVotingOptions.isEnabled = false
            }
        }
    }
}