package com.example.spendify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class CategoryAdapter(private val context: Context,
                      private val categories: MutableList<Category>,
                      private val onEdit: (Category) -> Unit,
                      private val onDelete: (Category) -> Unit
): ArrayAdapter<Category>(context, 0, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_category, parent, false)

        val category = categories[position]

        val tvCategoryName = view.findViewById<TextView>(R.id.tvCategoryName)
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)

        tvCategoryName.text = category.name

        btnEdit.setOnClickListener { onEdit(category) }
        btnDelete.setOnClickListener { onDelete(category) }

        return view
    }
}