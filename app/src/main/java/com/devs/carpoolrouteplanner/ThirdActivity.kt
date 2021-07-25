package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thirdactivity)

        var password = findViewById(R.id.password) as EditText
        val button: Button = findViewById(R.id.button)
        val intent = Intent(this@ThirdActivity, EleventhActivity::class.java)

        button.setOnClickListener{
            val code = password.text
            if( code.toString().equals("world")){
                Toast.makeText(this, code, Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Type world", Toast.LENGTH_SHORT).show()
            }
        }


    }
}