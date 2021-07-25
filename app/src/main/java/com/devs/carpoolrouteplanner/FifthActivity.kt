package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FifthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fifthactivity)

        val button1: Button = findViewById(R.id.host)
        val button2: Button = findViewById(R.id.guest)

        val intent1 = Intent(this@FifthActivity, SixthActivity::class.java)
        val intent2 = Intent(this@FifthActivity, EleventhActivity::class.java)

        button1.setOnClickListener {
            startActivity(intent1)
        }

        button2.setOnClickListener {
            startActivity(intent2)
        }

    }
}