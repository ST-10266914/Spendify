package com.example.spendify

import androidx.room.Entity

@Entity
data class CategorySummary(
    val categoryName: String,
    val totalAmount: Double,
)
