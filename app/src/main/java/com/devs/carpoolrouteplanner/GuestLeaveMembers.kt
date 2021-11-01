package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class GuestLeaveMembers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: I'm leaving this out of the refactoring for now because I think it should be a message box
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guestleavegroup)

        val button1: Button = findViewById(R.id.stay)
        val button2: Button = findViewById(R.id.leave)

        val intent1 = Intent(this@GuestLeaveMembers, GuestMenu::class.java)
        val intent2 = Intent(this@GuestLeaveMembers, GuestMenu::class.java)

        button1.setOnClickListener {
            startActivity(intent1)
        }

        button2.setOnClickListener {
            startActivity(intent2)
        }
    }
}