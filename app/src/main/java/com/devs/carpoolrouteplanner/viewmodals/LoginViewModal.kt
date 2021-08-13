package com.devs.carpoolrouteplanner.viewmodals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devs.carpoolrouteplanner.utils.ApiService
import com.devs.carpoolrouteplanner.utils.LoggedInUser
import com.devs.carpoolrouteplanner.utils.LoginResult

class LoginViewModal: ViewModel() {
    public val loginResult: MutableLiveData<LoginResult> = MutableLiveData<LoginResult>()
    suspend fun login(apiUrl:String,username:String,password:String)
    {
        val result: LoginResult = ApiService.login(apiUrl,username.toString(), password.toString())
        loginResult.value = result
    }
}