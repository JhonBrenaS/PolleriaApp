package com.example.polleria.utils

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import com.example.polleria.InitBD
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AppConfig : Application(){
    companion object{
        lateinit var CONTEXT:Context
        lateinit var BD: InitBD
        lateinit var databaseReference: DatabaseReference
        lateinit var pd: ProgressDialog
        var IS_ONLINE: Boolean = false
        var BD_NAME="polleria.bd"
        var VERSION=1
    }
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        CONTEXT =applicationContext
        IS_ONLINE = true
        BD=InitBD()
        databaseReference = FirebaseDatabase.getInstance().reference
    }

}