package com.example.spendify

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class ExpenseWithCategory(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category
)
