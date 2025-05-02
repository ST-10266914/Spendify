package com.example.spendify

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM Expense WHERE id = :expenseId")
    suspend fun getExpenseById(expenseId: Int): Expense?


    @Query("SELECT * FROM Expense WHERE categoryId = :categoryId")
    suspend fun getExpensesByCategory(categoryId: Int): List<Expense>


    @Transaction
    @Query("SELECT * FROM Expense")
    suspend fun getAllExpensesWithCategory(): List<ExpenseWithCategory>

    @Query("""
    SELECT c.name AS categoryName, SUM(e.amount) AS totalAmount
    FROM Expense e
    INNER JOIN Category c ON e.categoryId = c.id
    GROUP BY c.name
""")
    suspend fun getCategorySummary(): List<CategorySummary>
}