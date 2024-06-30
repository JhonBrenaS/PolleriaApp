package com.example.polleria

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.polleria.databinding.ActivityRegisterUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        auth = Firebase.auth

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    private fun initViews(){

        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupConfirm.text.toString()
            when{
                !email.isValidEmail() -> Toast.makeText(this, "No es un correo valido", Toast.LENGTH_SHORT).show()
                password != confirmPassword -> Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                else -> registerNewUserInFirebase(email, password)
            }
        }
    }

    private fun registerNewUserInFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(RegisterUserActivity::class.java.simpleName, "Success: Usuario nuevo registrado")
                    Toast.makeText(this, "El usuario ha sido registrado con exito.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Log.d(RegisterUserActivity::class.java.simpleName, "Failed: ${task.exception}")
                    Toast.makeText(this, "No se ha podido registrar el usuario", Toast.LENGTH_SHORT,).show()
                }
            }
    }

    private fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

}