package com.example.spendify

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val description: String,
    val categoryId: Int,
    val date: Long,
    val photoPath: String? = null
)
