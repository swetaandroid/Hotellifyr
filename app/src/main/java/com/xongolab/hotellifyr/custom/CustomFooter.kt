package com.xongolab.hotellifyr.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.CustomFooterBinding

class CustomFooter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {

    var binding: CustomFooterBinding = CustomFooterBinding.bind(
        LayoutInflater.from(context).inflate(R.layout.custom_footer, this)
    )

    companion object {
        var selectedPosition: Int = 0
    }

    init {
        changeBackground(selectedPosition)
    }

    fun changeBackground(position: Int) {
        selectedPosition = position
    }

}