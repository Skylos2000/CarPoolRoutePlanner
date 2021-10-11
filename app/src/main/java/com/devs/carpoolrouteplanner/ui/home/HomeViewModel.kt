package com.devs.carpoolrouteplanner.ui.home

import android.content.Intent
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devs.carpoolrouteplanner.Solomon

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Testing Preview. For viewing purpose only."
    }
    val text: LiveData<String> = _text

}