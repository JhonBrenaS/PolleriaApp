package com.example.polleria

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.polleria.controller.FoodsController
import com.example.polleria.Service.ApiServicePollo
import com.example.polleria.entity.Drink
import com.example.polleria.entity.Food
import com.example.polleria.entity.Order
import com.example.polleria.utils.ApiUtils
import com.example.polleria.utils.AppConfig.Companion.databaseReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterOrderActivity : AppCompatActivity() {

    private lateinit var spnPlatoFondo: AutoCompleteTextView
    private lateinit var spnBebidas: AutoCompleteTextView
    private lateinit var chcMayonesa: CheckBox
    private lateinit var chcMostaza: CheckBox
    private lateinit var chcKetchup: CheckBox
    private lateinit var chcAjiCasa: CheckBox

    private lateinit var rdGeneral: RadioGroup
    private lateinit var rdDelivery: RadioButton
    private lateinit var rdTienda: RadioButton

    private lateinit var txtPlato: TextView
    private lateinit var txtBebida: TextView
    private lateinit var txtEntrega: TextView
    private lateinit var txtTotal: TextView

    private lateinit var btnGrabar: Button
    private lateinit var btnVolver: Button
    private lateinit var btnCalcular: Button

    private lateinit var api: ApiServicePollo

    private var foodsController = FoodsController()
    private var foods: ArrayList<Food> = arrayListOf()
    private var drinks: ArrayList<Drink> = arrayListOf()

    var precioFood: Double = 0.0
    var precioDrink: Double = 0.0
    var totalPagar: Double = 0.0
    var entrega: Int = 0

    private var foodsSelected: Food? = null
    private var drinksSelected: Drink? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_order)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        spnPlatoFondo = findViewById(R.id.spnPlatoFondo)
        spnBebidas = findViewById(R.id.spnBebidas)
        chcMayonesa = findViewById(R.id.chcMayonesa)
        chcMostaza = findViewById(R.id.chcMostaza)
        chcKetchup = findViewById(R.id.chcKetchup)
        chcAjiCasa = findViewById(R.id.chcAjiCasa)
        rdDelivery = findViewById(R.id.rdDelivery)
        rdTienda = findViewById(R.id.rdTienda)

        txtPlato = findViewById(R.id.txtPlato)
        txtBebida = findViewById(R.id.txtBebida)
        txtEntrega = findViewById(R.id.txtEntrega)
        txtTotal = findViewById(R.id.txtTotal)

        api = ApiUtils.getAPIServicePlato()
        btnGrabar = findViewById(R.id.btnGrabar)
        btnVolver = findViewById(R.id.btnVolver)
        btnCalcular = findViewById(R.id.btnCalcular)

        btnCalcular.setOnClickListener { calcular() }
        btnGrabar.setOnClickListener { grabar() }
        btnVolver.setOnClickListener { volver() }
        spnPlatoFondo.setText("Seleccione", false)
        spnBebidas.setText("Seleccione", false)

        spnPlatoFondo.setOnItemClickListener { _, _, position, _ ->
            foodsSelected = foods[position]
        }

        spnBebidas.setOnItemClickListener { _, _, position, _ ->
            drinksSelected = drinks[position]
        }

        listarFoods()
        listarDrinks()
    }

    fun listarFoods(){
        databaseReference.child(ConstantsFirebaseChild.CHILD_FOODS).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (row in snapshot.children) {
                    val bean = row.getValue(Food::class.java)
                    bean?.let {
                        foods.add(it)
                    }
                }
                spnPlatoFondo.setAdapter(ArrayAdapter(this@RegisterOrderActivity, android.R.layout.select_dialog_item,foods.map { it.nombre }))
            }

            override fun onCancelled(error: DatabaseError) {
                showAlert(error.message)
            }
        })
    }

    fun listarDrinks(){
        databaseReference.child(ConstantsFirebaseChild.CHILD_DRINKS).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (row in snapshot.children) {
                    val bean = row.getValue(Drink::class.java)
                    bean?.let {
                        drinks.add(it)
                    }
                }
                spnBebidas.setAdapter(ArrayAdapter(this@RegisterOrderActivity, android.R.layout.select_dialog_item,drinks.map { "${it.tipo} - ${it.nombre}"}))
            }

            override fun onCancelled(error: DatabaseError) {
                showAlert(error.message)
            }
        })
    }

    fun calcular() {
        var platos = foodsSelected?.nombre
        var bebidas = drinksSelected?.nombre
        if (platos.isNullOrEmpty()  || bebidas.isNullOrEmpty()) {
            Toast.makeText(this, "Faltan datos por ingresar o seleccionar", Toast.LENGTH_SHORT).show()
            return
        }
        precioFood = foodsSelected?.precio ?: 0.0
        precioDrink = drinksSelected?.precio ?: 0.0
        entrega = if (rdDelivery.isChecked) 5 else 0


        totalPagar = precioFood + precioDrink + entrega

        txtPlato.text = "S/.$precioFood"
        txtBebida.text = "S/.$precioDrink"
        txtEntrega.text = "S/.$entrega"
        txtTotal.text = "S/.$totalPagar"
    }

    fun volver() {
        finish()
    }

    fun grabar() {
        val platos = spnPlatoFondo.text.toString()
        val bebidas = spnBebidas.text.toString()
        val precioplatos = txtPlato.text.toString().replace("S/.", "").trim().toDouble()
        val preciobebida = txtBebida.text.toString().replace("S/.", "").trim().toDouble()
        val precioentrega = txtEntrega.text.toString().replace("S/.", "").trim().toDouble()
        val preciototal = txtTotal.text.toString().replace("S/.", "").trim().toDouble()

        if (platos.isEmpty() || bebidas.isEmpty()) {
            mostrarAlerta("Faltan datos por ingresar")
            return
        }

        val entrega = when {
            rdDelivery.isChecked -> "Delivery"
            rdTienda.isChecked -> "Recoge en Tienda"
            else -> {
                mostrarAlerta("Debes seleccionar la entrega")
                return
            }
        }

        val salsa = mutableListOf<String>().apply {
            if (chcMayonesa.isChecked) add("Mayonesa")
            if (chcMostaza.isChecked) add("Mostaza")
            if (chcKetchup.isChecked) add("Ketchup")
            if (chcAjiCasa.isChecked) add("Aji de la Casa")
        }.joinToString(", ")

        val bean = Order(
            plato = platos,
            bebidas = bebidas,
            salsa = salsa,
            entrega = entrega,
            precioplato = precioplatos,
            preciobebida = preciobebida,
            precioentrega = precioentrega,
            preciototal = preciototal,
            foto = foodsSelected?.imagenUrl ?: "")

        api.save(bean).enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
                    showAlert("Pedido Registrado")
                } else {
                    showAlert("Error en el registro de Pedido")
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                showAlert("Error: ${t.localizedMessage}")
            }
        })
    }

    fun showAlert(men: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exito")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun mostrarAlerta(mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
