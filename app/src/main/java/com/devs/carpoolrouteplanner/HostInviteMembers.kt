package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class HostInviteMembers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hostinvitemembers)

        var email = findViewById(R.id.email) as EditText
        val button: Button = findViewById(R.id.button)
        val intent = Intent(this@HostInviteMembers, GroupHostMenu::class.java)

        button.setOnClickListener{
            val username = email.text
            if(username.toString().equals("hello")){
                Toast.makeText(this, "Invite Send", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
            else{
                Toast.makeText(this, username, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
