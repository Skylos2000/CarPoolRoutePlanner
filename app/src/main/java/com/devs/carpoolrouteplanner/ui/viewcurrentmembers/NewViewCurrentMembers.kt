package com.devs.carpoolrouteplanner.ui.viewcurrentmembers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.R
import com.devs.carpoolrouteplanner.utils.getConfigValue
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch

class NewViewCurrentMembers : Fragment(){
    //val my_url = getConfigValue("backend_url")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.viewcurrentmembers, container, false)
        //val my_url = getConfigValue("backend_url")
        val my_url = "http://10.45.228.103:3306/"
        val button: Button = root.findViewById(R.id.button)

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

                val response: String = client.get(my_url + "list_my_groups/"){
                }
                val data = response
                Toast.makeText(activity?.applicationContext, data, Toast.LENGTH_LONG).show()
            }
            //finish()
            button.isClickable = true
        }
        return root
    }
}