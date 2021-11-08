package com.devs.carpoolrouteplanner.ui.viewroute

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavAction
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.devs.carpoolrouteplanner.R

import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.devs.carpoolrouteplanner.adapters.RecyclerAdapter
import com.devs.carpoolrouteplanner.utils.GroupDestination
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.utils.httpClient
import io.ktor.client.request.*
import com.devs.carpoolrouteplanner.ui.MainGroupActivity
import io.ktor.http.*

import kotlinx.android.synthetic.main.route_recycler_view.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ViewRouteFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapter

    private var destinations = mutableListOf<GroupDestination>()

    private var titleList = mutableListOf<String>()
    private var descriptionList = mutableListOf<String>()

    private lateinit  var routedata: List<List<String>>

    val gid get() = (activity as MainGroupActivity).gid
    val backendUrl get() = requireContext().getConfigValue("backend_url")!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.route_recycler_view, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        recycler_view.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            getData()
            adapter = RecyclerAdapter(titleList, descriptionList)
            val itemTouchHelper = ItemTouchHelper(simpleCallback)
            itemTouchHelper.attachToRecyclerView(this)
            val gid = (activity as MainGroupActivity).gid
            val newdestinationbutton: Button = add_destination
            val startroutebutton: Button = start_navigation
            val optimizeroute: Button = optimize_route
            newdestinationbutton.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_group_manage_destinations_to_navigation_maps_fragment)
            }
            startroutebutton.setOnClickListener {
                startNavigation()
            }
            optimizeroute.setOnClickListener {
                optimizeRoute()
            }
        }
    }

    private fun getData() {
        routedata = getDataFromDb()
        var cords = mutableListOf<String>()
        var title = mutableListOf<String>()
        for (aList in routedata) {
            cords.add("Coordinates: " + aList.elementAt(0) + "," + aList.elementAt(1))
            title.add("" + aList.elementAt(2))
        }
        titleList = title
        descriptionList = cords
        return

    }

    private fun getDataFromDb(): List<List<String>> {
        destinations = runBlocking {
            httpClient.get<List<GroupDestination>>("$backendUrl/groups/$gid/destinations")
        }.toMutableList()

        return destinations.sortedBy { it.orderNum }.map { listOf(it.lat.toString(), it.long.toString(), it.label) }
//         return listOf(listOf("30","-90","Home"),listOf("29","-90","Work"),listOf("29","-89","Louisiana Tech"),listOf("29","-89.5","Tractor Supply"))
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(
        ItemTouchHelper.DOWN), 1) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            var startPosition = viewHolder.bindingAdapterPosition
            var endPosition = target.bindingAdapterPosition
            recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)//send back to db here
            reorderData(startPosition, endPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

        private fun reorderData(start: Int, end: Int) {
            Collections.swap(destinations, start, end)

            //TODO sent new order to db here
            val newOrderPairs = destinations.mapIndexed { index, groupDestination ->
                mapOf("first" to groupDestination.destinationId, "second" to index)
            }
            lifecycleScope.launch {
                httpClient.post<String>("$backendUrl/groups/$gid/reorder_destinations") {
                    contentType(ContentType.Application.Json)
                    body = newOrderPairs
                }
            }
        }
    }

    private fun optimizeRoute() {
        //TODO add backend call to optimize route
    }

    private fun deleteDestination(destination: GroupDestination) {
        runBlocking {
            httpClient.post<String>("$backendUrl/groups/${gid}/destinations/${destination.destinationId}/delete")
        }
    }

    private fun startNavigation(){
        //TODO make gmaps open
        //destinationString = httpClient.get<List<Pair<Double,Double>>>(backendUrl + "/get_group_routes/${httpResponse.first()}").joinToString("|"){ "${it.first},${it.second}" }
        //Uri.parse("https://www.google.com/maps/dir/?api=1&destination=18.518496,73.879259&travelmode=driving&waypoints=$destinationString")

        var destinationString = ""
        var finalDest = ""

        for (i in 1 until routedata.size) {
            if (i == 1) {
                destinationString += routedata[i][0] + "," + routedata[i][1]
            } else if (i != 1 && i != routedata.size -1) {
                destinationString += "|" + routedata[i][0] + "," + routedata[i][1]
            } else {
                finalDest += routedata[i][0] + "," + routedata[i][1]
            }
        }

        val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + finalDest + "&travelmode=driving&waypoints=" + destinationString)

        System.out.println(gmmIntentUri)

        val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        intent.setPackage("com.google.android.apps.maps")
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            try {
                val unrestrictedIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                startActivity(unrestrictedIntent)
            } catch (innerEx: ActivityNotFoundException) {
                Toast.makeText(this.requireContext(), "Please install a maps application", Toast.LENGTH_LONG)
                    .show()
            }
        }
        //testing git

    }

}

