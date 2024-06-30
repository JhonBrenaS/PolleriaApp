package com.example.polleria

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.polleria.controller.FoodsController
import com.example.polleria.controller.TypeFoodController
import com.example.polleria.databinding.ActivityRegisterFoodBinding
import com.example.polleria.entity.Food
import com.example.polleria.entity.TypeFood
import com.example.polleria.utils.AppConfig
import com.example.polleria.utils.AppConfig.Companion.databaseReference
import com.example.polleria.utils.showAlertDialog
import java.util.Calendar

class RegisterFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterFoodBinding
    private var typeFoods: ArrayList<TypeFood> = arrayListOf(TypeFood(descripcion = "Seleccione"))
    private val foodsController = FoodsController()
    private val typeFoodsController = TypeFoodController()
    private var typeFoodSelect: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterFoodBinding.inflate(layoutInflater)
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

        listarTypeFoods()
    }

    private fun grabar() {
        val nombrePlato = binding.edtNombrePlato.text.toString().trim()
        val descripcion = binding.edtDescripcion.text.toString().trim()
        val precio = binding.edtPrecio.text.toString().trim()
        val imgUrl = binding.edtImageUrl.text.toString().trim()

        // Validar campos
        if (typeFoodSelect == null || nombrePlato.isEmpty() || descripcion.isEmpty() || precio.isEmpty() || imgUrl.isEmpty()) {
            showAlertDialog(this, "Por favor completa todos los campos.")
            return
        }

        // Generar ID único para el nuevo plato

        val food = Food(
            nombre = nombrePlato,
            descripcion = descripcion,
            precio = precio.toDouble(),
            tipo = typeFoodSelect!!,
            dateMillis = Calendar.getInstance().timeInMillis,
            imagenUrl = imgUrl)

        // Guardar en Firebase
        if(AppConfig.IS_ONLINE){
            listarTypeFoods()
            val platoId = databaseReference.child(ConstantsFirebaseChild.CHILD_FOODS).push().key ?: return
            food.idFirebase = platoId

            databaseReference.child(ConstantsFirebaseChild.CHILD_FOODS).child(platoId).setValue(food)
                .addOnSuccessListener {
                    showAlertDialog(this@RegisterFoodActivity, "Plato registrado correctamente.")
                    limpiarCampos()
                }
                .addOnFailureListener { e ->
                    showAlertDialog(this@RegisterFoodActivity, "Error al registrar plato: ${e.message}")
                }
        }else{
            food.source = "OFFLINE"
            foodsController.save(food)
            showAlertDialog(this@RegisterFoodActivity, "Plato registrado correctamente. Guardado localmente.[SQLite]")
            limpiarCampos()
        }
    }


    fun listarTypeFoods(){
        typeFoods.addAll(typeFoodsController.findAll())
        binding.spnTipoPlato.setOnItemClickListener { _, _, position, _->
            if(position != 0){
                typeFoodSelect = typeFoods[position].descripcion
            }
        }
        binding.spnTipoPlato.setAdapter(ArrayAdapter(this@RegisterFoodActivity, android.R.layout.select_dialog_item,typeFoods.map { it.descripcion }))
    }


    private fun limpiarCampos() {
        binding.edtNombrePlato.setText("")
        binding.edtDescripcion.setText("")
        binding.edtPrecio.setText("")
        binding.spnTipoPlato.setText("", false)
        binding.edtImageUrl.setText("")
    }
}
