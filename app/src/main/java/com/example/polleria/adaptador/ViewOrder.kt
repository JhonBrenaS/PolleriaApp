package com.example.polleria.adaptador

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polleria.R

class ViewOrder(item: View): RecyclerView.ViewHolder(item) {

    var tvCodigo: TextView
    var tvPlato: TextView
    var tvPrecio: TextView
    //var tvApellido: TextView
    var imgFoto: ImageView
    var imgEliminar: ImageView
    var layoutCard: LinearLayout
    init{
        layoutCard = item.findViewById(R.id.layoutCard)
        tvCodigo=item.findViewById(R.id.tvCodigo)
        tvPlato=item.findViewById(R.id.tvPlato)
        tvPrecio = item.findViewById(R.id.tvPrecio)
        //tvApellido=item.findViewById(R.id.tvApellido)
        imgFoto=item.findViewById(R.id.imgFoto)
        imgEliminar=item.findViewById(R.id.btnEliminar)
    }
}