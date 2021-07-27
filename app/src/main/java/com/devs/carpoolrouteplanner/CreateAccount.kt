package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.viewmodals.LoginViewModal
import kotlinx.coroutines.launch

class CreateAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        var username = findViewById(R.id.username) as EditText
        var email = findViewById(R.id.email) as EditText
        var password = findViewById(R.id.password) as EditText
        var progressBar = findViewById(R.id.progressBar) as ProgressBar
        val button: Button = findViewById(R.id.submitButton)
        val intent = Intent(this@CreateAccount, AccountSignIn::class.java)


        progressBar.visibility = View.GONE



        button.setOnClickListener {
            val username = username.getText()
            val email = email.getText()
            val code = password.getText()
            if (!username.toString().equals("") && !email.toString().equals("") && !code.toString().equals("")) {
                progressBar.visibility = View.VISIBLE
                button.isClickable = false
                lifecycleScope.launch {
                    //TODO send info to backend
                    //if (success) {
                    startActivity(intent)
                    finish()
                    progressBar.visibility= View.GONE
                    button.isClickable = true
                }
                Toast.makeText(this, "Account Created Successfully, Please log in.", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}