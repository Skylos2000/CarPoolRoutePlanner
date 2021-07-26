package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class HostRemoveMembers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hostremovemembers)
        //Remove Members
        var email = findViewById(R.id.email) as EditText
        var password = findViewById(R.id.password) as EditText
        val button: Button = findViewById(R.id.button)
        val intent = Intent(this@HostRemoveMembers, GroupHostMenu::class.java)

        button.setOnClickListener{
            val username = email.text
            val code = password.text
            if(username.toString().equals("hello") && code.toString().equals("world")){
                Toast.makeText(this, code, Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
            else{
                Toast.makeText(this, username, Toast.LENGTH_SHORT).show()
            }
    }
}
}