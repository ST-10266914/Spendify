package com.example.spendify

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
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

}