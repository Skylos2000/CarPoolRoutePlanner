package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class GroupHostMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grouphostmenu)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)

        val intent1 = Intent(this@GroupHostMenu, HostInviteMembers::class.java)
        val intent2 = Intent(this@GroupHostMenu, ViewCurrentMembers::class.java)
        val intent3 = Intent(this@GroupHostMenu, HostRemoveMembers::class.java)
        val intent4 = Intent(this@GroupHostMenu, HostDisbandMembers::class.java)
        val intent5 = Intent(this@GroupHostMenu, HostLogOut::class.java)

        button1.setOnClickListener {
            startActivity(intent1)
        }

        button2.setOnClickListener {
            startActivity(intent2)
        }

        button3.setOnClickListener {
            startActivity(intent3)
        }

        button4.setOnClickListener {
            startActivity(intent4)
        }

        button5.setOnClickListener {
            startActivity(intent5)
        }
    }
}