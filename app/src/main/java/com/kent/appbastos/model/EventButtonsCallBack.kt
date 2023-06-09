package com.kent.appbastos.model

import androidx.appcompat.app.AlertDialog

interface EventButtonsCallBack {
    fun buttonUp(alertDialog: AlertDialog)
    fun buttonDown(alertDialog: AlertDialog)
}