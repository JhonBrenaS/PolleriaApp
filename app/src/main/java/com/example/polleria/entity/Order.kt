package com.example.polleria.entity

class Order(
        var cod:Int? = null,
        var plato:String= "",
        var bebidas:String= "",
        var salsa:String= "",
        var entrega:String= "",
        var precioplato: Double,
        var preciobebida: Double,
        var precioentrega: Double,
        var preciototal: Double,
        var foto:String = "")
    {
    }