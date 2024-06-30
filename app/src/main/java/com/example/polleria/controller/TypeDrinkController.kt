package com.example.polleria.controller
import android.content.ContentValues
import com.example.polleria.utils.AppConfig
import com.example.polleria.entity.TypeDrink

class TypeDrinkController {

    companion object{
        const val NOMBRE_TABLA = "tb_tipo_bebida"
    }

    fun findAll(): ArrayList<TypeDrink> {
        val lista = arrayListOf<TypeDrink>()
        val CN = AppConfig.BD.readableDatabase
        val SQL = "SELECT * FROM $NOMBRE_TABLA"
        val RS = CN.rawQuery(SQL, null)

        while (RS.moveToNext()) {
            val bean = TypeDrink(
                idFirebase = RS.getString(1),          // idFirebase
                descripcion = RS.getString(2)         // descripcion
            )
            // Adicionar objeto "bean" dentro de lista
            lista.add(bean)
        }

        // Cerrar cursor y base de datos
        RS.close()
        CN.close()

        return lista
    }

    fun save(bean: TypeDrink): Int {
        var estado: Int
        val CN = AppConfig.BD.writableDatabase
        val conte = ContentValues()
        conte.put("idFirebase",bean.idFirebase)
        conte.put("descripcion",bean.descripcion)

        estado = CN.insert(NOMBRE_TABLA, null, conte).toInt()

        // Cerrar base de datos
        CN.close()

        return estado
    }




    fun clearTableTypeDrinks() {
        val db = AppConfig.BD.writableDatabase
        db.execSQL("DELETE FROM $NOMBRE_TABLA")
        db.close()
    }
}
