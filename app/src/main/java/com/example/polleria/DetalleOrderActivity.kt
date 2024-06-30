package com.example.polleria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.polleria.Service.ApiServicePollo
import com.example.polleria.entity.Order
import com.example.polleria.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleOrderActivity : AppCompatActivity() {

    private lateinit var txtDetalleCodigo: TextView
    private lateinit var txtDetallePlato: TextView
    private lateinit var txtDetalleBebida: TextView
    private lateinit var txtDetalleEntrega: TextView
    private lateinit var txtDetallePrecioPlato: TextView
    private lateinit var txtDetallePrecioBebida: TextView
    private lateinit var txtDetallePrecioEntrega: TextView
    private lateinit var txtDetallePrecioTotal: TextView
    private lateinit var imgDetalleFoto: ImageView

    private lateinit var api: ApiServicePollo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_order)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtDetalleCodigo = findViewById(R.id.txtDetalleCodigo)
        txtDetallePlato = findViewById(R.id.txtDetallePlato)
        txtDetalleBebida = findViewById(R.id.txtDetalleBebida)
        txtDetalleEntrega = findViewById(R.id.txtDetalleEntrega)
        txtDetallePrecioPlato = findViewById(R.id.txtDetallePrecioPlato)
        txtDetallePrecioBebida = findViewById(R.id.txtDetallePrecioBebida)
        txtDetallePrecioEntrega = findViewById(R.id.txtDetallePrecioEntrega)
        txtDetallePrecioTotal = findViewById(R.id.txtDetallePrecioTotal)
        imgDetalleFoto = findViewById(R.id.imgDetalleFoto)

        api= ApiUtils.getAPIServicePlato()
        datos()
    }

    private fun volver() {
        val data = Intent(this, ListOrdersActivity::class.java)
        startActivity(data)
    }

    fun datos(){
        //RECUPERAR CLAVES
        var info = intent.extras!!
        //INVOCAR A LA FUNCION FindById
        api.findById(info.getInt("codigo")).enqueue(object: Callback<Order>{
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful){
                    var obj = response.body()!!
                    txtDetalleCodigo.setText(obj.cod.toString())
                    txtDetallePlato.setText(obj.plato)
                    txtDetalleBebida.setText(obj.bebidas)
                    txtDetalleEntrega.setText(obj.entrega)
                    txtDetallePrecioPlato.setText(obj.precioplato.toString())
                    txtDetallePrecioBebida.setText(obj.preciobebida.toString())
                    txtDetallePrecioEntrega.setText(obj.precioentrega.toString())
                    txtDetallePrecioTotal.setText(obj.preciototal.toString())
                    Glide
                        .with(this@DetalleOrderActivity)
                        .load(obj.foto)
                        .into(imgDetalleFoto)
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                showAlertConfirm(t.localizedMessage)
            }

        })

    }

    fun showAlertConfirm(men:String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("SISTEMA")
        builder.setMessage("¿Desea Elimina Al Docente $txtDetalleCodigo")
        builder.setPositiveButton(
            "Aceptar"
        ) { dialog, which -> }
        builder.setNegativeButton(
            android.R.string.cancel
        ) { dialog, which -> }

        val dialog = builder.create()
        dialog.show()
    }

}
