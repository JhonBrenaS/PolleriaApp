package com.example.polleria.utils

import com.example.polleria.Service.ApiServicePollo

class ApiUtils {
    companion object {
        val BASE_URL = "https://6680405356c2c76b495b9734.mockapi.io/"

        fun getAPIServicePlato(): ApiServicePollo {
            return RetrofitClient.getClient(BASE_URL).create(ApiServicePollo::class.java)
        }
    }
}
