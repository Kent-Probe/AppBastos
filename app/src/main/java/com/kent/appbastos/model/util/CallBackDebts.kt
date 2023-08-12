package com.kent.appbastos.model.util

import com.kent.appbastos.model.firebase.Debts
import com.kent.appbastos.model.firebase.Receipt

interface CallBackDebts {
    fun onSuccess(debts: Debts)
}

interface CallBackReceipt{
    fun onSuccess(receipt: Receipt)
}