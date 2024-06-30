package com.example.polleria.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.polleria.utils.AppConfig.Companion.pd


fun showAlertDialog(context: Context, men: String, onClick: DialogInterface.OnClickListener? = null )  {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Mensaje!")
    builder.setMessage(men)
    builder.setPositiveButton("Aceptar", onClick)
    val dialog: AlertDialog = builder.create()
    dialog.show()
}

fun showLoadingDialog(men: String){
    pd.setMessage(men)
    pd.show()
}

fun hideLoadingDialog(){
    pd.dismiss()
}