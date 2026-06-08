package com.xongolab.hotellifyr.core

import android.content.Context
import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.xongolab.hotellifyr.R

abstract class CoreFragment : Fragment(), View.OnClickListener {

    var coreActivity: CoreActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        coreActivity = activity as CoreActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun makeLinks(textView: TextView,vararg links: Pair<String, View.OnClickListener>) {
        if (textView.text.isNotEmpty()) {

            val spannableString = SpannableString(textView.text)
            for (link in links) {
                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(view: View) {
                        Selection.setSelection((view as TextView).text as Spannable, 0)
                        view.invalidate()
                        link.second.onClick(view)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = true
                        ds.color = ContextCompat.getColor(coreActivity!!, R.color.colorPrimary)
                    }
                }
                val startIndexOfLink = textView.text.toString().indexOf(link.first)
                if (startIndexOfLink != -1) {
                    spannableString.setSpan(
                        clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

            }
            textView.movementMethod =
                LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
            textView.setText(spannableString, TextView.BufferType.SPANNABLE)
        }
    }

    fun isInternetConnected(): Boolean {
        return coreActivity!!.isInternetConnected()
    }
}