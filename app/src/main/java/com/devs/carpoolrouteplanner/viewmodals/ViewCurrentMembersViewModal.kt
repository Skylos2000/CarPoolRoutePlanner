package com.devs.carpoolrouteplanner.viewmodals

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.devs.carpoolrouteplanner.ViewCurrentMembers
import com.devs.carpoolrouteplanner.utils.getConfigValue
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray

data class MemberModel(val name:String,val latitude:Double,val longitude:Double)

class ViewCurrentMembersViewModal(application: Application) : AndroidViewModel(application) {
    public val currentMembers: MutableLiveData<Array<MemberModel>> = MutableLiveData<Array<MemberModel>>()

    public fun loadMembers(apiUrl:String,gid:String){
        val username = getApplication<Application>().getSharedPreferences("login_details", Context.MODE_PRIVATE).getString("username","");
        val password = getApplication<Application>().getSharedPreferences("login_details", Context.MODE_PRIVATE).getString("password","");

        viewModelScope.launch {

            apiUrl?.let {
                val client = HttpClient(CIO)
                {
                    install(Auth) {
                        basic {
                            credentials {
                                BasicAuthCredentials(username = username!!, password = password!!)
                            }
                        }
                    }
                }
                try {
                    val response: HttpResponse = client.get(apiUrl + "get_group_members/$gid")

                    val members:JsonArray = response.receive()
                    print(members)

                } catch (e: Exception) {

                }
            }
        }
        currentMembers.value = arrayOf(MemberModel("John Doe",12.22,223.11)
                ,MemberModel("Jane Doe",12.22,223.11),
            MemberModel("James Bond",12.22,223.11)
        )
    }
}