package com.stamp

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stamp.adapter.PassAdapter
import com.stamp.model.Pass
import com.stamp.viewModel.StampViewModel

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Pass>?) {
    val adapter = recyclerView.adapter as PassAdapter
    adapter.submitList(data)
}

@BindingAdapter("stampCount")
fun bindStampCount(view: TextView, data: Pass?) {
    data?.max_stamps?.apply {
        if (data.stamps != null)
            view.text = String.format(view.context.getString(R.string.stampsWithMax), data.stamps, this)
        else
            view.text = String.format(view.context.resources.getQuantityString(R.plurals.max_stamps, this), this)
    }
}

@BindingAdapter("commitMessage", "commitStatus")
fun bindCommitMessage(view: TextView, stamp: StampViewModel?, status: AdvancedViewModelStatus?) {
    stamp?.apply {
        val stringId = when (status) {
            AdvancedViewModelStatus.SUCCESS -> R.plurals.granted_stamps_with_details
            AdvancedViewModelStatus.ERROR -> R.plurals.granted_stamps_with_details_failed
            else -> R.plurals.granting_stamps_with_details
        }

        stamp.amount.value?.apply {
            view.text = String.format(view.context.resources.getQuantityText(stringId, this).toString(), this, stamp.pass.value?.name)
        }
    }
}

@BindingAdapter("statusIcon")
fun bindStatusIcon(view: ImageView, status: AdvancedViewModelStatus?) {
    status?.apply {
        when (this) {
            AdvancedViewModelStatus.SUCCESS -> {
                view.visibility = View.VISIBLE
                view.setImageResource(R.drawable.ic_done_black_24dp)
                view.setColorFilter(Color.parseColor("#4CAF50"))
            }

            AdvancedViewModelStatus.ERROR -> {
                view.visibility = View.VISIBLE
                view.setImageResource(R.drawable.ic_close_black_24dp)
                view.setColorFilter(Color.parseColor("#E91E63"))
            }

            AdvancedViewModelStatus.IDLE -> {
                view.visibility = View.VISIBLE
                view.setImageResource(R.drawable.ic_clock_black_24dp)
                view.setColorFilter(Color.parseColor("#444444"))
            }

            else -> {
                view.visibility = View.GONE
            }
        }
    }
}