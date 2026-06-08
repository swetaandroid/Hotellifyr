package com.xongolab.hotellifyr.utils

import android.text.Editable
import android.text.TextWatcher
import java.util.Calendar

class ExpiryDateFormatTextWatcher : TextWatcher {

    private var isFormatting: Boolean = false
    private var deleteChar: Boolean = false
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100 // Get the last two digits of the current year

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Check if a character is being deleted
        if (count > 0 && after == 0) {
            deleteChar = true
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // No action required here
    }

    override fun afterTextChanged(s: Editable?) {
        if (isFormatting || deleteChar) {
            deleteChar = false
            return
        }

        isFormatting = true

        val formatted = formatExpiryDate(s.toString())
        s?.replace(0, s.length, formatted)

        isFormatting = false
    }

    private fun formatExpiryDate(expiryDate: String): String {
        // Remove all non-digit characters
        val digitsOnly = expiryDate.replace("\\D".toRegex(), "")

        // Return if the string is empty
        if (digitsOnly.isEmpty()) {
            return ""
        }

        // Ensure the first two digits are between 01 and 12 (valid month)
        val month = if (digitsOnly.length >= 2) digitsOnly.substring(0, 2) else digitsOnly
        val monthInt = month.toIntOrNull()

        if (monthInt == null || monthInt < 1 || monthInt > 12) {
            // Invalid month, keep only the first digit if it is valid
            val validMonth = if (monthInt == null) "" else month.substring(0, 1)
            return validMonth
        }

        // Ensure the last two digits of the year represent a year greater than or equal to the current year
        val year = if (digitsOnly.length >= 4) digitsOnly.substring(2, 4) else ""
        val yearInt = year.toIntOrNull()

        if (yearInt != null && yearInt < currentYear) {
            // Invalid year, keep only the valid part
            return "$month/"
        }

        // Format the string as MM/YY
        return when {
            digitsOnly.length >= 4 -> "${digitsOnly.substring(0, 2)}/${digitsOnly.substring(2, 4)}"
            digitsOnly.length >= 3 -> "${digitsOnly.substring(0, 2)}/${digitsOnly.substring(2)}"
            digitsOnly.length >= 2 -> "${digitsOnly.substring(0, 2)}/"
            else -> digitsOnly
        }
    }
}

