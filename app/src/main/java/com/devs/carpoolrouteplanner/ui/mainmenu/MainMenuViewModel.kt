package com.devs.carpoolrouteplanner.ui.mainmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainMenuViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the mainmenu Fragment"
    }
    val text: LiveData<String> = _text
}