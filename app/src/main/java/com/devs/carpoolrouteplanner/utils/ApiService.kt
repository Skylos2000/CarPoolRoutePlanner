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

@Serializable
data class User(val userId: Int, val email: String, val username: String, val groupIds: List<Int>)

private var authCredentials: BasicAuthCredentials? = null
private var bearerToken: BearerTokens? = null

lateinit var httpClient: HttpClient

suspend fun installAuth(apiUrl: String, username: String, password: String): LoginResult {
    // Load credentials
//    authCredentials = BasicAuthCredentials(username, password)
    val loginClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer() // TODO: Is this needed?
        }
    }
    val token = loginClient.post<String>("$apiUrl/login") {
        contentType(ContentType.Application.Json)
        body = hashMapOf("username" to username, "password" to password)
    }

    httpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer() // TODO: Is this needed?
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(token, "")
                }
            }
        }
    }
    // Test the credentials
    val response = httpClient.get<HttpResponse>(apiUrl + "/users/me") {

    }

    if (response.status == HttpStatusCode.OK) {
        val userData = response.receive<SerializedUser>()
        return LoginResult(true, LoggedInUser(userData.userId, userData.username))
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