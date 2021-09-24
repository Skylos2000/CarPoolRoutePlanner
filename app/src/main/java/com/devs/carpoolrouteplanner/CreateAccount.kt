package com.devs.carpoolrouteplanner

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.utils.getConfigValue
import kotlinx.coroutines.launch

class CreateAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        val usernameTextBox = findViewById<EditText>(R.id.createAccount_usernameTextBox)
        val emailTextBox = findViewById<EditText>(R.id.createAccount_emailTextBox)
        val passwordTextBox = findViewById<EditText>(R.id.createAccount_passwordTextBox)
        val progressBar = findViewById<ProgressBar>(R.id.createAccount_progressBar)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val accountSignInIntent = Intent(this@CreateAccount, AccountSignIn::class.java)

        progressBar.visibility = View.GONE

        submitButton.setOnClickListener {
            val username = usernameTextBox.text.toString()
            val email = emailTextBox.text.toString()
            val password = passwordTextBox.text.toString()

            if (username != "" && email != "" && password != "") {
                progressBar.visibility = View.VISIBLE
                submitButton.isClickable = false
                lifecycleScope.launch {
                    try {
                        val client = HttpClient(CIO)
                        val url = getConfigValue("backend_url")
                        client.post<String>(url + "signup_text/") {
                                body = "%s,%s,%s".format(username, password, email)
                        }
                        Toast.makeText(this@CreateAccount, "Account Created Successfully, Please log in.", Toast.LENGTH_SHORT).show()
                    }
                    catch (e: Exception) {
                        Toast.makeText(this@CreateAccount, "Account Creation Failed, Please Try Again.", Toast.LENGTH_SHORT).show()
                    }//if (success) {
                    startActivity(accountSignInIntent)
                    finish()
                    progressBar.visibility= View.GONE
                    submitButton.isClickable = true
                }

            }
            else {
                Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}