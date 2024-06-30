package com.example.polleria.entity

class Drink {
    var id: Int = 0
    var idFirebase: String = ""
    var nombre: String = ""
    var descripcion: String = ""
    var precio: Double = 0.0
    var tipo: String = ""
    var source: String = "ONLINE"
    var dateMillis: Long = 0L
    var imagenUrl: String = ""

    constructor(id: Int = 0, idFirebase: String = "",nombre: String, descripcion: String, precio: Double, tipo: String,dateMillis: Long, imagenUrl: String = "",source: String = "ONLINE") {
        this.id = id
        this.idFirebase = idFirebase
        this.nombre = nombre
        this.descripcion = descripcion
        this.precio = precio
        this.tipo = tipo
        this.imagenUrl = imagenUrl
        this.dateMillis = dateMillis
        this.source = source
    }

    constructor()

}