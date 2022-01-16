package com.example.myapplication.dataClass
import com.google.firebase.Timestamp

data class ItemClass (
    val name:String = "",
    val count: Int = 0,
    val best_before: Timestamp = Timestamp.now()
)