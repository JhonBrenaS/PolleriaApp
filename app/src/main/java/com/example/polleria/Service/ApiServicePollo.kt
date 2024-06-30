package com.example.polleria.Service

import com.example.polleria.entity.Order
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServicePollo {
    @GET("/api/pedido")
    fun findAll(): Call<List<Order>>

    @POST("/api/pedido")
    fun save(@Body plato: Order): Call<Order>

    @GET("/api/pedido/{codigo}")
    fun findById(@Path("codigo") codigo: Int): Call<Order>

    @PUT("/api/platos/{codigo}")
    fun update(@Path("codigo") codigo: Int, @Body plato: Order): Call<Order>

    @DELETE("/api/pedido/{codigo}")
    fun deleteById(@Path("codigo") codigo: Int): Call<Void>
}
