package com.example.polleria

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polleria.adaptador.DrinkAdapter
import com.example.polleria.adaptador.FoodAdapter
import com.example.polleria.controller.DrinkController
import com.example.polleria.controller.FoodsController
import com.example.polleria.databinding.ActivityListDrinkBinding
import com.example.polleria.databinding.ActivityListFoodMenuBinding
import com.example.polleria.entity.Drink
import com.example.polleria.entity.Food
import com.example.polleria.utils.AppConfig.Companion.databaseReference
import com.example.polleria.utils.AppConfig
import com.example.polleria.utils.hideLoadingDialog
import com.example.polleria.utils.showAlertDialog
import com.example.polleria.utils.showLoadingDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

// Se trae las bebidas de firebase y se guarda en SQLite
class ListDrinkActivity : CallRemotesActivity() {

    private lateinit var binding: ActivityListDrinkBinding
    private lateinit var drinkAdapter: DrinkAdapter
    private val drinkController: DrinkController = DrinkController()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDrinkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        AppConfig.BD.writableDatabase
        binding.btnNuevaBebida.setOnClickListener { nuevo() }

        drinkAdapter = DrinkAdapter()
        initFoodsRecyclerView()
        binding.btnSync.setOnClickListener {
            syncFoods()
        }

        binding.edtNombrePlato.addTextChangedListener {
            drinkAdapter.filter.filter(it.toString())
        }

        AppConfig.pd = ProgressDialog(this)
    }

    private fun initFoodsRecyclerView() {
        binding.rvDrink.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvDrink.adapter = drinkAdapter
    }

    private fun nuevo() {
        val intent = Intent(this, RegisterDrinkActivity::class.java)
        startActivity(intent)
    }

    private fun listar() {
        if(AppConfig.IS_ONLINE){
            databaseReference.child(ConstantsFirebaseChild.CHILD_DRINKS).orderByChild(ConstantsFirebaseChild.CHILD_DATE_MILLIS).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    drinkController.clearTableDrinks()
                    for (row in snapshot.children) {
                        val bean = row.getValue(Drink::class.java)
                        if(bean != null){
                            drinkController.save(bean)
                        }
                    }
                    getDrinkDB()
                }

                override fun onCancelled(error: DatabaseError) {
                    showAlertDialog(this@ListDrinkActivity, error.message)
                }
            })
        }else{
            getDrinkDB()
        }
    }

    private fun syncFoods(){
        showLoadingDialog("Sincronizando Bebidas")
        val foods = drinkController.findAllOffline()
        for (food in foods){
            val foodId = databaseReference.child(ConstantsFirebaseChild.CHILD_DRINKS).push().key ?: return
            food.idFirebase = foodId
            databaseReference.child(ConstantsFirebaseChild.CHILD_DRINKS).child(foodId).setValue(food).addOnSuccessListener {
                drinkController.clearTableDrinks()
                hideLoadingDialog()
                cargarBebidasAgain()
                onResume()
            }
        }

        listar()

    }

    private fun getDrinkDB(){
        drinkAdapter.setData(drinkController.findAll())
    }

    fun cargarBebidasAgain(){
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
            }

            override fun onCancelled(error: DatabaseError) {
                hideLoadingDialog()
                showAlertDialog(this@ListDrinkActivity, "Bebidas " + error.message)
            }
        })
    }

    override fun onResume() {
        if(AppConfig.IS_ONLINE){
            listar()
        }else{
            val drinks = drinkController.findAll()
            drinkAdapter.setData(drinks)
        }
        super.onResume()
    }

}
