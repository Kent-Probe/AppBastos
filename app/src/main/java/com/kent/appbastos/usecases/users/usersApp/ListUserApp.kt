package com.kent.appbastos.usecases.users.usersApp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kent.appbastos.R
import com.kent.appbastos.model.adapter.RecyclerViewAdapterListUsersApp
import com.kent.appbastos.model.alerts.Alerts
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.firebase.UserApp
import com.kent.appbastos.model.util.EventButtonsCallBack
import com.kent.appbastos.model.util.EventCallBackUserApp
import com.kent.appbastos.model.util.Keys

class ListUserApp : AppCompatActivity() {

    private val listUsers:MutableList<UserApp> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user_app)
        val context = this

        //Text view center response
        val response = findViewById<AppCompatTextView>(R.id.response)
        response.visibility = AppCompatTextView.VISIBLE
        response.text = Keys.MSM_LOADING

        //variable recycle view
        val listUserRecyclerView: RecyclerView = findViewById(R.id.listUserAppId)

        //Val button
        val floatButtonBack: FloatingActionButton = findViewById(R.id.exitBtn)

        floatButtonBack.setOnClickListener {
            finish()
        }

        //fill recycler view
        listUsers.clear()
        setupRecyclerView(listUserRecyclerView, context, response)


        //val search view
        val searchView = findViewById<SearchView>(R.id.search_bar)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Acción cuando se presiona el botón de búsqueda
                if(query != null && query.isNotEmpty()){
                    listUsers.clear()
                    setupRecyclerView(listUserRecyclerView, context, response, query)
                }else{
                    listUsers.clear()
                    setupRecyclerView(listUserRecyclerView, context, response)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Acción cuando se cambia el texto del SearchView
                return false
            }
        })


        response.visibility = AppCompatTextView.GONE
        listUserRecyclerView.visibility = RecyclerView.VISIBLE
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, context: Context, textViewResponse: AppCompatTextView, text: String? = null) {
        textViewResponse.visibility = AppCompatTextView.VISIBLE
        recyclerView.visibility = RecyclerView.GONE
        textViewResponse.text = Keys.MSM_LOADING

        val messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listUsers.clear()
                dataSnapshot.children.forEach { child ->
                    val user =
                        UserApp(
                            firstName = child.child(Keys.FIRST_NAME).value.toString(),
                            lastName = child.child(Keys.LAST_NAME).value.toString(),
                            email = child.child(Keys.EMAIL).value.toString(),
                            rol = child.child(Keys.ROL).value.toString(),
                            key = child.key
                        )
                    user.let { listUsers.add(user) }
                }

                //validate text input search view
                if(text != null){
                    val filterList = listUsers.filter {
                        "${it.firstName} ${it.lastName}".contains(text, true)
                    }
                    listUsers.clear()
                    filterList.forEach { listUsers.add(it) }

                    if(filterList.isEmpty()){
                        recyclerView.visibility = RecyclerView.GONE
                        textViewResponse.visibility = AppCompatTextView.VISIBLE
                        textViewResponse.text = Keys.MSM_RESPONSE_NEGATIVE
                    }else{
                        recyclerView.visibility = RecyclerView.VISIBLE
                        textViewResponse.visibility = RecyclerView.GONE
                    }
                }else{
                    recyclerView.visibility = RecyclerView.VISIBLE
                    textViewResponse.visibility = AppCompatTextView.GONE
                }

                //adapter to recycler view
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = RecyclerViewAdapterListUsersApp(listUsers, object :
                    EventCallBackUserApp {
                    override fun onSuccess(
                        userApp: UserApp,
                        holder: RecyclerViewAdapterListUsersApp.ViewHolder
                    ) {
                        Alerts().showAlertSelection(layoutInflater, context,
                        "Desa cambiar su rol de ${userApp.rol} a admin",
                        "Confirmar",
                        "Cancelar",
                        object : EventButtonsCallBack{
                            override fun buttonUp(alertDialog: AlertDialog) {
                                userApp.key?.let { DataBaseShareData().updateRolUserApp(it) }
                                holder.btnChangeRol.isEnabled = false
                                holder.btnChangeRol.visibility = Button.GONE
                                holder.rol.text = Keys.FORMAT_STRING.format("Rol: ", Keys.ROL_ADMIN)
                                alertDialog.dismiss()
                            }

                            override fun buttonDown(alertDialog: AlertDialog) {
                                alertDialog.dismiss()
                            }

                        })

                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ERROR EN LIST USER")
            }
        }

        DataBaseShareData().addEventOnlyListenerUserApp(messagesListener)
    }
}