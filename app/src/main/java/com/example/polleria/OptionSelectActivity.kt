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

class OptionSelectActivity : CallRemotesActivity() {
    private lateinit var layoutPlatos:LinearLayout
    private lateinit var layoutPedidos:LinearLayout
    private lateinit var layoutBebidas:LinearLayout

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
        //refer
        layoutPedidos = findViewById(R.id.option_pedidos)
        layoutPlatos = findViewById(R.id.optionPlatos)
        layoutBebidas = findViewById(R.id.optionBebida)
        layoutPlatos.setOnClickListener { optionComidas() }
        layoutPedidos.setOnClickListener { optionPedidos() }
        layoutBebidas.setOnClickListener { optionBebidas() }

        AppConfig.pd = ProgressDialog(this)
        cargasDatosRemotosALocal()
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
        var intent=Intent(this,RegisterDrinkActivity::class.java)
        startActivity(intent)
    }

}