package com.example.polleria

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polleria.databinding.ActivityLoginBinding
import com.example.polleria.utils.showAlertDialog
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Arrays


class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int = 99
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        initClicks()

        //Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        binding.imgGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        //Facebook
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(error: FacebookException) {
                    showAlertDialog(this@LoginActivity, "Error al iniciar con facebook: ${error.localizedMessage}")
                }
            })
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
//
        binding.imgFB.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"));

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(LoginActivity::class.java.simpleName, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(LoginActivity::class.java.simpleName, "Google sign in failed", e)
            }
        }
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(LoginActivity::class.java.simpleName, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LoginActivity::class.java.simpleName, "signInWithCredential:success")
                    openOptionSelectActivity()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LoginActivity::class.java.simpleName, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun loginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
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

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LoginActivity::class.java.simpleName, "signInWithCredential:success")
                    openOptionSelectActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LoginActivity::class.java.simpleName, "signInWithCredential:failure", task.exception)
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