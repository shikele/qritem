package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var signInButton: Button
    private lateinit var loginEmailTextEdit: TextInputEditText
    private lateinit var loginPasswordTextEdit: TextInputEditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progressCircle: CircularProgressIndicator
    private lateinit var emailLayout : TextInputLayout
    private lateinit var passwordLayout : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signInButton = findViewById(R.id.sign_in_button)
        loginEmailTextEdit = findViewById(R.id.email)
        loginPasswordTextEdit = findViewById(R.id.password)
        progressCircle = findViewById(R.id.progress_circle)
        emailLayout = findViewById(R.id.email_text_layout)
        passwordLayout = findViewById(R.id.password_text_layout)
        progressCircle.hide()
        auth = Firebase.auth

        signInButton.setOnClickListener() {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = loginEmailTextEdit.text.toString()
        val password = loginPasswordTextEdit.text.toString()
        clearFocus()
        clearError()


        if (email.isBlank()){

            emailLayout.error = "Email cannot be empty"
            emailLayout.requestFocus()
        } else if (password.isBlank()) {

            passwordLayout.error = "password cannot be empty"
            passwordLayout.requestFocus()
        } else {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(){ task ->

                progressCircle.show()

                if (task.isSuccessful) {

                    val user = auth.currentUser
                    progressCircle.hide()
                    if (user != null) {
                        Snackbar.make(passwordLayout, "Welcome " +user.email, Snackbar.LENGTH_LONG)
                            .show()

                    }
                    Log.d("debug", "here")
                    startActivity(Intent(this, MainActivity::class.java))


                } else {
                    progressCircle.hide()
                    Snackbar.make(passwordLayout, "Please check user name or Password", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.parseColor("#9B59B6"))
                        .setAnchorView(signInButton)
                        .show()
                    emailLayout.requestFocus()
                    passwordLayout.requestFocus()

                }
            }
        }
    }

    private fun clearFocus () {
        emailLayout.clearFocus()
        passwordLayout.clearFocus()
    }

    private fun clearError () {
        emailLayout.error = null
        passwordLayout.error = null
    }
}