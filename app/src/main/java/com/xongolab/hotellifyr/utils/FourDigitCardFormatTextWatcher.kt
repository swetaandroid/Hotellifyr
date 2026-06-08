package com.xongolab.hotellifyr.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log

class FourDigitCardFormatTextWatcher : TextWatcher {

    private var isFormatting: Boolean = false
    private var deleteChar: Boolean = false

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
        Log.d("TextWatcher", "afterTextChanged called")

        if (isFormatting || deleteChar) {
            deleteChar = false
            return
        }

        isFormatting = true

        val formatted = formatCardNumber(s.toString())
        s?.replace(0, s.length, formatted)

        isFormatting = false
    }

    private fun formatCardNumber(cardNumber: String): String {
        return cardNumber.replace("\\s".toRegex(), "")
            .chunked(4)
            .joinToString(" ")
    }
}
