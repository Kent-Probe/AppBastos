package com.kent.appbastos.usecases.users

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kent.appbastos.R
import com.kent.appbastos.model.EventCallBackSuccess
import com.kent.appbastos.model.adapter.RecyclerViewAdapterListUsers
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.firebase.User

class ListUsers : AppCompatActivity() {

    private val listUsers:MutableList<User> = ArrayList()
    private val database = DataBaseShareData().database

    companion object{
        //Properties
        const val USERNAME = "username"
        const val NUMBER = "number"
        const val NUMBER_DEBTS= "numberDebts"
        const val PHONE = "phone"
        const val KEY = "key"
        const val DEBTS = "debts"

        const val USER = "users"
        const val CLIENT = "clients"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_users)

        //fill recycle view
        val listUserRecyclerView: RecyclerView = findViewById(R.id.listUserId)

        //Val button
        val floatButtonBack: FloatingActionButton = findViewById(R.id.exitBtn)
        val floatBtnAddUser: FloatingActionButton = findViewById(R.id.btnAddUser)

        val isOfCreditSale: Boolean = this.intent.extras?.getBoolean("creditSale") == true
        if(isOfCreditSale){
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
        setupRecyclerView(listUserRecyclerView, this, isOfCreditSale)

    }

    //Function of Recycler View with Database
    private fun setupRecyclerView(recyclerView: RecyclerView, context: Context, isCreditSale: Boolean) {

        val messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listUsers.clear()
                dataSnapshot.children.forEach { child ->
                    val user =
                        User(
                            username = child.child(USERNAME).value.toString(),
                            number = child.child(NUMBER).value.toString(),
                            numberDebts = child.child(NUMBER_DEBTS).value.toString().toFloat(),
                            debts = child.child(DEBTS).value.toString().toFloat(),
                            key = child.key
                        )
                    user.let { listUsers.add(user) }
                }
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = RecyclerViewAdapterListUsers(listUsers, object : EventCallBackSuccess{
                    override fun onSuccess(user: User) {
                        if(isCreditSale){
                            val intent = Intent().apply {
                                putExtra(USERNAME, user.username)
                                putExtra(PHONE, user.number)
                                putExtra(KEY, user.key)
                            }
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled:")
            }
        }
        database.child(USER).child(CLIENT).addListenerForSingleValueEvent(messagesListener)
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