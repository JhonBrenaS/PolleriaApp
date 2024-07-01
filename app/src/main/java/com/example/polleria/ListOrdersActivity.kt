package com.example.polleria

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polleria.Service.ApiServicePollo
import com.example.polleria.adaptador.OrderAdapter
import com.example.polleria.entity.Order
import com.example.polleria.utils.ApiUtils
import com.example.polleria.utils.AppConfig
import com.example.polleria.utils.hideLoadingDialog
import com.example.polleria.utils.showAlertDialog
import com.example.polleria.utils.showLoadingDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListOrdersActivity : AppCompatActivity() {

    private lateinit var rvPedidos: RecyclerView
    private lateinit var btnAgregarPedido: Button
    private lateinit var apiPedido: ApiServicePollo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_orders)

        rvPedidos = findViewById(R.id.rvPedidos)
        btnAgregarPedido = findViewById(R.id.btnAgregarPollo)
        btnAgregarPedido.setOnClickListener { nuevo() }
        apiPedido = ApiUtils.getAPIServicePlato()
        AppConfig.pd = ProgressDialog(this)
    }

    private fun listarPedidos() {
        showLoadingDialog("Cargando pedidos")
        apiPedido.findAll().enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    hideLoadingDialog()
                    var data = response.body()
                    var adaptador = OrderAdapter(data!!, {
                        var intent = Intent(this@ListOrdersActivity, DetalleOrderActivity::class.java)
                        intent.putExtra("codigo", it)
                        startActivity(intent)
                    }, {
                        eliminarPedido(it)
                    })
                    rvPedidos.adapter = adaptador
                    rvPedidos.layoutManager = LinearLayoutManager(this@ListOrdersActivity)
                } else {
                    hideLoadingDialog()
                }
            }
            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                hideLoadingDialog()
                showAlertDialog(this@ListOrdersActivity, t.localizedMessage)
            }
        })
    }

    private fun eliminarPedido(codigo: Int) {
        showLoadingDialog("Eliminando pedido")
        apiPedido.deleteById(codigo).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful){
                    hideLoadingDialog()
                    showAlertDialog(this@ListOrdersActivity, "Eliminado correctamente"){ dialog, _ ->
                        dialog.dismiss()
                        listarPedidos()
                    }
                }else{
                    hideLoadingDialog()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                hideLoadingDialog()
                showAlertDialog(this@ListOrdersActivity, t.localizedMessage)
            }

        })
    }

    private fun nuevo() {
        val intent = Intent(this, RegisterOrderActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        listarPedidos()
    }
}
