package com.devs.carpoolrouteplanner.ui.voting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.devs.carpoolrouteplanner.R
import com.devs.carpoolrouteplanner.databinding.FragmentGroupVotingBinding
import com.devs.carpoolrouteplanner.databinding.FragmentHomeBinding
import com.devs.carpoolrouteplanner.ui.MainGroupActivity
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.utils.httpClient
import com.google.android.material.snackbar.Snackbar
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.android.synthetic.main.fragment_group_voting.*
import kotlinx.coroutines.runBlocking

/**
 * A simple [Fragment] subclass.
 * Use the [GroupVotingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupVotingFragment : Fragment() {

    lateinit var locationOptions: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_voting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val my_url = context?.getConfigValue("backend_url")
        var gid = (activity as MainGroupActivity).gid

        var btnStartVote: Button = btnStartVote
        var btnEndVote: Button = btnEndVote
        var btnSubmitLocation: Button = btnSubmitLocation
        var btnRefresh: Button = btnRefresh
        var btnTutorial: ImageButton = btnTutorial
        var txtTutorial: TextView = txtTutorial
        var txtEnterLocation: TextView = txtEnterLocation
        var lvVotingOptions: ListView = lvVotingOptions
        txtTutorial.isVisible = false

        var isSnackbarShowing = false

        runBlocking {
            locationOptions = httpClient.post("$my_url/votingOptions") {
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


        var listAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, locationOptionsList)
        lvVotingOptions.adapter = listAdapter

        // -1: There is no active voting for this group
        // -2: Only the group leader can start/end a poll

//        val snackbar = Snackbar.make(this.requireView(), "Rules for voting:\n- Only the group leader can start and stop voting\n" +
//                "\n- You cannot submit a location for voting if it is already an option\n" +
//                "\n- Press the 'Refresh Voting Locations' button to see to see newly added voting locations\n" +
//                "\n- Once you vote, current voting scores are shown\n" +
//                "\n- Press the 'Refresh Voting Locations' to update scores after voting\n" +
//                "\n- You may only vote once\n" +
//                "\nPress the 'i' button again to close this window", Snackbar.LENGTH_INDEFINITE).setAction("dismiss"){}
        btnTutorial.setOnClickListener{
            //snackbar.show()
            if (!txtTutorial.isVisible) {
                btnStartVote.isClickable = false
                btnEndVote.isClickable = false
                btnSubmitLocation.isClickable = false
                btnRefresh.isClickable = false
                lvVotingOptions.isClickable = false
                txtEnterLocation.isClickable = false


                txtTutorial.isVisible = true
                txtTutorial.text = "Rules for voting:\n" +
                        "\n- Only the group leader can start and stop voting" +
                        "\n- You cannot submit a location for voting if it is already an option" +
                        "\n- Press the 'Refresh Voting Locations' button to see to see newly added voting locations" +
                        "\n- Once you vote, current voting scores are shown" +
                        "\n- Press the 'Refresh Voting Locations' to update scores after voting" +
                        "\n- You may only vote once\n" +
                        "\nPress the 'i' button again to close this window"
            }
            else {
                //txtTutorial.text = ""
                txtTutorial.isVisible = false

                btnStartVote.isClickable = true
                btnEndVote.isClickable = true
                btnSubmitLocation.isClickable = true
                btnRefresh.isClickable = true
                lvVotingOptions.isClickable = true
                txtEnterLocation.isClickable = true
            }

        }

        btnStartVote.setOnClickListener {

            val response: String
            runBlocking {
                response = httpClient.post("$my_url/startVote") {
                    body = gid.toString()
                }
            }
            if (response == "-2"){
                Toast.makeText(this.context, "Only the group leader can start/end a poll", Toast.LENGTH_SHORT).show()
            }
        }

        btnEndVote.setOnClickListener {

            val response: String
            runBlocking {
                response = httpClient.post("$my_url/voteResult") {
                    body = gid.toString()
                }
            }
            if (response == "-1"){
                Toast.makeText(this.context, "There is no active voting for this group", Toast.LENGTH_SHORT).show()
            }
            if (response == "-2"){
                Toast.makeText(this.context, "Only the group leader can end a poll", Toast.LENGTH_SHORT).show()
            }
        }

        btnSubmitLocation.setOnClickListener {
            if (txtEnterLocation.text == ""){
                Toast.makeText(this.context, "Location field cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else {
                val response: String
                runBlocking {
                    response = httpClient.post("$my_url/addVotingLocation") {
                        body = gid.toString() + "," + txtEnterLocation.text
                    }
                }
                if (response == "-1") {
                    Toast.makeText(this.context,
                        "Voting has not started",
                        Toast.LENGTH_SHORT).show()
                }
                if (response == "-4") {
                    Toast.makeText(this.context,
                        "This voting location has already been added",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        // make new route for this in the backend
        btnRefresh.setOnClickListener {
            runBlocking {
                // authenticates user
                if (lvVotingOptions.isEnabled) {
                    locationOptions = httpClient.post("$my_url/votingOptions") {
                        body = gid.toString()
                    }
                }
                else{
                    locationOptions = httpClient.post("$my_url/votingScores") {
                        body = gid.toString()
                    }
                }
            }
            if (locationOptions == "-1"){
                Toast.makeText(this.context, "There is no active voting for this group", Toast.LENGTH_SHORT).show()
            }
            else {
                locationOptions = locationOptions.replace("[", "")
                locationOptions = locationOptions.replace("]", "")
                locationOptions = locationOptions.replace(34.toChar().toString(), "")
                locationOptionsList = locationOptions.split(",")

                listAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, locationOptionsList)
                lvVotingOptions.adapter = listAdapter
            }
        }

        lvVotingOptions.setOnItemClickListener { adapterView, view, i, l ->
            runBlocking {

                if(locationOptionsList[i] == "") { locationOptions = "-3" }
                else {

                    val response: HttpResponse = httpClient.post("$my_url/castVote") {
                        body = gid.toString() + "," + locationOptionsList[i]
                    }

                    locationOptions = httpClient.post("$my_url/votingScores") {
                        body = gid.toString()
                    }
                }
            }
            when (locationOptions) {
                "-1" -> {
                    Toast.makeText(this.context, "There is no active voting for this group", Toast.LENGTH_SHORT).show()
                }
                "-3" -> {
                    Toast.makeText(this.context, "There are no voting locations, try refreshing", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    locationOptions = locationOptions.replace("[", "")
                    locationOptions = locationOptions.replace("]", "")
                    locationOptions = locationOptions.replace(34.toChar().toString(), "")
                    locationOptionsList = locationOptions.split(",")

                    listAdapter =
                        ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, locationOptionsList)
                    lvVotingOptions.adapter = listAdapter

                    lvVotingOptions.isClickable = false
                    lvVotingOptions.isEnabled = false
                }
            }
        }
    }

}