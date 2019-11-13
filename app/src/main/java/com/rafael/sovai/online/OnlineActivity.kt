package com.rafael.sovai.online

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.rafael.sovai.R
import java.util.*

class OnlineActivity: AppCompatActivity() {

    var roomKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online)

        createOrEnterRoom()
    }

    private fun createOrEnterRoom() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("rooms")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val roomsSize = snapshot.childrenCount

                if (roomsSize <= 0) {
                    val uuid = UUID.randomUUID().toString()
                    roomKey = uuid

                    myRef.child(roomKey)
                            .push()
                            .setValue("player 1")
                }

                myRef.removeEventListener(this)
                enterRoom()
            }

            override fun onCancelled(error: DatabaseError) {
                println("The read failed: " + error.message)
                myRef.removeEventListener(this)
            }
        })
    }

    private fun enterRoom() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("rooms")

        if (roomKey == "") {
            myRef.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                    val ref = dataSnapshot.ref

                    println(">>> ref: $ref")
                }

                override fun onCancelled(p0: DatabaseError) { }
                override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
                override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
                override fun onChildRemoved(p0: DataSnapshot) { }
            })
        }
    }
}