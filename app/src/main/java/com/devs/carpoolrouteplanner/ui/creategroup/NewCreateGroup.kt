package com.devs.carpoolrouteplanner.ui.creategroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.R
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch

class NewCreateGroup : Fragment(){
    //val my_url = getConfigValue("backend_url")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.creategroup, container, false)
        //val my_url = getConfigValue("backend_url")
        val my_url = "http://10.45.228.103:3306/"

        var button = root.findViewById(R.id.button) as Button
        button.setOnClickListener {
            button.isClickable = false
            lifecycleScope.launch {
                val client = HttpClient(CIO) {
                    install(Auth) {
                        basic {
                            credentials {
                                BasicAuthCredentials(username = "aaa", password = "eee")
                            }
                        }
                    }
                }

                val response: HttpResponse = client.post(my_url + "create_group/") {
                }
            }
            //finish()
            button.isClickable = true
        }
        return root
    }
}
