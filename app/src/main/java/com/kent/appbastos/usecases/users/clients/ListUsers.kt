package com.kent.appbastos.usecases.users.clients

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.kent.appbastos.model.adapter.RecyclerViewAdapterListUsers
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.firebase.User
import com.kent.appbastos.model.util.EventCallBackSuccess
import com.kent.appbastos.model.util.Keys

class ListUsers : AppCompatActivity() {

    private val listUsers:MutableList<User> = ArrayList()
    private val database = DataBaseShareData().database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_users)
        val context = this

        //Text view center response, hide and show response too
        val response = findViewById<AppCompatTextView>(R.id.response)
        response.visibility = AppCompatTextView.VISIBLE
        response.text = Keys.MSM_LOADING

        //Data share
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val rol = pref.getString(Keys.ROL, null).toString()

        //variable recycle view
        val listUserRecyclerView: RecyclerView = findViewById(R.id.listUserId)

        //Val button
        val floatButtonBack: FloatingActionButton = findViewById(R.id.exitBtn)
        val floatBtnAddUser: FloatingActionButton = findViewById(R.id.btnAddUser)

        //Only admin
        val isOfCreditSale: Boolean = this.intent.extras?.getBoolean(Keys.CREDIT_SALE) == true
        if(isOfCreditSale || rol != Keys.ROL_ADMIN || rol != Keys.ROL_USER_SUDO){
            floatBtnAddUser.visibility = View.GONE
        }

        floatBtnAddUser.setOnClickListener {
            val intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
        }

        floatButtonBack.setOnClickListener {
            finish()
        }
        listUsers.clear()
        setupRecyclerView(listUserRecyclerView, this, isOfCreditSale, response)

        //val search view
        val searchView = findViewById<SearchView>(R.id.search_bar)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Acción cuando se presiona el botón de búsqueda
                if(query != null && query.isNotEmpty() && query.isNotBlank()){
                    listUsers.clear()
                    setupRecyclerView(listUserRecyclerView, context, isOfCreditSale, response, query)
                }else{
                    listUsers.clear()
                    setupRecyclerView(listUserRecyclerView, context, isOfCreditSale, response)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Acción cuando se cambia el texto del SearchView
                if(newText != null && (newText.isEmpty() || newText.isBlank())){
                    //fill recycler view
                    listUsers.clear()
                    setupRecyclerView(listUserRecyclerView, context, isOfCreditSale, response)
                }
                return true
            }
        })

        response.visibility = AppCompatTextView.GONE
        listUserRecyclerView.visibility = RecyclerView.VISIBLE

    }

    //Function of Recycler View with Database
    private fun setupRecyclerView(recyclerView: RecyclerView, context: Context, isCreditSale: Boolean, textViewResponse: AppCompatTextView, text: String? = null) {

        val messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listUsers.clear()
                dataSnapshot.children.forEach { child ->
                    val user =
                        User(
                            username = child.child(Keys.USERNAME).value.toString(),
                            number = child.child(Keys.NUMBER).value.toString(),
                            numberDebts = child.child(Keys.NUMBER_DEBTS).value.toString().toFloat(),
                            debts = child.child(Keys.DEBTS).value.toString().toFloat(),
                            key = child.key
                        )
                    user.let { listUsers.add(user) }
                }

                //validate text input search view
                if(text != null){
                    val filterList = listUsers.filter {
                        it.username!!.contains(text, true)
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

                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = RecyclerViewAdapterListUsers(listUsers, object :
                    EventCallBackSuccess {
                    override fun onSuccess(user: User) {
                        if(isCreditSale){
                            val intent = Intent().apply {
                                putExtra(Keys.USERNAME, user.username)
                                putExtra(Keys.PHONE, user.number)
                                putExtra(Keys.KEY, user.key)
                            }
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ERROR EN LIST USER")
            }
        }
        database.child(Keys.USER).child(Keys.CLIENT).addListenerForSingleValueEvent(messagesListener)
    }

    /*
    //Deslizar item
    private fun deleteSwipe(recyclerView: RecyclerView){
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                listUsers[viewHolder.adapterPosition].username?.let { database.child(it).setValue(null) }
                listUsers.removeAt(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }*/

}