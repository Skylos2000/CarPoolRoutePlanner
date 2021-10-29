package com.devs.carpoolrouteplanner


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.AccountSignIn.Companion.creds
import com.devs.carpoolrouteplanner.utils.ApiService
//import com.devs.carpoolrouteplanner.utils.LoginResult
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.viewmodals.LoginViewModal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import com.facebook.*
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.util.*

@Suppress("DEPRECATION")

class AccountSignIn : AppCompatActivity() {

    // companion object that will hold the user credentials and can be accessed from anywhere
    companion object {
        val creds = arrayOf("", "")
    }
    //facebook Start
    var callbackManager: CallbackManager?=null
    private val fbemail = "email"
    //facebook end

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accountsignin)
        var email = findViewById(R.id.email) as EditText
        var password = findViewById(R.id.password) as EditText
        var progressBar = findViewById(R.id.progressBar) as ProgressBar
        val button: Button = findViewById(R.id.button)
        val button2: Button = findViewById(R.id.createAccButton)
        val intent = Intent(this@AccountSignIn, MainMenu::class.java)
        val intent2 = Intent(this@AccountSignIn, CreateAccount::class.java)
        val loginViewModel: LoginViewModal = ViewModelProvider(this).get(LoginViewModal::class.java)

//facebook Start
        FacebookSdk.sdkInitialize(applicationContext)

        val loginButton = findViewById<LoginButton>(R.id.loginButton)
        loginButton.setOnClickListener {
            callbackManager = CallbackManager.Factory.create()

            loginButton.setReadPermissions(listOf(fbemail))
            LoginManager.getInstance().logInWithReadPermissions(this, listOf(fbemail))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.d("MainActivity", "Facebook token: " + loginResult!!.accessToken.token)
                    startActivity(Intent(applicationContext, MainMenu::class.java))
                }

                override fun onCancel() {
                    Log.d("MainActivity", "Facebook onCancel.")
                }

                override fun onError(exception: FacebookException) {
                    Log.d("MainActivity", "Facebook onError.")
                }
            })
        }

        val accessToken = AccessToken.getCurrentAccessToken()
        accessToken != null && !accessToken.isExpired
//facebook end

            progressBar.visibility = View.GONE
            val context = this

            //startActivity(intent)

            loginViewModel.loginResult.observe(this, {
                val loginResult = it
                Toast.makeText(this, loginResult.message, Toast.LENGTH_SHORT).show()
                if (loginResult.success) {
                    //TODO save the loginResult.value somewhere for access
                    creds[0] = loginResult.user.username;
                    creds[1] = loginResult.user.password;
                    with(this.getSharedPreferences("login_details", Context.MODE_PRIVATE).edit()) {
                        putString("uid", loginResult.user.uid)
                        putString("username", loginResult.user.username)
                        putString("password", loginResult.user.password)
                        apply()
                    }
                    startActivity(intent)
                    finish()
                }
                progressBar.visibility = View.GONE
                button.isClickable = true
            })

            button.setOnClickListener {
                val username = email.getText()
                val code = password.getText()
                val apiUrl = getConfigValue("backend_url");
                //startActivity(intent)
                creds[0] = username.toString()
                creds[1] = code.toString()

                if (!username.toString().equals("") && !code.toString().equals("")) {
                    progressBar.visibility = View.VISIBLE
                    button.isClickable = false
                    lifecycleScope.launch {
                        apiUrl?.let {
                            loginViewModel.login(it, username.toString(), code.toString())

                        }

                    }
                } else {
                    Toast.makeText(this, "One or two fields are empty", Toast.LENGTH_SHORT).show()
                }
            }
            button2.setOnClickListener {
                startActivity(intent2)
            }

        }
//facebook Start
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
//facebook end
    }