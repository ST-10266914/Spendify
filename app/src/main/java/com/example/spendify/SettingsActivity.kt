package com.example.spendify

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    private lateinit var etMinGoal: EditText
    private lateinit var etMaxGoal: EditText
    private lateinit var btnSaveGoals: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        etMinGoal = findViewById(R.id.etMinGoal)
        etMaxGoal = findViewById(R.id.etMaxGoal)
        btnSaveGoals = findViewById(R.id.btnSaveGoals)

        loadGoals()

        btnSaveGoals.setOnClickListener {
            saveGoals()
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()  // Go back to previous activity
        }
    }

    private fun loadGoals() {
        val sharedPref = getSharedPreferences("goals", Context.MODE_PRIVATE)
        val minGoal = sharedPref.getFloat("minGoal", 0f)
        val maxGoal = sharedPref.getFloat("maxGoal", 0f)
        etMinGoal.setText(minGoal.toString())
        etMaxGoal.setText(maxGoal.toString())
    }

    private fun saveGoals() {
        val minGoal = etMinGoal.text.toString().toFloatOrNull() ?: 0f
        val maxGoal = etMaxGoal.text.toString().toFloatOrNull() ?: 0f

        val sharedPref = getSharedPreferences("goals", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putFloat("minGoal", minGoal)
            putFloat("maxGoal", maxGoal)
            apply()
        }

        Toast.makeText(this, "Goals saved!", Toast.LENGTH_SHORT).show()
    }
}