package com.example.spendify

import androidx.room.Entity

@Entity
data class SpendingSummary(
    val categoryName: String,
    val totalSpent: Float
)
