package com.example.polleria.utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        fun getClient(URL:String):Retrofit {
            val retrofit = Retrofit.Builder()
                //.baseUrl("https://jsonplaceholder.typicode.com")
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }
    }
}