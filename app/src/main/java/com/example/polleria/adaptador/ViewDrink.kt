package com.example.polleria.adaptador

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polleria.R

//Añadir  (item:View): RecyclerView.ViewHolder(item) a la clase creada
class ViewDrink(item: View) : RecyclerView.ViewHolder(item) {
    //declarar atributos
    var tvNombre: TextView
    var tvDescripcion: TextView
    var tvPrecio: TextView
    var tvTipo: TextView
    var ivFood: ImageView

    //Bloque init(referenciar)
    init {
        tvNombre = item.findViewById(R.id.tvNombre)
        tvDescripcion = item.findViewById(R.id.tvDescripcion)
        tvPrecio = item.findViewById(R.id.tvPrecio)
        tvTipo = item.findViewById(R.id.tvTipoPlato)
        ivFood = item.findViewById(R.id.imgFoto)
    }
}