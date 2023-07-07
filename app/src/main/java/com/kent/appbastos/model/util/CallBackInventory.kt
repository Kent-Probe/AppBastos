package com.kent.appbastos.model.util

import com.kent.appbastos.model.firebase.Inventory

interface CallBackInventory {
    fun onSuccess(inventory: Inventory)
}