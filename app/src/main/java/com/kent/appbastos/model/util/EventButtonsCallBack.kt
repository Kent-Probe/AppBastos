package com.kent.appbastos.model.util

import androidx.appcompat.app.AlertDialog

interface EventButtonsCallBack {
    fun buttonUp(alertDialog: AlertDialog)
    fun buttonDown(alertDialog: AlertDialog)
}