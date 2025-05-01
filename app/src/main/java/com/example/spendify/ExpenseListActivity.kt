package com.example.spendify

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ExpenseListActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var expenseDao: ExpenseDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var listView: ListView
    private lateinit var adapter: ExpenseListAdapter
    private var expenseList = mutableListOf<ExpenseWithCategory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list)

        db = AppDatabase.getDatabase(this)
        expenseDao = db.expenseDao()
        categoryDao = db.categoryDao()
        listView = findViewById(R.id.expenseListView)

        adapter = ExpenseListAdapter(this, expenseList,
            onEdit = { expense -> navigateToEdit(expense) },
            onDelete = { expense -> confirmDelete(expense) }
        )
        listView.adapter = adapter

        loadExpenses()
    }

    private fun loadExpenses() {
        lifecycleScope.launch {
            expenseList.clear()
            expenseList.addAll(expenseDao.getAllExpensesWithCategory())
            adapter.notifyDataSetChanged()
        }
    }

    private fun navigateToEdit(expense: ExpenseWithCategory) {
        val intent = Intent(this, AddExpenseActivity::class.java).apply {
            putExtra("expenseId", expense.expense.id)
        }
        startActivity(intent)
    }

    private fun confirmDelete(expense: ExpenseWithCategory) {
        AlertDialog.Builder(this)
            .setTitle("Delete Expense")
            .setMessage("Are you sure you want to delete this expense?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    expenseDao.deleteExpense(expense.expense)
                    loadExpenses()
                    Toast.makeText(this@ExpenseListActivity, "Expense deleted", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadExpenses()
    }
}