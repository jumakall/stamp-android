package com.stamp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stamp.databinding.PassViewBinding
import com.stamp.model.Pass

class PassAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Pass, PassAdapter.PassViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassViewHolder {
        return PassViewHolder(
            PassViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: PassViewHolder, position: Int) {
        val pass = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(pass)
        }
        holder.bind(pass)
    }

    class PassViewHolder(private var binding: PassViewBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(pass: Pass) {
            binding.pass = pass
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Pass>() {
        override fun areItemsTheSame(oldItem: Pass, newItem: Pass): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Pass, newItem: Pass): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class OnClickListener(val clickListener: (pass:Pass) -> Unit) {
        fun onClick(pass:Pass) = clickListener(pass)
    }

}