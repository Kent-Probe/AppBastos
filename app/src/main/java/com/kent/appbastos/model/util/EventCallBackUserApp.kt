package com.kent.appbastos.model.util

import com.kent.appbastos.model.adapter.RecyclerViewAdapterListUsersApp
import com.kent.appbastos.model.firebase.UserApp

interface EventCallBackUserApp {
    fun onSuccess(userApp: UserApp, holder: RecyclerViewAdapterListUsersApp.ViewHolder){

    }
}