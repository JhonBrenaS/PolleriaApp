package com.example.polleria

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polleria.adaptador.FoodAdapter
import com.example.polleria.controller.FoodsController
import com.example.polleria.databinding.ActivityListFoodMenuBinding
import com.example.polleria.entity.Food
import com.example.polleria.utils.AppConfig.Companion.databaseReference
import com.example.polleria.utils.AppConfig
import com.example.polleria.utils.showAlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ListFoodMenuActivity : CallRemotesActivity() {

    private lateinit var binding: ActivityListFoodMenuBinding
    private lateinit var foodsAdapter: FoodAdapter
    private val foodsController: FoodsController = FoodsController()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListFoodMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        AppConfig.BD.writableDatabase
        binding.btnNuevoPlato.setOnClickListener { nuevo() }

        foodsAdapter = FoodAdapter()
        initFoodsRecyclerView()
        binding.btnSync.setOnClickListener {
            syncFoods()
        }

        binding.edtNombrePlato.addTextChangedListener {
            foodsAdapter.filter.filter(it.toString())
        }
    }

    private fun initFoodsRecyclerView() {
        binding.rvPlato.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvPlato.adapter = foodsAdapter
    }

    private fun nuevo() {
        val intent = Intent(this, RegisterFoodActivity::class.java)
        startActivity(intent)
    }

    private fun listar() {
        databaseReference.child(ConstantsFirebaseChild.CHILD_FOODS).orderByChild(ConstantsFirebaseChild.CHILD_DATE_MILLIS).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodsController.clearTableFoods()
                for (row in snapshot.children) {
                    val bean = row.getValue(Food::class.java)
                    bean?.let {
                        foodsController.save(it)
                    }
                }
                getFoodsDB()
            }

            override fun onCancelled(error: DatabaseError) {
                showAlertDialog(this@ListFoodMenuActivity, error.message)
            }
        })
    }

    private fun syncFoods(){

        val foods = foodsController.findAllOffline()

        for (food in foods){
            val foodId = databaseReference.child(ConstantsFirebaseChild.CHILD_FOODS).push().key ?: return
            food.idFirebase = foodId
            databaseReference.child(ConstantsFirebaseChild.CHILD_FOODS).child(foodId).setValue(food).addOnSuccessListener {
                foodsController.clearTableFoods()
                cargarComidas()
                onResume()
            }
        }

        listar()

    }

    private fun getFoodsDB(){
        foodsAdapter.setData(foodsController.findAll())
    }

    override fun onResume() {
        if(AppConfig.IS_ONLINE){
            listar()
        }else{
            val foods = foodsController.findAll()
            foodsAdapter.setData(foods)
        }

        binding.btnSync.visibility = if (AppConfig.IS_ONLINE) View.GONE else View.VISIBLE
        super.onResume()
    }

}
