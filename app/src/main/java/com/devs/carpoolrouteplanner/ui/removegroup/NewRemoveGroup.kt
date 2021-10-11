package com.devs.carpoolrouteplanner.ui.removegroup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.GroupHostMenu
import com.devs.carpoolrouteplanner.R
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch



class NewRemoveGroup : Fragment(){
    //val my_url = getConfigValue("backend_url")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.hostremovemembers, container, false)
        //val my_url = getConfigValue("backend_url")
        val my_url = "http://10.45.228.103:3306/"

        var email = root.findViewById(R.id.Gemail) as EditText
        var password = root.findViewById(R.id.Gpass) as EditText
        val button: Button = root.findViewById(R.id.button5)

        button.setOnClickListener{
            val username = email.text
            val code = password.text
            if(username.toString().equals("hello") && code.toString().equals("world")){
                Toast.makeText(activity?.applicationContext, code, Toast.LENGTH_SHORT).show()
                //startActivity(intent)
            }
            else{
                Toast.makeText(activity?.applicationContext, username, Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }
}
