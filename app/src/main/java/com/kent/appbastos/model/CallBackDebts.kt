package com.kent.appbastos.model

import com.kent.appbastos.model.firebase.Debts

interface CallBackDebts {
    fun onSuccess(debts: Debts)
}