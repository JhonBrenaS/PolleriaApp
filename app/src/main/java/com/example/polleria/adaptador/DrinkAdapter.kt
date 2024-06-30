package com.example.polleria.adaptador

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.polleria.R
import com.example.polleria.entity.Drink

class DrinkAdapter(private var drinksInside: ArrayList<Drink>) : RecyclerView.Adapter<ViewFood>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewFood {
        val vista: View = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return ViewFood(vista)
    }

    override fun getItemCount(): Int = drinksInside.size

    override fun onBindViewHolder(holder: ViewFood, position: Int) {
        val food = drinksInside[position]
        holder.tvNombre.text = food.nombre
        holder.tvDescripcion.text = food.descripcion
        holder.tvPrecio.text = food.precio.toString()
        holder.tvTipo.text = food.tipo
        Glide
            .with(holder.ivFood.context)
            .load(food.imagenUrl)
            .into(holder.ivFood)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(drink: Drink) {
        val indexFood = drinksInside.indexOfFirst { it.idFirebase == drink.idFirebase }
        if (indexFood == -1) {
            drinksInside.add(drink)
        } else {
            drinksInside.removeAt(indexFood)
            drinksInside.add(indexFood, drink)
        }
        notifyDataSetChanged()
    }

    fun setData(drinks: List<Drink>) {
        drinksInside.clear()
        drinksInside.addAll(drinks)
        notifyDataSetChanged()
    }
}
