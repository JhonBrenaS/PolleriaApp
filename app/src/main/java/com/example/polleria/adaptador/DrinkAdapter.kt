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
import com.example.polleria.entity.Drink
import com.example.polleria.entity.Food

class DrinkAdapter() : RecyclerView.Adapter<ViewDrink>(), Filterable {

    var drinkList: ArrayList<Drink> = ArrayList()
    var drinkListFitered: ArrayList<Drink> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewDrink {
        val vista: View = LayoutInflater.from(parent.context).inflate(R.layout.item_drink, parent, false)
        return ViewDrink(vista)
    }

    override fun getItemCount(): Int = drinkListFitered.size

    override fun onBindViewHolder(holder: ViewDrink, position: Int) {
        val drink = drinkListFitered[position]
        holder.tvNombre.text = drink.nombre
        holder.tvDescripcion.text = drink.descripcion
        holder.tvPrecio.text = drink.precio.toString()
        holder.tvTipo.text = drink.tipo
        Glide
            .with(holder.ivFood.context)
            .load(drink.imagenUrl)
            .into(holder.ivFood)

        // Configurar el click listener para cada item
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateFoodActivity::class.java).apply {
                putExtra("PLATO_ID", drink.idFirebase)
            }
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(drink: Drink) {
        val indexFood = drinkList.indexOfFirst { it.idFirebase == drink.idFirebase }
        if (indexFood == -1) {
            drinkList.add(drink)
        } else {
            drinkList.removeAt(indexFood)
            drinkList.add(indexFood, drink)
        }
        notifyDataSetChanged()
    }

    fun setData(drink: List<Drink>) {
        drinkList.clear()
        drinkList.addAll(drink)
        drinkListFitered.addAll(drink)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) drinkListFitered = drinkList else {
                    val filteredList = ArrayList<Drink>()
                    drinkList
                        .filter {
                            (it.nombre.contains(constraint!!, true))

                        }
                        .forEach { filteredList.add(it) }
                    drinkListFitered = filteredList

                }
                return FilterResults().apply { values = drinkListFitered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                drinkListFitered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Drink>
                notifyDataSetChanged()
            }
        }
    }
}
