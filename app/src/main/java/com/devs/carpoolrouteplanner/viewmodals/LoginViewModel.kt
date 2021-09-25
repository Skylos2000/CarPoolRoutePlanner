package com.devs.carpoolrouteplanner.viewmodals

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devs.carpoolrouteplanner.utils.LoginResult
import com.devs.carpoolrouteplanner.utils.httpClient
import com.devs.carpoolrouteplanner.utils.installAuth
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*

// TODO: Is this file needed?
class LoginViewModel: ViewModel() {
    val loginResult: MutableLiveData<LoginResult> = MutableLiveData<LoginResult>()

    suspend fun login(apiUrl: String, username: String, password: String) {
        val result: LoginResult = installAuth(apiUrl, username, password)
        loginResult.value = result
    }
}