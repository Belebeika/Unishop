package com.example.unishop.data

import com.google.firebase.database.*

class FirebaseDatabaseHelper {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun readToilets(listener: ValueEventListener) {
        val toiletsRef = database.child("Toilets")
        toiletsRef.addValueEventListener(listener)
    }
}