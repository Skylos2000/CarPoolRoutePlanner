package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainMenuLogOut : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainmenulogout)

        val button1: Button = findViewById(R.id.stay)
        val button2: Button = findViewById(R.id.leave)

        val intent1 = Intent(this@MainMenuLogOut, MainMenu::class.java)
        val intent2 = Intent(this@MainMenuLogOut, AccountSignIn::class.java)

        button1.setOnClickListener {
            startActivity(intent1)
        }

        button2.setOnClickListener {
            startActivity(intent2)
        }
    }
}