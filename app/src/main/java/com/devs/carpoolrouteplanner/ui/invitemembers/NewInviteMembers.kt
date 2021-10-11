package com.devs.carpoolrouteplanner.ui.invitemembers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.devs.carpoolrouteplanner.R

class NewInviteMembers : Fragment() {
    //val my_url = getConfigValue("backend_url")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.hostinvitemembers, container, false)
        //val my_url = getConfigValue("backend_url")
        val my_url = "http://10.45.228.103:3306/"

        var email = root.findViewById(R.id.email) as EditText
        val button: Button = root.findViewById(R.id.button)

        button.setOnClickListener{
            val username = email.text
            if(username.toString().equals("hello")){
                Toast.makeText(activity?.applicationContext, "Invite Send", Toast.LENGTH_SHORT).show()
                //startActivity(intent)
            }
            else{
                Toast.makeText(activity?.applicationContext, username, Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }
}