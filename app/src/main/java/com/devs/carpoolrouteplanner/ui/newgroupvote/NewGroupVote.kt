package com.devs.carpoolrouteplanner.ui.newgroupvote

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.devs.carpoolrouteplanner.AccountSignIn
import com.devs.carpoolrouteplanner.R
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking


class NewGroupVote : Fragment(){
    //val my_url = getConfigValue("backend_url")

    lateinit var locationOptions: String

    // username and password from companion object
    val userN = AccountSignIn.creds[0]
    val passW = AccountSignIn.creds[1]
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.solomon_group_vote, container, false)
/*
        val my_url = "http://10.45.228.103:3306/"
        //val my_url = (getConfigValue("backend_url"))

        //val b = intent.extras
        //val gid = b?.getInt("group")
        val gid = "12"

        // submit location
        // cast vote
        // if group leader: start vote button, get result button
        var btnStartVote: Button = root.findViewById(R.id.btnStartVote)
        var btnEndVote: Button = root.findViewById(R.id.btnEndVote)
        var btnSubmitLocation: Button = root.findViewById(R.id.btnSubmitLocation)
        var btnRefresh: Button = root.findViewById(R.id.btnRefresh)
        var txtEnterLocation: TextView = root.findViewById(R.id.txtEnterLocation)
        var lvVotingOptions: ListView = root.findViewById(R.id.lvVotingOptions)

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

            locationOptions = client.post(my_url + "votingOptions") {
                body = gid.toString()
            }
        }

        if (locationOptions == "-1"){
            locationOptions = ""
        }
        locationOptions = locationOptions.replace("[", "")
        locationOptions = locationOptions.replace("]", "")
        locationOptions = locationOptions.replace(34.toChar().toString(), "")
        var locationOptionsList: List<String> = locationOptions.split(",")

        var listAdapter = (ListView)(activity?.applicationContext,
            view?.findViewById(android.R.layout.simple_list_item_1), locationOptionsList)
        lvVotingOptions.adapter = listAdapter

        // -1: There is no active voting for this group
        // -2: Only the group leader can start/end a poll

        btnStartVote.setOnClickListener {
            runBlocking {
                // authenticates user
                val client = HttpClient(CIO) {
                    install(Auth) {
                        basic {
                            credentials {
                                BasicAuthCredentials(username = userN, password = passW)
                            }
                        }
                    }
                }

                val response: String = client.post(my_url + "startVote") {
                    body = gid.toString()
                }
                if (response == "-2"){
                    Toast.makeText(activity?.applicationContext, "Only the group leader can start/end a poll", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnEndVote.setOnClickListener {
            runBlocking {
                // authenticates user
                val client = HttpClient(CIO) {
                    install(Auth) {
                        basic {
                            credentials {
                                BasicAuthCredentials(username = userN, password = passW)
                            }
                        }
                    }
                }

                val response: String = client.post(my_url + "voteResult") {
                    body = gid.toString()
                }
                if (response == "-1"){
                    Toast.makeText(activity?.applicationContext, "There is no active voting for this group", Toast.LENGTH_SHORT).show()
                }
                if (response == "-2"){
                    Toast.makeText(activity?.applicationContext, "Only the group leader can end a poll", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnSubmitLocation.setOnClickListener {
            if (txtEnterLocation.text == ""){
                Toast.makeText(activity?.applicationContext, "Location field cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else {
                runBlocking {
                    // authenticates user
                    val client = HttpClient(CIO) {
                        install(Auth) {
                            basic {
                                credentials {
                                    BasicAuthCredentials(username = userN, password = passW)
                                }
                            }
                        }
                    }

                    val response: String = client.post(my_url + "addVotingLocation") {
                        body = gid.toString() + "," + txtEnterLocation.text
                    }
                    if (response == "-1") {
                        Toast.makeText(activity?.applicationContext,
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
                val client = HttpClient(CIO) {
                    install(Auth) {
                        basic {
                            credentials {
                                BasicAuthCredentials(username = userN, password = passW)
                            }
                        }
                    }
                }

                if (lvVotingOptions.isEnabled) {
                    locationOptions = client.post(my_url + "votingOptions") {
                        body = gid.toString()
                    }
                }
                else{
                    locationOptions = client.post(my_url + "votingScores") {
                        body = gid.toString()
                    }
                }
            }
            if (locationOptions == "-1"){
                Toast.makeText(activity?.applicationContext, "There is no active voting for this group", Toast.LENGTH_SHORT).show()
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
                // authenticates user
                val client = HttpClient(CIO) {
                    install(Auth) {
                        basic {
                            credentials {
                                BasicAuthCredentials(username = userN, password = passW)
                            }
                        }
                    }
                }

                if(locationOptionsList[i] == "") { locationOptions = "-3" }
                else {

                    val response: HttpResponse = client.post(my_url + "castVote") {
                        body = gid.toString() + "," + locationOptionsList[i]
                    }

                    locationOptions = client.post(my_url + "votingScores") {
                        body = gid.toString()
                    }
                }
            }
            if (locationOptions == "-3"){
                Toast.makeText(activity?.applicationContext, "There are no voting locations, try refreshing", Toast.LENGTH_SHORT).show()
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
        }*/
        return root
    }
}
