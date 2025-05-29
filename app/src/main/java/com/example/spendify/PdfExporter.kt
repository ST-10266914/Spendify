package com.example.spendify

import android.content.Context
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File

object PdfExporter {

    fun exportExpensesToPdf(context: Context, expenses: List<ExpenseWithCategory>): File {
        val file = File(context.getExternalFilesDir(null), "Expenses_Report.pdf")
        val pdfWriter = PdfWriter(file)
        val pdfDoc = PdfDocument(pdfWriter)
        val document = Document(pdfDoc)

        document.add(Paragraph("Spendify Monthly Expense Report\n\n"))

        for (expense in expenses) {
            document.add(
                Paragraph(
                            "Category: ${expense.category.name}\n" +
                            "Amount: R${expense.expense.amount}\n" +
                            "Note: ${expense.expense.description}\n" +
                            "Start Date: ${expense.expense.startDate}\n\n" +
                            "End Date: ${expense.expense.endDate}\n\n"
                )
            )
        }

        document.close()
        return file
    }
}