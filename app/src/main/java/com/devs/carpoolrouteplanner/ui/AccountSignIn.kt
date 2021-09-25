package com.devs.carpoolrouteplanner.ui


import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.CreateAccount
import com.devs.carpoolrouteplanner.R
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.viewmodals.LoginViewModel
import kotlinx.coroutines.launch

class AccountSignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accountsignin)

        val usernameTextBox = findViewById<EditText>(R.id.signIn_emailTextBox)
        val passwordTextBox = findViewById<EditText>(R.id.signIn_passwordTextBox)
        val progressBar = findViewById<ProgressBar>(R.id.signInProgressBar)
        val signInButton = findViewById<Button>(R.id.signInButton)
        val createAccountButton = findViewById<Button>(R.id.createAccountButton)

        val mainActivityIntent = Intent(this, MainActivity::class.java)
        val createAccountIntent = Intent(this, CreateAccount::class.java)

        val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        progressBar.visibility = View.GONE

        loginViewModel.loginResult.observe(this, { loginResult ->
            // Display a success message
            // TODO: More accurate errors in cases where the backend is down, the creds are wrong
            Toast.makeText(
                this,
                "Login ${if (loginResult.success) "successful" else "failed"}",
                Toast.LENGTH_SHORT
            ).show()


            if (loginResult.success && loginResult.user != null) {
                // Store credentials
                // TODO: Should credentials be stored like this?
                with(this.getSharedPreferences("login_details",Context.MODE_PRIVATE).edit()) {
                    putInt("uid", loginResult.user.uid)
                    putString("username", loginResult.user.username)
                    // putString("password", loginResult.user.password)
                    apply()
                }

                // Start the main activity
                startActivity(mainActivityIntent)
                finish()
            }

            // TODO: Should this be above the main activity start?
            progressBar.visibility = View.GONE
            signInButton.isClickable = true
        })

        signInButton.setOnClickListener {
            val username = usernameTextBox.text
            val code = passwordTextBox.text
            val apiUrl = getConfigValue("backend_url")
                ?: throw Resources.NotFoundException("backend_url resource could not be found. Have you set it in the dev_config.properties file?")

            if (username.toString() != "" && code.toString() != "") {
                progressBar.visibility = View.VISIBLE
                signInButton.isClickable = false
                lifecycleScope.launch {
                    loginViewModel.login(apiUrl, username.toString(), code.toString())
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