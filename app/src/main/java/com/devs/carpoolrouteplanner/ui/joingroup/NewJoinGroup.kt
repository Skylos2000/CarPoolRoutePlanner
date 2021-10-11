package com.devs.carpoolrouteplanner.ui.joingroup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.devs.carpoolrouteplanner.R
import androidx.lifecycle.lifecycleScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch

class NewJoinGroup : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.joingroup, container, false)
        //val my_url = getConfigValue("backend_url")
        val my_url = "http://10.45.228.103:3306/"

        var password = root.findViewById(R.id.password) as EditText
        val button: Button = root.findViewById(R.id.button)

        button.setOnClickListener{
            val code = password.text
            button.isClickable = false

            val username = this.activity?.getSharedPreferences("login_details", Context.MODE_PRIVATE)
                ?.getString("username","");
            val password =
                this.activity?.getSharedPreferences("login_details", Context.MODE_PRIVATE)
                    ?.getString("password","");

            lifecycleScope.launch{
                val apiUrl = my_url

                apiUrl?.let{
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
                        val response: HttpResponse = client.get(apiUrl + "join_group/$code")

                        val gid: String = response.receive()
                        button.isClickable = true

                        if(gid == "EMPTY"){
                            Toast.makeText(activity?.applicationContext, "No group with the given invite code", Toast.LENGTH_SHORT).show()
                        }else
                        {
                            Toast.makeText(activity?.applicationContext, "Group joined", Toast.LENGTH_SHORT).show()
                            //intent.putExtra("GID",gid)
                            //startActivity(intent)
                        }
                    } catch (e: Exception) {
                        button.isClickable = false

                        Toast.makeText(activity?.applicationContext, "No group with the given invite code", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        return root
    }
}