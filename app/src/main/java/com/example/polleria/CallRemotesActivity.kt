package com.example.polleria

import androidx.appcompat.app.AppCompatActivity
import com.example.polleria.controller.DrinkController
import com.example.polleria.controller.FoodsController
import com.example.polleria.controller.TypeDrinkController
import com.example.polleria.controller.TypeFoodController
import com.example.polleria.entity.Drink
import com.example.polleria.entity.Food
import com.example.polleria.entity.TypeDrink
import com.example.polleria.entity.TypeFood
import com.example.polleria.utils.AppConfig
import com.example.polleria.utils.hideLoadingDialog
import com.example.polleria.utils.showAlertDialog
import com.example.polleria.utils.showLoadingDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

open class CallRemotesActivity: AppCompatActivity() {

    private val typeDrinkController = TypeDrinkController()
    private val typeFoodController = TypeFoodController()
    private val drinkController = DrinkController()
    private val foodsController = FoodsController()

    fun cargasDatosRemotosALocal(){
        cargarTipoBebidas()
    }

    fun limpiarTodosLosDatos(){

    }

    fun cargarTipoBebidas(){
        showLoadingDialog("Cargando datos de Firebase")
        AppConfig.databaseReference.child(ConstantsFirebaseChild.CHILD_TYPE_DRINKS).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                typeDrinkController.clearTableTypeDrinks()
                for (row in snapshot.children) {
                    val bean = row.getValue(TypeDrink::class.java)
                    bean?.let {
                        typeDrinkController.save(it)
                    }
                }
                cargarTipoComidas()
            }

            override fun onCancelled(error: DatabaseError) {
                hideLoadingDialog()
                showAlertDialog(this@CallRemotesActivity, "Tipo Bebidas " + error.message)
            }
        })
    }

    fun cargarTipoComidas(){
        AppConfig.databaseReference.child(ConstantsFirebaseChild.CHILD_TYPE_FOODS).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                typeFoodController.clearTableTypeFood()
                for (row in snapshot.children) {
                    val bean = row.getValue(TypeFood::class.java)
                    bean?.let {
                        typeFoodController.save(it)
                    }
                }
                cargarBebidas()
            }

            override fun onCancelled(error: DatabaseError) {
                hideLoadingDialog()
                showAlertDialog(this@CallRemotesActivity, "Tipo Comidas " + error.message)
            }
        })
    }

    fun cargarBebidas(){
        AppConfig.databaseReference.child(ConstantsFirebaseChild.CHILD_DRINKS).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                drinkController.clearTableDrinks()
                for (row in snapshot.children) {
                    val bean = row.getValue(Drink::class.java)
                    bean?.let {
                        drinkController.save(it)
                    }
                }
                cargarComidas()
            }

            override fun onCancelled(error: DatabaseError) {
                hideLoadingDialog()
                showAlertDialog(this@CallRemotesActivity, "Bebidas " + error.message)
            }
        })
    }

    fun cargarComidas(){
        AppConfig.databaseReference.child(ConstantsFirebaseChild.CHILD_FOODS).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodsController.clearTableFoods()
                for (row in snapshot.children) {
                    val bean = row.getValue(Food::class.java)
                    bean?.let {
                        foodsController.save(it)
                    }
                }
                hideLoadingDialog()
            }

            override fun onCancelled(error: DatabaseError) {
                hideLoadingDialog()
                showAlertDialog(this@CallRemotesActivity, "Comidas " + error.message)
            }
        })
    }
}