package com.kent.appbastos.model

interface BasicEventCallback {
    fun onSuccess()
    fun onCancel()
    fun databaseFailure()

}