package com.example.polleria.controller
import android.content.ContentValues
import com.example.polleria.entity.Drink
import com.example.polleria.utils.AppConfig

class DrinkController {
    companion object{
        const val TABLE_NAME = "tb_bebidas"
    }

    fun findAll(): ArrayList<Drink> {
        val lista = arrayListOf<Drink>()
        val CN = AppConfig.BD.readableDatabase
        val SQL = "SELECT * FROM $TABLE_NAME"
        val RS = CN.rawQuery(SQL, null)

        while (RS.moveToNext()) {
            val bean = Drink(
                idFirebase = RS.getString(1).toString(),          // idFirebase
                source = RS.getString(2),          // source
                nombre = RS.getString(3),       // nombre
                descripcion = RS.getString(4),       // descripcion
                precio = RS.getDouble(5),       // precio
                tipo = RS.getString(6),       // tipo
                dateMillis = RS.getLong(7),         // dateMillis
                imagenUrl = RS.getString(8)        // imagenUrl
            )
            // Adicionar objeto "bean" dentro de lista
            lista.add(bean)
        }

        // Cerrar cursor y base de datos
        RS.close()
        CN.close()

        return lista
    }

    fun findAllOffline(): ArrayList<Drink> {
        val lista = arrayListOf<Drink>()
        val CN = AppConfig.BD.readableDatabase
        val SQL = "SELECT * FROM $TABLE_NAME WHERE SOURCE = 'OFFLINE'"
        val RS = CN.rawQuery(SQL, null)

        while (RS.moveToNext()) {
            val food = Drink(
                idFirebase = RS.getString(1).toString(),
                source = RS.getString(2),
                nombre = RS.getString(3),
                descripcion = RS.getString(4),
                precio = RS.getDouble(5),
                tipo = RS.getString(6),
                dateMillis = RS.getLong(7),
                imagenUrl = RS.getString(8)
            )
            lista.add(food)
        }

        RS.close()
        CN.close()

        return lista
    }

    fun save(bean: Drink): Int {
        var estado: Int
        val CN = AppConfig.BD.writableDatabase
        val conte = ContentValues()
        conte.put("idFirebase",bean.idFirebase)
        conte.put("source",bean.source)
        conte.put("nombre", bean.nombre)
        conte.put("descripcion", bean.descripcion)
        conte.put("precio", bean.precio)
        conte.put("tipo", bean.tipo)
        conte.put("dateMillis", bean.dateMillis)
        conte.put("imagenUrl", bean.imagenUrl)

        estado = CN.insert(TABLE_NAME, null, conte).toInt()

        // Cerrar base de datos
        CN.close()

        return estado
    }

    fun clearTableDrinks() {
        val db = AppConfig.BD.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
        db.close()
    }
}
