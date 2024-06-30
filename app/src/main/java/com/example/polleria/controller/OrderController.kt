package com.example.polleria.controller

import android.content.ContentValues
import com.example.polleria.entity.Order
import com.example.polleria.utils.AppConfig


class OrderController {
    fun findAll():ArrayList<Order>{
        var lista=ArrayList<Order>()
        var CN=AppConfig.BD.readableDatabase
        var SQL="select * from tb_pollo"
        var RS=CN.rawQuery(SQL,null)
        while(RS.moveToNext())
        {
            var bean=Order(RS.getInt(0),
                RS.getString(1),
                RS.getString(2),
                RS.getString(3),
                RS.getString(4),
                RS.getDouble(5),
                RS.getDouble(6),
                RS.getDouble(7),
                RS.getDouble(8),
                RS.getString(9)
                )
            lista.add(bean)
        }
        return lista
    }

    fun save(bean:Order):Int{
        var estado=-1
        var CN=AppConfig.BD.writableDatabase
        var conte= ContentValues()
        conte.put("plato",bean.plato)
        conte.put("bebidas",bean.bebidas)
        conte.put("salsa",bean.salsa)
        conte.put("entrega",bean.entrega)
        conte.put("precioplato",bean.precioplato)
        conte.put("preciobebida",bean.preciobebida)
        conte.put("precioentrega",bean.precioentrega)
        conte.put("preciototal",bean.preciototal)
        conte.put("foto",bean.foto)
        estado=CN.insert("tb_pollo","cod",conte).toInt()
        return estado

    }

    fun findById(codigo:Int):Order{
        lateinit var bean:Order
        var CN=AppConfig.BD.readableDatabase
        var SQL="select * from tb_pollo where cod=?"
        var RS=CN.rawQuery(SQL, arrayOf(codigo.toString()))
        if(RS.moveToNext()){
            bean=Order(RS.getInt(0),
                RS.getString(1),
                RS.getString(2),
                RS.getString(3),
                RS.getString(4),
                RS.getDouble(5),
                RS.getDouble(6),
                RS.getDouble(7),
                RS.getDouble(8),
                RS.getString(9)
            )
        }
        return bean
    }
}