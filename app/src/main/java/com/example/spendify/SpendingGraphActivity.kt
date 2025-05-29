package com.example.spendify

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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

            val sharedPref = getSharedPreferences("goals", Context.MODE_PRIVATE)
            val minGoal = sharedPref.getFloat("minGoal", 0f)
            val maxGoal = sharedPref.getFloat("maxGoal", 0f)

            val stackedEntries = ArrayList<BarEntry>()
            val labels = ArrayList<String>()

            expensesByCategory.forEachIndexed { index, item ->
                val spent = item.totalSpent
                val remainingToMin = (minGoal - spent).coerceAtLeast(0f)
                val remainingToMax = (maxGoal - (spent + remainingToMin)).coerceAtLeast(0f)

                val barValues = floatArrayOf(spent, remainingToMin, remainingToMax)
                stackedEntries.add(BarEntry(index.toFloat(), barValues))
                labels.add(item.categoryName)
            }

            val stackedDataSet = BarDataSet(stackedEntries, "Goals").apply {
                setColors(
                    Color.BLUE,   // Spent
                    Color.GREEN,  // Min Goal portion
                    Color.RED     // Remaining to Max Goal
                )
                stackLabels = arrayOf("Spent", "Min Goal", "Remaining")
            }

            val barData = BarData(stackedDataSet)
            barData.barWidth = 0.2f

            barChart.data = barData

            val xAxis = barChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(true)
            xAxis.granularity = 1f
            xAxis.labelRotationAngle = -45f
            xAxis.labelCount = labels.size

            barChart.axisRight.isEnabled = false
            barChart.axisLeft.axisMinimum = 0f
            barChart.description.isEnabled = false
            barChart.setFitBars(true)
            barChart.invalidate()


            val legend = barChart.legend
            legend.textSize = 14f  // Increase text size
            legend.formSize = 14f  // Increase form/shape size
            legend.isWordWrapEnabled = true  // Wrap if legend items are long

            if (expensesByCategory.isEmpty()) {
                barChart.clear()
                barChart.setNoDataText("No expenses to display in the last 30 days.")
            }
        }
    }


}