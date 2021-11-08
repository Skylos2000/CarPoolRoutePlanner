package com.devs.carpoolrouteplanner.ui.viewroute

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment
import com.devs.carpoolrouteplanner.utils.GroupDestination
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devs.carpoolrouteplanner.R
import com.devs.carpoolrouteplanner.ui.MainGroupActivity
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.utils.httpClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.route_recycler_view.*
import kotlinx.coroutines.launch
import java.util.*

class MapsFragment : Fragment() {

    private var userlatlng: LatLng? = null

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        //TODO check permissions
//        if (ActivityCompat.checkSelfPermission(this.context,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this.context,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//        )

        if (ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
        else if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 69)
        }
        if (ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
        else if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 70)

        }
        googleMap.isMyLocationEnabled=true
        var marker: Marker? = null
        var ruston = LatLng(32.5232,-92.6379)//where map opens to
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ruston,12.0F))


        googleMap.setOnMapClickListener {
            marker?.remove()
            marker = googleMap.addMarker(MarkerOptions().position(it))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            userlatlng = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiKey = getString(R.string.api_key)

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }
        // Create a new Places client instance.
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        mapFragment?.getMapAsync(callback)
        apply {
            val adddestinationbutton: FloatingActionButton = fab
            adddestinationbutton.setOnClickListener {
                if(userlatlng==null){
                    Toast.makeText(this.context, "You must choose a location first.", Toast.LENGTH_LONG).show()
                }
                else{
                    addDestinationToDb(userlatlng!!.latitude, userlatlng!!.longitude)
                    Toast.makeText(this.context, "Destination Added", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_navigation_maps_fragment_to_navigation_group_manage_destinations)
                }
            }
        }

    }
    private fun addDestinationToDb(lat: Double,long: Double) {
        val gid = (activity as MainGroupActivity).gid
        val myurl = context?.getConfigValue("backend_url")
        //TODO Geocode latlng
        val geocoder = Geocoder(this.requireContext())
        val address: List<Address> = geocoder.getFromLocation(lat, long, 1)
//        try
//        {
//        val address: List<Address> = geocoder.getFromLocation(lat, long, 1)
//            Toast.makeText(this.requireContext(), address.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show()
//        }catch (E: Exception){
//            Toast.makeText(this.requireContext(), E.message, Toast.LENGTH_SHORT).show()
//        }


        //TODO send dest. to db here
        lifecycleScope.launch {
            try {
                val response: String = httpClient.post(myurl + "/groups/${gid}/add_destinations") {
                    contentType(ContentType.Application.Json)
                    body = listOf(GroupDestination(0,gid,lat,long, address.get(0).getAddressLine(0),99))

                }
//                Toast.makeText(activity?.applicationContext,
//                    "${response}",
//                    Toast.LENGTH_LONG).show()
            } catch (E: Exception) {
                Toast.makeText(activity?.applicationContext,
                    "${E.message}",
                    Toast.LENGTH_LONG).show()
                System.out.println(E.message)
                //throw E
            }
        }
    }
}