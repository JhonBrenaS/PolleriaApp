package com.example.polleria

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.polleria.controller.DrinkController
import com.example.polleria.controller.TypeDrinkController
import com.example.polleria.databinding.ActivityRegisterDrinkBinding
import com.example.polleria.entity.Drink
import com.example.polleria.entity.TypeDrink
import com.example.polleria.utils.AppConfig
import com.example.polleria.utils.AppConfig.Companion.databaseReference
import java.util.Calendar

class RegisterDrinkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterDrinkBinding
    private val drinkController = DrinkController()
    private val typeDrinkController = TypeDrinkController()
    private var typeDrinks: ArrayList<TypeDrink> = arrayListOf(TypeDrink(descripcion = "Seleccione"))

    private var typeDrinkSelect: TypeDrink? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterDrinkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        // Ajustar padding para evitar que el contenido se solape con los insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar listener para el botón de guardar
        binding.btnGrabar.setOnClickListener {
            grabar()
        }
        listarTypesDrink()
    }

    fun listarTypesDrink(){
        typeDrinks.addAll(typeDrinkController.findAll())
        binding.spinnerTipoBebida.setOnItemClickListener { _, _, position, _->
            if(position != 0){
                typeDrinkSelect = typeDrinks[position]
            }
        }
        binding.spinnerTipoBebida.setAdapter(ArrayAdapter(this, android.R.layout.select_dialog_item,typeDrinks.map { it.descripcion }))
    }


    private fun grabar() {
        val nombreBebida = binding.edtNombrePlato.text.toString().trim()
        val descripcion = binding.edtDescripcion.text.toString().trim()
        val tipoBebida = typeDrinkSelect
        val precio = binding.edtPrecio.text.toString().trim()
        val imgUrl = binding.edtImageUrl.text.toString().trim()

        // Validar campos
        if (nombreBebida.isEmpty() || descripcion.isEmpty() || precio.isEmpty()  || imgUrl.isEmpty() || tipoBebida == null) {
            showAlert("Por favor completa todos los campos.")
            return
        }

        // Generar ID único para el nuevo plato

        val drink = Drink(
            nombre = nombreBebida,
            descripcion = descripcion,
            precio = precio.toDouble(),
            tipo = typeDrinkSelect?.descripcion ?: "",
            dateMillis = Calendar.getInstance().timeInMillis,
            imagenUrl = imgUrl)

        // Guardar en Firebase
        if(AppConfig.IS_ONLINE){
            val drinkId = databaseReference.child(ConstantsFirebaseChild.CHILD_DRINKS).push().key ?: return
            drink.idFirebase = drinkId

            databaseReference.child(ConstantsFirebaseChild.CHILD_DRINKS).child(drinkId).setValue(drink)
                .addOnSuccessListener {
                    showAlert("Bebida registrado correctamente.")
                    limpiarCampos()
                }
                .addOnFailureListener { e ->
                    showAlert("Error al registrar plato: ${e.message}")
                }
        }else{
            drink.source = "OFFLINE"
            drinkController.save(drink)
            showAlert("Bebida registrado correctamente.")
            limpiarCampos()
        }
    }


    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Mensaje")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun limpiarCampos() {
        binding.edtNombrePlato.setText("")
        binding.edtDescripcion.setText("")
        binding.edtPrecio.setText("")
        binding.spnMenu.editText?.setText("")
        binding.edtImageUrl.setText("")
    }
}
