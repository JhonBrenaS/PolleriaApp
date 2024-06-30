package com.example.polleria

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.polleria.utils.AppConfig

class InitBD: SQLiteOpenHelper(AppConfig.CONTEXT, AppConfig.BD_NAME, null, AppConfig.VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Creating tb_platos table
        db.execSQL(
            "CREATE TABLE tb_platos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idFirebase VARCHAR(150), " +
                    "source VARCHAR(50), " +
                    "nombre VARCHAR(100), " +
                    "descripcion TEXT, " +
                    "precio REAL, " +
                    "tipo VARCHAR(50), " +
                    "dateMillis BIGINT, " +
                    "imagenUrl TEXT)"
        )

        db.execSQL(
            "CREATE TABLE tb_bebidas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idFirebase VARCHAR(150), " +
                    "source VARCHAR(50), " +
                    "nombre VARCHAR(100), " +
                    "descripcion TEXT, " +
                    "precio REAL, " +
                    "tipo VARCHAR(50), " +
                    "dateMillis BIGINT, " +
                    "imagenUrl TEXT)"
        )


        // Creating tb_pollo table
        db.execSQL(
            "CREATE TABLE tb_pedidos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "plato VARCHAR(30), " +
                    "bebidas VARCHAR(30), " +
                    "salsa VARCHAR(30), " +
                    "entrega VARCHAR(30), " +
                    "precioplato INTEGER, " +
                    "preciobebida INTEGER, " +
                    "precioentrega INTEGER, " +
                    "preciototal INTEGER, " +
                    "foto VARCHAR(30))"
        )

        db.execSQL(
            "CREATE TABLE tb_tipo_plato (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idFirebase VARCHAR(100), " +
                    "descripcion VARCHAR(50)) "
        )

        db.execSQL(
            "CREATE TABLE tb_tipo_bebida (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idFirebase VARCHAR(100), " +
                    "descripcion VARCHAR(50)) "
        )

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Implement database schema upgrade logic if needed
    }
}
