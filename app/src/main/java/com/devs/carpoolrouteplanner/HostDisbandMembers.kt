package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class HostDisbandMembers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hostdisbandgroup)

        val password = findViewById<EditText>(R.id.password)
        val button = findViewById<Button>(R.id.button)
        val intent = Intent(this@HostDisbandMembers, GroupHostMenu::class.java)

        button.setOnClickListener {
            val code = password.text
            if (code.toString() == "world") {
                Toast.makeText(this, code, Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else {
                Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}