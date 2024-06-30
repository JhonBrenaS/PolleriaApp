package com.example.polleria.controller
import android.content.ContentValues
import com.example.polleria.utils.AppConfig
import com.example.polleria.entity.Food

class FoodsController {
    fun findAll(): ArrayList<Food> {
        val lista = arrayListOf<Food>()
        val CN = AppConfig.BD.readableDatabase
        val SQL = "SELECT * FROM tb_platos"
        val RS = CN.rawQuery(SQL, null)

        while (RS.moveToNext()) {
            val bean = Food(
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

    fun findAllOffline(): ArrayList<Food> {
        val lista = arrayListOf<Food>()
        val CN = AppConfig.BD.readableDatabase
        val SQL = "SELECT * FROM tb_platos WHERE SOURCE = 'OFFLINE'"
        val RS = CN.rawQuery(SQL, null)

        while (RS.moveToNext()) {
            val food = Food(
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

    fun save(bean: Food): Int {
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

        estado = CN.insert("tb_platos", null, conte).toInt()

        // Cerrar base de datos
        CN.close()

        return estado
    }

    fun findById(codigo: Int): Food? {
        var bean: Food? = null
        val CN = AppConfig.BD.readableDatabase
        val SQL = "SELECT * FROM tb_platos WHERE id = ?"
        val RS = CN.rawQuery(SQL, arrayOf(codigo.toString()))

        if (RS.moveToNext()) {
            bean = Food(
                RS.getInt(0).toString(),          // id
                RS.getString(1),       // nombre
                RS.getString(2),       // descripcion
                RS.getDouble(3),       // precio
                RS.getString(4),       // tipo
                RS.getLong(5),         // dateMillis
                RS.getString(6)        // imagenUrl
            )
        }

        // Cerrar cursor y base de datos
        RS.close()
        CN.close()

        return bean
    }

    fun update(bean: Food): Int {
        val CN = AppConfig.BD.writableDatabase
        val conte = ContentValues()
        conte.put("nombre", bean.nombre)
        conte.put("descripcion", bean.descripcion)
        conte.put("precio", bean.precio)
        conte.put("tipo", bean.tipo)
        conte.put("date", bean.dateMillis)
        conte.put("img", bean.imagenUrl)

        val estado = CN.update("tb_platos", conte, "id=?", arrayOf(bean.idFirebase.toString()))

        // Cerrar base de datos
        CN.close()

        return estado
    }



    fun clearTableFoods() {
        val db = AppConfig.BD.writableDatabase
        db.execSQL("DELETE FROM tb_platos")
        db.close()
    }
}
