package com.kent.appbastos.usecases.users

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kent.appbastos.R
import com.kent.appbastos.model.adapter.RecyclerViewAdapter
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.firebase.User

class ListUsers : AppCompatActivity() {

    private lateinit var messagesListener: ValueEventListener
    private val listUsers:MutableList<User> = ArrayList()
    private val database = DataBaseShareData().database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_users)

        //fill recycle view
        val listUserRecyclerView: RecyclerView = findViewById(R.id.listUserId)

        //Val button
        val floatButtonBack: FloatingActionButton = findViewById(R.id.exitBtn)

        floatButtonBack.setOnClickListener {
            finish()
        }
        listUsers.clear()
        setupRecyclerView(listUserRecyclerView, this)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, context: Context) {

        messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listUsers.clear()
                dataSnapshot.children.forEach { child ->
                    val user =
                        User(
                            username = child.child("username").value.toString(),
                            number = child.child("number").value.toString(),
                            numberDebts = child.child("numberDebts").value.toString().toFloat(),
                            debts = child.child("debts").value.toString().toFloat()
                        )
                    user.let { listUsers.add(user) }
                }
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = RecyclerViewAdapter(listUsers, context)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled:")
            }
        }
        database.child("users").addValueEventListener(messagesListener)
    }

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
    }

}