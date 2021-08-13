package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class GuestMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guestmenu)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)

        val intent1 = Intent(this@GuestMenu, ViewCurrentMembers::class.java)
        val intent2 = Intent(this@GuestMenu, GuestLeaveMembers::class.java)
        val intent3 = Intent(this@GuestMenu, GuestLogOut::class.java)

        val gid = intent.getStringExtra("GID")
        button1.setOnClickListener {
            intent1.putExtra("GID",gid)
            startActivity(intent1)
        }

        button2.setOnClickListener {
            startActivity(intent2)
        }

        button3.setOnClickListener {
            startActivity(intent3)
        }
    }
}