package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.devs.carpoolrouteplanner.ui.AccountSignIn

class GuestLogOut : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guestlogout)

        val button1: Button = findViewById(R.id.stay)
        val button2: Button = findViewById(R.id.leave)

        val intent1 = Intent(this@GuestLogOut, GuestMenu::class.java)
        val intent2 = Intent(this@GuestLogOut, AccountSignIn::class.java)

        button1.setOnClickListener {
            startActivity(intent1)
        }

        button2.setOnClickListener {
            startActivity(intent2)
        }
    }
}