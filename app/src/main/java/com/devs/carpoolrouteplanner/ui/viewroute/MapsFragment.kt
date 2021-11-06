package com.devs.carpoolrouteplanner.ui.viewroute

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devs.carpoolrouteplanner.R
import com.devs.carpoolrouteplanner.ui.MainActivity
import com.devs.carpoolrouteplanner.ui.MainGroupActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.route_recycler_view.*

class MapsFragment : Fragment() {

    private var userlatlng: LatLng? = null
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

        googleMap.isMyLocationEnabled=true


        var ruston = LatLng(32.5232,-92.6379)//where map opens to
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ruston))


        googleMap.setOnMapClickListener {
            googleMap.addMarker(MarkerOptions().position(it))
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
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        apply {

            val gid = (activity as MainGroupActivity).gid
            val adddestinationbutton: FloatingActionButton = fab
            adddestinationbutton.setOnClickListener {
                if(userlatlng==null){
                    Toast.makeText(this.context, "You must choose a location first.", Toast.LENGTH_LONG).show()
                }
                else{
                    addDestinationToDb()
                    Toast.makeText(this.context, "Destination Added", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_navigation_maps_fragment_to_navigation_group_manage_destinations)
                }
            }
        }

    }
    private fun addDestinationToDb(){
        //TODO Geocode latlng
        //TODO send dest. to db here
    }

}