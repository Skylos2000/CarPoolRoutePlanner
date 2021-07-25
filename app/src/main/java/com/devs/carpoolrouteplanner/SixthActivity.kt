package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SixthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sixthactivity)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)

        val intent1 = Intent(this@SixthActivity, SeventhActivity::class.java)
        val intent2 = Intent(this@SixthActivity, EighthActivity::class.java)
        val intent3 = Intent(this@SixthActivity, NinthActivity::class.java)
        val intent4 = Intent(this@SixthActivity, TenthActivity::class.java)
        val intent5 = Intent(this@SixthActivity, FourteenthActivity::class.java)

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