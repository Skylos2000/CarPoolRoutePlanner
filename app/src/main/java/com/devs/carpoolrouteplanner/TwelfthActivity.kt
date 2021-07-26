package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TwelfthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.twelfthactivity)

        val button1: Button = findViewById(R.id.stay)
        val button2: Button = findViewById(R.id.leave)

        val intent1 = Intent(this@TwelfthActivity, EleventhActivity::class.java)
        val intent2 = Intent(this@TwelfthActivity, EleventhActivity::class.java)

        button1.setOnClickListener {
            startActivity(intent1)
        }

        button2.setOnClickListener {
            startActivity(intent2)
        }
    }
}