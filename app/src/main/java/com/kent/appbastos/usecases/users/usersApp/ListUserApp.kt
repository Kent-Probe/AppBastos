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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kent.appbastos.R
import com.kent.appbastos.databinding.ActivityListUserAppBinding
import com.kent.appbastos.model.adapter.RecyclerViewAdapterListUsersApp
import com.kent.appbastos.model.alerts.Alerts
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.firebase.UserApp
import com.kent.appbastos.model.util.EventButtonsCallBack
import com.kent.appbastos.model.util.EventCallBackUserApp
import com.kent.appbastos.model.util.Keys

class ListUserApp : AppCompatActivity() {

    private lateinit var binding: ActivityListUserAppBinding

    private val listUsers:MutableList<UserApp> = ArrayList()
    private var isSudo: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUserAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rol = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).getString(Keys.ROL, null).toString()

        isSudo = rol == Keys.ROL_USER_SUDO

        //toolbar
        binding.toolbar.toolbar.title = "Usuarios registrados"
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.toolbar.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        //Text view center response, hide and show response too
        binding.response.visibility = AppCompatTextView.VISIBLE
        binding.response.text = Keys.MSM_LOADING

        //fill recycler view
        listUsers.clear()
        setupRecyclerView(binding.listUserAppId, this@ListUserApp)


        //val search view
        val searchView = binding.searchBar
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Acción cuando se presiona el botón de búsqueda
                if(query != null && query.isNotEmpty() && query.isNotBlank()){
                    listUsers.clear()
                    setupRecyclerView(binding.listUserAppId, this@ListUserApp, query)
                }else{
                    listUsers.clear()
                    setupRecyclerView(binding.listUserAppId, this@ListUserApp)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Acción cuando se cambia el texto del SearchView
                if(newText != null && (newText.isEmpty() || newText.isBlank())){
                    //fill recycler view
                    listUsers.clear()
                    setupRecyclerView(binding.listUserAppId, this@ListUserApp)
                }
                return true
            }
        })


        binding.response.visibility = AppCompatTextView.GONE
        binding.listUserAppId.visibility = RecyclerView.VISIBLE
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, context: Context, text: String? = null) {
        binding.response.visibility = AppCompatTextView.VISIBLE
        recyclerView.visibility = RecyclerView.GONE
        binding.response.text = Keys.MSM_LOADING

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
                    if(user.rol != Keys.ROL_USER_SUDO) user.let { listUsers.add(user) }
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
                        binding.response.visibility = AppCompatTextView.VISIBLE
                        binding.response.text = Keys.MSM_RESPONSE_NEGATIVE
                    }else{
                        recyclerView.visibility = RecyclerView.VISIBLE
                        binding.response.visibility = RecyclerView.GONE
                    }
                }else{
                    recyclerView.visibility = RecyclerView.VISIBLE
                    binding.response.visibility = AppCompatTextView.GONE
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

                }, isSudo)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ERROR EN LIST USER")
            }
        }

        DataBaseShareData().addEventOnlyListenerUserApp(messagesListener)
    }
}