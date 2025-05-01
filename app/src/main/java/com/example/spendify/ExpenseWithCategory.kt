package com.example.spendify

import androidx.room.Entity

@Entity
data class ExpenseWithCategory(
    val expense: Expense,
    val categoryName: String
)
