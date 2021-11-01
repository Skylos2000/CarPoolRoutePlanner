package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class GroupManagementType : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.groupmanagementtype)

        val hostManagementButton = findViewById<Button>(R.id.host)
        val guestManagementButton = findViewById<Button>(R.id.guest)

        val groupHostMenu = Intent(this@GroupManagementType, GroupHostMenu::class.java)
        val groupGuestMenu = Intent(this@GroupManagementType, GuestMenu::class.java)

        hostManagementButton.setOnClickListener {
            startActivity(groupHostMenu)
        }

        guestManagementButton.setOnClickListener {
            startActivity(groupGuestMenu)
        }

    }
}