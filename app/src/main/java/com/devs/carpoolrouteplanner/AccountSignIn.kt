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
import com.devs.carpoolrouteplanner.utils.ApiService
import com.devs.carpoolrouteplanner.utils.LoginResult
import com.devs.carpoolrouteplanner.viewmodals.LoginViewModal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountSignIn : AppCompatActivity() {
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

        progressBar.visibility = View.GONE
        val context = this

        loginViewModel.loginResult.observe(this, {
            val loginResult = it
            Toast.makeText(this, loginResult.message, Toast.LENGTH_SHORT).show()
            if (loginResult.success) {
                startActivity(intent)
                //TODO save the loginResult.value somewhere for access
                with(this.getPreferences(Context.MODE_PRIVATE).edit()){
                    putString("uid",loginResult.user.uid)
                    apply()
                }
                finish()
            }
            progressBar.visibility= View.GONE
            button.isClickable = true
        })

        button.setOnClickListener {
            val username = email.getText()
            val code = password.getText()
            //startActivity(intent)

            if (!username.toString().equals("") && !code.toString().equals("")) {
                progressBar.visibility = View.VISIBLE
                button.isClickable = false
                lifecycleScope.launch{
                    loginViewModel.login(username.toString(), code.toString())
                }
            } else {
                Toast.makeText(this, "One or two fields are empty", Toast.LENGTH_SHORT).show()
            }
        }
        button2.setOnClickListener{
            startActivity(intent2)
        }


    }
}