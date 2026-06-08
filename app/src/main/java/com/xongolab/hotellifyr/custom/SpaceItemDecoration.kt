package com.xongolab.hotellifyr.custom

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class SpaceItemDecoration(private val verticalSpace: Int, private val horizontalSpace: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        // Apply horizontal spacing
        outRect.left = horizontalSpace
        outRect.right = horizontalSpace

        // Apply vertical spacing
        outRect.bottom = verticalSpace

        // Add top space only to the first item
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = verticalSpace
        }
    }
}
