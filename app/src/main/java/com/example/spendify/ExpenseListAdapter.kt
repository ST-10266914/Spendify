package com.example.spendify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class ExpenseListAdapter(context: Context,
                         private val expenseList: List<ExpenseWithCategory>,
                         private val onEdit: (ExpenseWithCategory) -> Unit,
                         private val onDelete: (ExpenseWithCategory) -> Unit
): ArrayAdapter<ExpenseWithCategory>(context, R.layout.expense_list_item, expenseList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.expense_list_item, parent, false)

        val expense = expenseList[position]

        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        val amountTextView = view.findViewById<TextView>(R.id.amountTextView)
        val categoryTextView = view.findViewById<TextView>(R.id.categoryTextView)
        val editButton = view.findViewById<Button>(R.id.editButton)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)


        amountTextView.text = "$${expense.expense.amount}"
        categoryTextView.text = expense.categoryName

        editButton.setOnClickListener { onEdit(expense) }
        deleteButton.setOnClickListener { onDelete(expense) }

        return view
    }
}