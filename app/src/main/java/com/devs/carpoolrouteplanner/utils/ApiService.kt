package com.devs.carpoolrouteplanner.utils

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

// TODO: Find a better place to put this
data class RowUser(
    val id: Int,
    val email: String,
    val username: String,
    val password: String, // This should never get used
    val defaultPickupLatitude: Double?,
    val defaultPickupLongitude: Double?,
)

val httpClient = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = KotlinxSerializer() // TODO: Is this needed?
    }
}

suspend fun installAuth(apiUrl: String, username: String, password: String): LoginResult {
    // Load the Auth plugin with the given credentials
    httpClient.config {
        install(Auth) {
            basic {
                BasicAuthCredentials(username, password)
            }
        }
    }

    // Test the credentials
    val response = httpClient.get<HttpResponse>("$apiUrl/my_user_info/")

    if (response.status == HttpStatusCode.OK) {
        val userData = response.receive<RowUser>()
        return LoginResult(true, LoggedInUser(userData.id, userData.username))
    }

    return LoginResult(false, null)
}

//class ApiService {
//    companion object {
//        suspend fun login(apiUrl:String,username: String, password: String): LoginResult {
//            val client = HttpClient(CIO) {
//                install(Auth) {
//                    basic {
//                        credentials {
//                            BasicAuthCredentials(username = username, password = password)
//                        }
//                    }
//                }
//            }
//            try {
//                val response: HttpResponse = client.get(apiUrl + "example/what_is_my_name/")
//                if (response.status.value == 401) {
//                    return LoginResult(
//                        success = false,
//                        message = "User/Password is wrong",
//                        user = LoggedInUser(uid = "", name = "")
//                    )
//                }
//                val name: String = response.receive()
//                return LoginResult(
//                    success = true,
//                    message = "Welcome " + name,
//                    LoggedInUser(uid = username + "|" + password, name = name,username=username,password=password)
//                )
//            } catch (e: Exception) {
//                return LoginResult(success = false, message = "Wrong Username/Password")
//            }
//        }
//    }
//}