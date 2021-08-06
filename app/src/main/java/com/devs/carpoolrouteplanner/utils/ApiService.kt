package com.devs.carpoolrouteplanner.utils

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class ApiService {
    companion object {
        val apiUrl = "http://10.0.0.53:8080/"
        suspend fun login(username: String, password: String): LoginResult {
            val client = HttpClient(CIO) {
                install(Auth) {
                    basic {
                        credentials {
                            BasicAuthCredentials(username = username, password = password)
                        }
                    }
                }
            }
            try {
                val response: HttpResponse = client.get(apiUrl + "example/what_is_my_name/")
                if (response.status.value == 401) {
                    return LoginResult(
                        success = false,
                        message = "User/Password is wrong",
                        user = LoggedInUser(uid = "", name = "")
                    )
                }
                val name: String = response.receive()
                return LoginResult(
                    success = true,
                    message = "Welcome " + name,
                    LoggedInUser(uid = username + "|" + password, name = name)
                )
            } catch (e: Exception) {
                return LoginResult(success = false, message = "Wrong Username/Password")
            }
        }
    }
}