package com.kent.appbastos.model

import com.google.firebase.database.DataSnapshot

interface BasicEventCallback {
    fun onSuccess(dataSnapshot: DataSnapshot)
    fun onCancel()
    fun databaseFailure()

}