package com.aquareminder.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aquareminder.app.R
import com.aquareminder.app.databinding.ItemHistoryBinding
import com.aquareminder.app.model.DailyRecord
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter : ListAdapter<DailyRecord, HistoryAdapter.HistoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(
        private val binding: ItemHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: DailyRecord) {
            // Format date
            binding.tvDate.text = formatDate(record.date)
            
            // Show amount
            binding.tvAmount.text = "${record.totalAmount} ml / ${record.goal} ml"
            
            // Show status icon
            if (record.goalReached) {
                binding.ivStatus.setImageResource(R.drawable.ic_check)
            } else {
                binding.ivStatus.setImageResource(R.drawable.ic_close)
            }
        }

        private fun formatDate(dateString: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                val date = inputFormat.parse(dateString)
                
                // Check if today
                val today = Calendar.getInstance()
                val recordDate = Calendar.getInstance()
                recordDate.time = date ?: Date()
                
                when {
                    today.get(Calendar.YEAR) == recordDate.get(Calendar.YEAR) &&
                    today.get(Calendar.DAY_OF_YEAR) == recordDate.get(Calendar.DAY_OF_YEAR) -> {
                        "Today"
                    }
                    today.get(Calendar.YEAR) == recordDate.get(Calendar.YEAR) &&
                    today.get(Calendar.DAY_OF_YEAR) - 1 == recordDate.get(Calendar.DAY_OF_YEAR) -> {
                        "Yesterday"
                    }
                    else -> {
                        outputFormat.format(date ?: Date())
                    }
                }
            } catch (e: Exception) {
                dateString
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DailyRecord>() {
        override fun areItemsTheSame(oldItem: DailyRecord, newItem: DailyRecord): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: DailyRecord, newItem: DailyRecord): Boolean {
            return oldItem == newItem
        }
    }
}
