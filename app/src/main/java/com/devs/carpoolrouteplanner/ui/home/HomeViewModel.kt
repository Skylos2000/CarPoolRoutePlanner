package com.devs.carpoolrouteplanner.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devs.carpoolrouteplanner.utils.httpClient
import io.ktor.client.request.*

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Carpool Route Planner \n\nThis app is intended to help you plan a route with either you or your friends. \n\nPlease click the button on the left top side of your screen to begin your journey. \n\nThank you for using our app."
    }
    val text: LiveData<String> = _text
}