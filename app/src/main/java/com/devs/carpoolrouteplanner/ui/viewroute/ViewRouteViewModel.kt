package com.devs.carpoolrouteplanner.ui.viewroute

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewRouteViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the route view Fragment"
    }
    val text: LiveData<String> = _text
}