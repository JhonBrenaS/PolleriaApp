package com.example.polleria.adaptador

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.polleria.R
import com.example.polleria.UpdateFoodActivity
import com.example.polleria.entity.Food

class FoodAdapter() : RecyclerView.Adapter<ViewFood>(), Filterable {

    var foodsList: ArrayList<Food> = ArrayList()
    var foodsListFiltered: ArrayList<Food> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewFood {
        val vista: View = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return ViewFood(vista)
    }

    override fun getItemCount(): Int = foodsListFiltered.size

    override fun onBindViewHolder(holder: ViewFood, position: Int) {
        val food = foodsListFiltered[position]
        holder.tvNombre.text = food.nombre
        holder.tvDescripcion.text = food.descripcion
        holder.tvPrecio.text = food.precio.toString()
        holder.tvTipo.text = food.tipo
        Glide
            .with(holder.ivFood.context)
            .load(food.imagenUrl)
            .into(holder.ivFood)

        // Configurar el click listener para cada item
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateFoodActivity::class.java).apply {
                putExtra("PLATO_ID", food.idFirebase)
            }
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(food: Food) {
        val indexFood = foodsList.indexOfFirst { it.idFirebase == food.idFirebase }
        if (indexFood == -1) {
            foodsList.add(food)
        } else {
            foodsList.removeAt(indexFood)
            foodsList.add(indexFood, food)
        }
        notifyDataSetChanged()
    }

    fun setData(foods: List<Food>) {
        foodsList.clear()
        foodsList.addAll(foods)
        foodsListFiltered.addAll(foods)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) foodsListFiltered = foodsList else {
                    val filteredList = ArrayList<Food>()
                    foodsList
                        .filter {
                            (it.nombre.contains(constraint!!, true)) or (it.descripcion.contains(constraint, true))

                        }
                        .forEach { filteredList.add(it) }
                    foodsListFiltered = filteredList

                }
                return FilterResults().apply { values = foodsListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                foodsListFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Food>
                notifyDataSetChanged()
            }
        }
    }
}
