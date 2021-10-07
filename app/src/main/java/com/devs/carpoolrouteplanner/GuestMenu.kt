package com.devs.carpoolrouteplanner

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.utils.getConfigValue
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.launch

class GuestMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guestmenu)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val inviteOthers: Button = findViewById(R.id.inviteOthers)
        val deleteGroup: Button = findViewById<Button>(R.id.button6)

        val intent1 = Intent(this@GuestMenu, ViewCurrentMembers::class.java)
        val intent2 = Intent(this@GuestMenu, GuestLeaveMembers::class.java)
        val intent3 = Intent(this@GuestMenu, GuestLogOut::class.java)

        val gid = intent.getStringExtra("GID")
        button1.setOnClickListener {
            intent1.putExtra("GID", gid)
            startActivity(intent1)
        }

        button2.setOnClickListener {
            startActivity(intent2)
        }

        button3.setOnClickListener {
            startActivity(intent3)
        }

        deleteGroup.setOnClickListener {
            showdialog()
        }

        inviteOthers.setOnClickListener {
            val qrIntent = Intent(this@GuestMenu, InviteByQR::class.java)
            startActivity(qrIntent)
//            return@setOnClickListener
//            val myurl = getConfigValue("backend_url") ?: return@setOnClickListener
//            val gid = "123"
//            inviteOthers.isClickable = false
//            lifecycleScope.launch {
//
//                val client = HttpClient(CIO) {
//                    install(Auth) {
//                        basic {
//                            credentials {
//                                BasicAuthCredentials(username = AccountSignIn.creds[0],
//                                    password = AccountSignIn.creds[1])
//                            }
//
//                        }
//                    }
//                }
//
//                try {
//                    val response: String = client.submitForm (
//                        url = myurl+ "create_invite/",
//                        formParameters = Parameters.build {
//                            append("gid",gid)
//                        }
//                    )
//                    val data = response
//                    Toast.makeText(applicationContext,
//                        data,
//                        Toast.LENGTH_LONG).show()
//                    var inviteCode = data;
//                    inviteOthers.isClickable = true
//
//                    var dummyLink = "https://coolrouteplanner.test/join/${inviteCode}";
//                    val shareIntent = Intent()
//                    shareIntent.action = Intent.ACTION_SEND
//                    shareIntent.type = "text/plain"
//                    shareIntent.putExtra(Intent.EXTRA_TEXT,
//                        "Please join my group using this invite code: ${inviteCode} or click on this link: {$dummyLink}")
//                    startActivity(Intent.createChooser(shareIntent, "Send To"))
//
////                    Toast.makeText(applicationContext, "Successfully created invite link.${data}", Toast.LENGTH_LONG).show()
//                } catch (E: Exception) {
//                    Toast.makeText(applicationContext,
//                        "Exception: No group with gid 123 or ${E.message}",
//                        Toast.LENGTH_LONG).show()
//                    inviteOthers.isClickable = true
//                }
//
//
//            }
        }
    }

    fun showdialog(){
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Input GID to delete")

// Set up the input
        val input = EditText(this)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Enter Id")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

// Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            var m_Text = input.text.toString()
            val my_url = getConfigValue("backend_url") ?: return@OnClickListener
            lifecycleScope.launch {

                val client = HttpClient(CIO) {
                    install(Auth) {
                        basic {
                            credentials {
                                BasicAuthCredentials(username = AccountSignIn.creds[0], password = AccountSignIn.creds[1])
                            }
                        }
                    }
                }

                try{
                    val response: String = client.post(my_url + "delete_group"){

                        body = m_Text
                    }
                    val data = response
                    Toast.makeText(applicationContext, "Successfully performed delete action", Toast.LENGTH_LONG).show()
                    dialog.cancel()
                }catch (E:Exception){
                    Toast.makeText(applicationContext,"Failed to delete. Wrong Group Id?",Toast.LENGTH_LONG).show()
                    dialog.cancel()
                }
            }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }
}