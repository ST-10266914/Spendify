package com.example.spendify

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CategorySummaryActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var expenseDao: ExpenseDao


    private lateinit var btnViewSummary: Button
    private lateinit var tvSummaryResults: TextView

    private var startDate: String = ""
    private var endDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_summary)

        db = AppDatabase.getDatabase(this)
        expenseDao = db.expenseDao()


        btnViewSummary = findViewById(R.id.btnViewSummary)
        tvSummaryResults = findViewById(R.id.tvSummaryResults)



        btnViewSummary.setOnClickListener {
            viewSummary()
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()  // Go back to previous activity
        }
    }


    private fun viewSummary() {
        lifecycleScope.launch {
            val summary: List<CategorySummary> = expenseDao.getCategorySummary()
            val builder = StringBuilder()
            for (item in summary) {
                builder.append("Category: ${item.categoryName}\nTotal Spent: ${item.totalAmount}\n\n")
            }
            tvSummaryResults.text = builder.toString()
        }
    }
}