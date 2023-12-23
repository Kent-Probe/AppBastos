package com.kent.appbastos.model.values

data class LogSale(
    val ref: String,
    val nameClient: String,
    val amount: Int,
    val valueTotal: Float,
    val valueUnit: Float,
    val typeSale: String,
    val key: String? = null
    // add other data important..
    )
