package com.example.polleria.entity

class TypeFood {
    var idFirebase: String = ""
    var descripcion: String = ""

    constructor(idFirebase: String = "", descripcion: String) {
        this.idFirebase = idFirebase
        this.descripcion = descripcion
    }

    constructor()
}