package com.kent.appbastos.model.util

import com.kent.appbastos.model.firebase.User

interface EventCallBackSuccess {
    fun onSuccess(user: User)
}