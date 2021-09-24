package com.devs.carpoolrouteplanner


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.viewmodals.LoginViewModal
import kotlinx.coroutines.launch

class AccountSignIn : AppCompatActivity() {

    // companion object that will hold the user credentials and can be accessed from anywhere
    companion object {
        val creds = arrayOf("", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accountsignin)
        val email: EditText = findViewById(R.id.signIn_emailTextBox)
        val password: EditText = findViewById(R.id.signIn_passwordTextBox)
        val progressBar: ProgressBar = findViewById(R.id.signInProgressBar)
        val signInButton: Button = findViewById(R.id.signInButton)
        val createAccountButton: Button = findViewById(R.id.createAccountButton)
        val mainMenuIntent = Intent(this, MainMenu::class.java)
        val createAccountIntent = Intent(this, CreateAccount::class.java)
        val loginViewModel = ViewModelProvider(this).get(LoginViewModal::class.java)

        progressBar.visibility = View.GONE

        loginViewModel.loginResult.observe(this, {
            val loginResult = it
            Toast.makeText(this, loginResult.message, Toast.LENGTH_SHORT).show()
            if (loginResult.success) {
                //TODO save the loginResult.value somewhere for access
                creds[0] = loginResult.user.username
                creds[1] = loginResult.user.password
                with(this.getSharedPreferences("login_details",Context.MODE_PRIVATE).edit()) {
                    putString("uid", loginResult.user.uid)
                    putString("username",loginResult.user.username)
                    putString("password",loginResult.user.password)
                    apply()
                }
                startActivity(mainMenuIntent)
                finish()
            }
            progressBar.visibility = View.GONE
            signInButton.isClickable = true
        })

        signInButton.setOnClickListener {
            val username = email.text
            val code = password.text
            val apiUrl = getConfigValue("backend_url")
            //startActivity(intent)
            creds[0] = username.toString()
            creds[1] = code.toString()

            if (username.toString() != "" && code.toString() != "") {
                progressBar.visibility = View.VISIBLE
                signInButton.isClickable = false
                lifecycleScope.launch {
                    apiUrl?.let {
                        loginViewModel.login(it, username.toString(), code.toString())

                    }

                }
            } else {
                Toast.makeText(this, "One or two fields are empty", Toast.LENGTH_SHORT).show()
            }
        }
        createAccountButton.setOnClickListener {
            startActivity(createAccountIntent)
        }


    }
}