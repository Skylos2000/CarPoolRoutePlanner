package com.devs.carpoolrouteplanner

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.utils.getConfigValue
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text

class SolomonGroupVote: AppCompatActivity() {

    lateinit var locationOptions: String

    // username and password from companion object
    val userN = AccountSignIn.creds[0]
    val passW = AccountSignIn.creds[1]

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

        locationOptions = locationOptions.replace("[", "")
        locationOptions = locationOptions.replace("]", "")
        locationOptions = locationOptions.replace(34.toChar().toString(), "")
        var locationOptionsList: List<String> = locationOptions.split(",")

        var listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationOptionsList)
        lvVotingOptions.adapter = listAdapter


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

                val response: HttpResponse = client.post(my_url + "startVote") {
                    body = gid.toString()
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

                val response: HttpResponse = client.post(my_url + "voteResult") {
                    body = gid.toString()
                }
            }
        }

        btnSubmitLocation.setOnClickListener {
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
                if (response.toString() == "Voting has not started"){
                    Toast.makeText(this@SolomonGroupVote, response.toString(), Toast.LENGTH_SHORT).show()
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
            locationOptions = locationOptions.replace("[", "")
            locationOptions = locationOptions.replace("]", "")
            locationOptions = locationOptions.replace(34.toChar().toString(), "")
            locationOptionsList = locationOptions.split(",")

            listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationOptionsList)
            lvVotingOptions.adapter = listAdapter
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

                val response: HttpResponse = client.post(my_url + "castVote") {
                    body = gid.toString() + "," + locationOptionsList[i]
                }

                locationOptions = client.post(my_url + "votingScores") {
                    body = gid.toString()
                }
            }
            locationOptions = locationOptions.replace("[", "")
            locationOptions = locationOptions.replace("]", "")
            locationOptions = locationOptions.replace(34.toChar().toString(), "")
            locationOptionsList = locationOptions.split(",")

            listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationOptionsList)
            lvVotingOptions.adapter = listAdapter

            lvVotingOptions.isClickable = false
            lvVotingOptions.isEnabled = false
        }
    }
}