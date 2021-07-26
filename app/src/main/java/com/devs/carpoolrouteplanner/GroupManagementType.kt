package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class GroupManagementType : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.groupmanagementtype)

        val button1: Button = findViewById(R.id.host)
        val button2: Button = findViewById(R.id.guest)

        val intent1 = Intent(this@GroupManagementType, GroupHostMenu::class.java)
        val intent2 = Intent(this@GroupManagementType, GuestMenu::class.java)

        button1.setOnClickListener {
            startActivity(intent1)
        }

        button2.setOnClickListener {
            startActivity(intent2)
        }

    }
}