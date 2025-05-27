package com.example.spendify

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Calendar

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var expenseDao: ExpenseDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var categoryList: List<String>

    private lateinit var etAmount: EditText
    private lateinit var etDescription: EditText
    private lateinit var spCategory: Spinner
    private lateinit var btnSaveExpense: Button
    private lateinit var ivPhoto: ImageView
    private var photoUri: String? = null
    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText

    private val REQUEST_IMAGE_PICK = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        db = AppDatabase.getDatabase(this)
        expenseDao = db.expenseDao()
        categoryDao = db.categoryDao()

        etAmount = findViewById(R.id.etAmount)
        etDescription = findViewById(R.id.etDescription)
        spCategory = findViewById(R.id.spCategory)
        btnSaveExpense = findViewById(R.id.btnSaveExpense)
        ivPhoto = findViewById(R.id.ivPhoto)
        startDateEditText = findViewById(R.id.startDateEditText)
        endDateEditText = findViewById(R.id.endDateEditText)

        loadCategories()


        ivPhoto.setOnClickListener {
            openGallery()
        }

        startDateEditText.setOnClickListener { showDatePickerDialog(startDateEditText) }
        endDateEditText.setOnClickListener { showDatePickerDialog(endDateEditText) }

        val expenseId = intent.getIntExtra("expenseId", -1)
        if (expenseId != -1) {
            lifecycleScope.launch {
                val existing = expenseDao.getExpenseById(expenseId)
                if (existing != null) {
                    etAmount.setText(existing.amount.toString())
                    etDescription.setText(existing.description)
                    startDateEditText.setText(existing.startDate)
                    endDateEditText.setText(existing.endDate)
                    // Set spinner category if needed
                }
            }
        }



        btnSaveExpense.setOnClickListener {
            saveExpense()
        }

    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val categories = categoryDao.getAllCategories()
            if (categories.isEmpty()) {
                Toast.makeText(this@AddExpenseActivity, "Please add categories first.", Toast.LENGTH_LONG).show()
                finish()
                return@launch
            }

            categoryList = categories.map { it.name }
            val adapter = ArrayAdapter(this@AddExpenseActivity, android.R.layout.simple_spinner_item, categoryList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spCategory.adapter = adapter
        }
    }



    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            ivPhoto.setImageURI(uri)
            photoUri = uri.toString()
        }
    }

    private fun showDatePickerDialog(target: EditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val date = "${year}-${month + 1}-${dayOfMonth}"
            target.setText(date)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePicker.show()
    }

    private fun saveExpense() {
        val amount = etAmount.text.toString().toDoubleOrNull()
        val description = etDescription.text.toString()
        val startDate = startDateEditText.text.toString()
        val endDate = endDateEditText.text.toString()

        if (spCategory.adapter == null || spCategory.selectedItem == null) {
            Toast.makeText(this, "Please wait for categories to load.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedCategoryName = spCategory.selectedItem.toString()

        if (amount != null) {
            btnSaveExpense.isEnabled = false // ✅ Prevent double tap

            lifecycleScope.launch {
                val categories = categoryDao.getAllCategories()
                val matchedCategory = categories.find { it.name == selectedCategoryName }

                if (matchedCategory != null) {
                    val expense = Expense(
                        amount = amount,
                        description = description,
                        categoryId = matchedCategory.id,
                        photoUri = photoUri,
                        startDate = startDate,
                        endDate = endDate
                    )

                    expenseDao.insertExpense(expense)

                    Toast.makeText(this@AddExpenseActivity, "Expense saved!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddExpenseActivity, "Selected category not found.", Toast.LENGTH_SHORT).show()
                    btnSaveExpense.isEnabled = true // Re-enable on error
                }
            }
        } else {
            Toast.makeText(this, "Please enter a valid amount.", Toast.LENGTH_SHORT).show()
        }
    }
}