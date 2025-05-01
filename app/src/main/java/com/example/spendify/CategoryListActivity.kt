package com.example.spendify

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CategoryListActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var categoryListView: ListView
    private lateinit var etNewCategory: EditText
    private lateinit var btnAddCategory: Button
    private var categoryList = mutableListOf<Category>()
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        db = AppDatabase.getDatabase(this)
        categoryDao = db.categoryDao()

        categoryListView = findViewById(R.id.categoryListView)
        etNewCategory = findViewById(R.id.etNewCategory)
        btnAddCategory = findViewById(R.id.btnAddCategory)

        adapter = CategoryAdapter(this, categoryList,
            onEdit = { category -> showEditDialog(category) },
            onDelete = { category -> deleteCategory(category) }
        )
        categoryListView.adapter = adapter

        loadCategories()

        btnAddCategory.setOnClickListener {
            val categoryName = etNewCategory.text.toString().trim()
            if (categoryName.isNotEmpty()) {
                lifecycleScope.launch {
                    categoryDao.insertCategory(Category(name = categoryName))
                    etNewCategory.text.clear()
                    loadCategories()
                }
            }
        }
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val all = categoryDao.getAllCategories()
            categoryList.clear()
            categoryList.addAll(all)
            adapter.notifyDataSetChanged()
        }
    }

    private fun showEditDialog(category: Category) {
        val editText = EditText(this)
        editText.setText(category.name)

        AlertDialog.Builder(this)
            .setTitle("Edit Category")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    lifecycleScope.launch {
                        categoryDao.updateCategory(category.copy(name = newName))
                        loadCategories()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteCategory(category: Category) {
        AlertDialog.Builder(this)
            .setTitle("Delete Category")
            .setMessage("Are you sure you want to delete '${category.name}'?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    categoryDao.deleteCategory(category)
                    loadCategories()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}