package com.example.polleria.entity

class Food {
    var idFirebase: String = ""
    var nombre: String = ""
    var descripcion: String = ""
    var precio: Double = 0.0
    var tipo: String = ""
    var source: String = "ONLINE"
    var dateMillis: Long = 0L
    var imagenUrl: String = ""

    constructor(idFirebase: String = "",nombre: String, descripcion: String, precio: Double, tipo: String,dateMillis: Long, imagenUrl: String = "",source: String = "ONLINE") {
        this.nombre = nombre
        this.descripcion = descripcion
        this.precio = precio
        this.tipo = tipo
        this.imagenUrl = imagenUrl
        this.idFirebase = idFirebase
        this.dateMillis = dateMillis
        this.source = source
    }

    constructor()
}