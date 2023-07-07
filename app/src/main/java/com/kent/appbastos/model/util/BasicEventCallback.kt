package com.kent.appbastos.model.util

import com.google.firebase.database.DataSnapshot

interface BasicEventCallback {
    fun onSuccess(dataSnapshot: DataSnapshot)
    fun onCancel()
    fun databaseFailure()

}