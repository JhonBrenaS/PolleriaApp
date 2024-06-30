package com.example.polleria

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polleria.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        initClicks()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser  //Validate if user is logged
        if (currentUser != null) {
            openOptionSelectActivity()
        }
    }

    private fun initClicks(){
        binding.signupRedirectText.setOnClickListener {
            openRegisterUserActivity()
        }
        
        binding.loginButton.setOnClickListener { 
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()
            loginFirebase(email, password)
        }
    }

    private fun loginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LoginActivity::class.java.simpleName, "Login success")
                    auth.currentUser
                    openOptionSelectActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LoginActivity::class.java.simpleName, "Login Failed", task.exception)
                    Toast.makeText(
                        this,
                        "Error al iniciar sesión",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun openRegisterUserActivity() {
        val intent = Intent(this, RegisterUserActivity::class.java)
        startActivity(intent)
    }

    private fun openOptionSelectActivity() {
        val intent = Intent(this, OptionSelectActivity::class.java)
        startActivity(intent)
    }
}