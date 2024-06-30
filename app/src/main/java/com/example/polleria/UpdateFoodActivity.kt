package com.example.polleria

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.polleria.databinding.ActivityUpdateFoodBinding
import com.example.polleria.entity.Food
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class UpdateFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateFoodBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateFoodBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()

        // Ajustar padding para evitar que el contenido se solape con los insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance().reference

        // Obtener el ID del plato desde el intent
        val platoId = intent.getStringExtra("PLATO_ID") ?: return

        // Consultar el plato para llenar los campos
        consultarPlatoPorId(platoId)

        // Configurar listener para el botón de actualizar
        binding.btnGrabarUpdate.setOnClickListener {
            actualizarPlato(platoId)
        }

        // Configurar listener para el botón de eliminar
        binding.btnCerrarUpdate.setOnClickListener {
            eliminarPlato(platoId)
        }
    }

    private fun actualizarPlato(id: String) {
        // Leer controles
        val nombrePlato = binding.edtNombrePlatoUpdate.text.toString().trim()
        val descripcion = binding.edtDescripcionUpdate.text.toString().trim()
        val precio = binding.edtPrecioUpdate.text.toString().trim()
        val tipoPlato = binding.spnMenuUpdate.editText?.text.toString()
        val imgUrl = binding.edtImageUrlUpdate.text.toString().trim()

        // Validar campos
        if (nombrePlato.isEmpty() || descripcion.isEmpty() || precio.isEmpty() || tipoPlato.isEmpty() || imgUrl.isEmpty()) {
            showAlert("Por favor completa todos los campos.")
            return
        }

        val food = Food(id, nombrePlato, descripcion, precio.toDouble(), tipoPlato, Calendar.getInstance().timeInMillis, imgUrl)

        // Actualizar el plato en Firebase
        database.child(ConstantsFirebaseChild.CHILD_FOODS).child(id).setValue(food)
            .addOnSuccessListener {
                showAlert("Plato modificado correctamente.")
                limpiarCampos()
            }
            .addOnFailureListener { e ->
                showAlert("Error al modificar el plato: ${e.message}")
            }
    }

    private fun eliminarPlato(platoId: String) {
        database.child(ConstantsFirebaseChild.CHILD_FOODS).child(platoId).removeValue()
            .addOnSuccessListener {
                showAlert("Plato eliminado correctamente.")
                limpiarCampos()
                finish()  // Volver a la actividad anterior
            }
            .addOnFailureListener { e ->
                showAlert("Error al eliminar el plato: ${e.message}")
            }
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Mensaje")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar") { dialog, which -> finish() }
        val dialog = builder.create()
        dialog.show()
    }

    private fun limpiarCampos() {
        binding.edtNombrePlatoUpdate.setText("")
        binding.edtDescripcionUpdate.setText("")
        binding.edtPrecioUpdate.setText("")
        binding.edtImageUrlUpdate.setText("")
    }

    private fun consultarPlatoPorId(platoId: String) {
        database.child(ConstantsFirebaseChild.CHILD_FOODS).child(platoId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val food = snapshot.getValue(Food::class.java)
                        // Aquí puedes manejar el plato obtenido, por ejemplo, mostrarlo en vistas
                        binding.edtNombrePlatoUpdate.setText(food?.nombre)
                        binding.edtDescripcionUpdate.setText(food?.descripcion)
                        binding.edtPrecioUpdate.setText(food?.precio.toString())
                        binding.edtImageUrlUpdate.setText(food?.imagenUrl)
                        binding.edtTipo.setText(food?.tipo, false)
                    } else {
                        showAlert("No se encontró el plato con ID: $platoId")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showAlert(error.message)
                }
            })
    }
}
