package com.adityaa0108.whatsappclone

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog


class LoadingAlert(private val context: Context) {
        private var dialog: AlertDialog? = null

        fun showLoadingDialog() {
            val builder = AlertDialog.Builder(context)
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_layout, null)
            builder.setView(dialogView)
            builder.setCancelable(false)

            dialog = builder.create()
            dialog?.show()
        }

        fun dismissLoadingDialog() {
            dialog?.dismiss()
        }
    }
