package com.xongolab.hotellifyr.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xongolab.hotellifyr.databinding.BackConfirmationBottomSheetDialogBinding
import com.xongolab.hotellifyr.databinding.CustomBottomSheetDialogBinding
import com.xongolab.hotellifyr.databinding.DialogNonMemberRateBinding


class DialogManager(private val context: Context) {

    fun showCustomBottomSheetDialog(
        imageResId: Int,
        title: String,
        description: String,
        buttonText: String,
        showNotNow: Boolean,
        onButtonClick: (() -> Unit)? = null,
        onNotNowClick: (() -> Unit)? = null
    ) {
        val dialog = BottomSheetDialog(context)

        val dialogBinding: CustomBottomSheetDialogBinding =
            CustomBottomSheetDialogBinding.inflate(LayoutInflater.from(context))
        val view = dialogBinding.root

        dialogBinding.apply {
            image.setImageResource(imageResId)
            tvTitle.text = title
            tvDescription.text = description
            button.text = buttonText

            tvNotNow.visibility = if (showNotNow) View.VISIBLE else View.GONE

            ivClose.setOnClickListener {
                dialog.dismiss()
            }

            button.setOnClickListener {
                dialog.dismiss()
                onButtonClick?.invoke()
            }

            tvNotNow.setOnClickListener {
                dialog.dismiss()
                onNotNowClick?.invoke()
            }
        }

        dialog.behavior.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    fun showNonMemberBottomSheetDialog(
        onYesButtonClick: (() -> Unit)? = null,
        onNoButtonClick: (() -> Unit)? = null
    ) {
        val dialog = BottomSheetDialog(context)

        val dialogBinding: DialogNonMemberRateBinding =
            DialogNonMemberRateBinding.inflate(LayoutInflater.from(context))
        val view = dialogBinding.root

        dialogBinding.apply {

            ivClose.setOnClickListener {
                dialog.dismiss()
            }

            tvYes.setOnClickListener {
                dialog.dismiss()
                onYesButtonClick?.invoke()
            }

            tvConfirm.setOnClickListener {
                dialog.dismiss()
                onNoButtonClick?.invoke()
            }
        }

        dialog.behavior.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    fun showBackConfirmationBottomSheetDialog(
        onButtonClick: (() -> Unit)? = null,
        onCancelClick: (() -> Unit)? = null
    ) {
        val dialog = BottomSheetDialog(context)

        val dialogBinding: BackConfirmationBottomSheetDialogBinding =
            BackConfirmationBottomSheetDialogBinding.inflate(LayoutInflater.from(context))
        val view = dialogBinding.root

        dialogBinding.apply {

            ivClose.setOnClickListener {
                dialog.dismiss()
            }

            btnConfirm.setOnClickListener {
                dialog.dismiss()
                onButtonClick?.invoke()
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
                onCancelClick?.invoke()
            }
        }

        dialog.behavior.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }
}

