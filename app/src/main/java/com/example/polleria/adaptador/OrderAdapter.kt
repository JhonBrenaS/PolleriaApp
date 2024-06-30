package com.example.polleria.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.polleria.R
import com.example.polleria.entity.Order

class OrderAdapter(var lista: List<Order>, private val onClickUpdate: (Int) -> Unit, private val onClickDelete:(Int) -> Unit):RecyclerView.Adapter<ViewOrder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewOrder {
        var vista= LayoutInflater.from(parent.context).inflate(R.layout.item_order,parent,false)
        return ViewOrder(vista)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewOrder, position: Int) {

        val order = lista[position]

        holder.tvCodigo.setText(order.cod.toString())
        holder.tvPlato.setText(order.plato)
        holder.tvPrecio.setText(order.precioplato.toString())

        Glide
            .with(holder.itemView.context)
            .load(order.foto)
            .into(holder.imgFoto)

        holder.layoutCard.setOnClickListener {
            onClickUpdate.invoke(order.cod ?: 0)
        }

        holder.imgEliminar.setOnClickListener {
            onClickDelete.invoke(order.cod ?: 0)
        }
    }
}
