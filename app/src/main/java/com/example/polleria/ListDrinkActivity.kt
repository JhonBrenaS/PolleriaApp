package com.example.polleria

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polleria.adaptador.DrinkAdapter
import com.example.polleria.controller.DrinkController
import com.example.polleria.databinding.ActivityListDrinkBinding
import com.example.polleria.entity.Drink
import com.example.polleria.utils.AppConfig.Companion.databaseReference
import com.example.polleria.utils.AppConfig
import com.example.polleria.utils.showAlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ListDrinkActivity : CallRemotesActivity() {

    private lateinit var binding: ActivityListDrinkBinding
    private lateinit var drinksAdapter: DrinkAdapter
    private val drinkController: DrinkController = DrinkController()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDrinkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        AppConfig.BD.writableDatabase
        binding.btnNuevaBebida.setOnClickListener { nuevo() }

        drinksAdapter = DrinkAdapter(arrayListOf())
        initFoodsRecyclerView()
        binding.btnSync.setOnClickListener {
            syncFoods()
        }
    }

    private fun initFoodsRecyclerView() {
        binding.rvDrink.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvDrink.adapter = drinksAdapter
    }

    private fun nuevo() {
        val intent = Intent(this, RegisterDrinkActivity::class.java)
        startActivity(intent)
    }

    private fun listar() {
        databaseReference.child(ConstantsFirebaseChild.CHILD_DRINKS).orderByChild(ConstantsFirebaseChild.CHILD_DATE_MILLIS).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                drinkController.clearTableDrinks()
                for (row in snapshot.children) {
                    val bean = row.getValue(Drink::class.java)
                    bean?.let {
                        drinkController.save(it)
                    }
                }
                getFoodsDB()
            }

            override fun onCancelled(error: DatabaseError) {
                showAlertDialog(this@ListDrinkActivity, error.message)
            }
        })
    }

    private fun syncFoods(){

        val drinksOffline = drinkController.findAllOffline()

        for (drink in drinksOffline){
            val foodId = databaseReference.child(ConstantsFirebaseChild.CHILD_DRINKS).push().key ?: return
            drink.idFirebase = foodId
            databaseReference.child(ConstantsFirebaseChild.CHILD_FOODS).child(foodId).setValue(drink).addOnSuccessListener {
                drinkController.clearTableDrinks()
                cargarComidas()
                onResume()
            }
        }

        listar()

    }

    private fun getFoodsDB(){
        drinksAdapter.setData(drinkController.findAll())
    }

    override fun onResume() {
        if(AppConfig.IS_ONLINE){
            listar()
        }else{
            val foods = drinkController.findAll()
            drinksAdapter.setData(foods)
        }

        binding.btnSync.visibility = if (AppConfig.IS_ONLINE) View.GONE else View.VISIBLE
        super.onResume()
    }

}
