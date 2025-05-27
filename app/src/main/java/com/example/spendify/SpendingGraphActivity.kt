package com.example.spendify

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SpendingGraphActivity : AppCompatActivity() {
    private lateinit var barChart: BarChart
    private lateinit var expenseDao: ExpenseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_graph)

        barChart = findViewById(R.id.barChart)
        expenseDao = AppDatabase.getDatabase(this).expenseDao()

        showSpendingGraph()
    }

    private fun showSpendingGraph() {
        lifecycleScope.launch {
            val expensesByCategory = expenseDao.getTotalSpentPerCategoryLastMonth()

            // Load global min/max goals from SharedPreferences
            val sharedPref = getSharedPreferences("goals", Context.MODE_PRIVATE)
            val minGoal = sharedPref.getFloat("minGoal", 0f)
            val maxGoal = sharedPref.getFloat("maxGoal", 0f)

            val entries = ArrayList<BarEntry>()
            val minGoalEntries = ArrayList<BarEntry>()
            val maxGoalEntries = ArrayList<BarEntry>()
            val labels = ArrayList<String>()

            expensesByCategory.forEachIndexed { index, item ->
                val categoryName = item.categoryName
                val spent = item.totalSpent

                entries.add(BarEntry(index.toFloat(), spent))
                minGoalEntries.add(BarEntry(index.toFloat(), minGoal))
                maxGoalEntries.add(BarEntry(index.toFloat(), maxGoal))
                labels.add(categoryName)
            }

            val spentDataSet = BarDataSet(entries, "Amount Spent").apply {
                color = Color.BLUE
            }

            val minDataSet = BarDataSet(minGoalEntries, "Min Goal").apply {
                color = Color.GREEN
            }

            val maxDataSet = BarDataSet(maxGoalEntries, "Max Goal").apply {
                color = Color.RED
            }

            val barData = BarData(spentDataSet, minDataSet, maxDataSet)
            barChart.data = barData

            val xAxis = barChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f

            barChart.description.isEnabled = false
            barChart.invalidate()
        }
    }
}