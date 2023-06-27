package com.kent.appbastos.model.values

import com.kent.appbastos.model.firebase.Inventory

interface CallBackInventory {
    fun onSuccess(inventory: Inventory)
}