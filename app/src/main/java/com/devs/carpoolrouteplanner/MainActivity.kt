package com.devs.carpoolrouteplanner


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var email = findViewById(R.id.email) as EditText
        var password = findViewById(R.id.password) as EditText

        val button: Button = findViewById(R.id.button)
        val intent = Intent(this@MainActivity, SecondActivity::class.java)

        button.setOnClickListener{
            val username = email.text
            val code = password.text

            if(username.toString().equals("") && code.toString().equals("")){
                Toast.makeText(this, "Log In Successful", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}