package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button

class MainMenuLogOut : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainmenulogout)

        val dev_menu: Button = findViewById(R.id.dev_menu)

        dev_menu.setOnClickListener {
            val intent1 = Intent(this@MainMenuLogOut, MainMenu::class.java)
            startActivity(intent1)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homeoptions,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_create->{
                val intent2 = Intent(this@MainMenuLogOut, CreateGroup::class.java)
                startActivity(intent2)
                true
            }

            R.id.action_join->{
                val intent2 = Intent(this@MainMenuLogOut, JoinGroup::class.java)
                startActivity(intent2)
                true
            }

            R.id.action_manage->{
                val intent2 = Intent(this@MainMenuLogOut, GroupManagementType::class.java)
                startActivity(intent2)
                true
            }

            R.id.action_logout->{
                AccountSignIn.creds[0]=""
                AccountSignIn.creds[1]=""
                val intent2 = Intent(this@MainMenuLogOut, AccountSignIn::class.java)
                startActivity(intent2)
                true
            }
            else->super.onOptionsItemSelected(item)
        }

    }
}