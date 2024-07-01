package com.example.polleria

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.polleria.utils.AppConfig
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class OptionSelectActivity : CallRemotesActivity() {
    private lateinit var layoutPlatos:LinearLayout
    private lateinit var layoutPedidos:LinearLayout
    private lateinit var layoutBebidas:LinearLayout
    private lateinit var buttonCerrar:MaterialButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_option_select)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val lmFacebook = LoginManager.getInstance()
        //refer
        layoutPedidos = findViewById(R.id.option_pedidos)
        layoutPlatos = findViewById(R.id.optionPlatos)
        layoutBebidas = findViewById(R.id.optionBebida)
        buttonCerrar = findViewById(R.id.btnCerrarSesion)
        layoutPlatos.setOnClickListener { optionComidas() }
        layoutPedidos.setOnClickListener { optionPedidos() }
        layoutBebidas.setOnClickListener { optionBebidas() }
        buttonCerrar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            mGoogleSignInClient.signOut()
            lmFacebook.logOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        AppConfig.pd = ProgressDialog(this)

        if(AppConfig.IS_ONLINE){
            cargasDatosRemotosALocal()
        }
    }

    fun optionComidas(){
        var intent=Intent(this,ListFoodMenuActivity::class.java)
        startActivity(intent)
    }
    fun optionPedidos(){
        var intent=Intent(this,ListOrdersActivity::class.java)
        startActivity(intent)
    }

    fun optionBebidas(){
        var intent=Intent(this,ListDrinkActivity::class.java)
        startActivity(intent)
    }

}