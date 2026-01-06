package com.aquareminder.app.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android:view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aquareminder.app.AquaReminderApp
import com.aquareminder.app.R
import com.aquareminder.app.databinding.FragmentStatisticsBinding
import com.aquareminder.app.model.DailyRecord
import com.aquareminder.app.ui.adapters.HistoryAdapter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var prefsManager: com.aquareminder.app.data.PreferencesManager
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val app = requireActivity().application as AquaReminderApp
        prefsManager = app.preferencesManager
        
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter()
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            prefsManager.getStatistics().collect { stats ->
                updateUI(stats)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            prefsManager.getTodayRecord().collect { todayRecord ->
                binding.tvTodayIntake.text = "${todayRecord.totalAmount} ml"
            }
        }
    }

    private fun updateUI(stats: com.aquareminder.app.model.Statistics) {
        // Update streak
        if (stats.currentStreak > 0) {
            binding.tvStreak.text = getString(R.string.day_streak, stats.currentStreak)
        } else {
            binding.tvStreak.text = getString(R.string.no_streak)
        }
        binding.tvBestStreak.text = "Best: ${stats.bestStreak} days"
        
        // Update average
        binding.tvAverageIntake.text = "${stats.averageDailyIntake} ml"
        
        // Update chart
        setupChart(stats.last7Days)
        
        // Update history
        historyAdapter.submitList(stats.last7Days)
    }

    private fun setupChart(records: List<DailyRecord>) {
        if (records.isEmpty()) return
        
        val barChart = BarChart(requireContext())
        binding.chartContainer.removeAllViews()
        binding.chartContainer.addView(barChart)
        
        val entries = records.mapIndexed { index, record ->
            BarEntry(index.toFloat(), record.totalAmount.toFloat())
        }
        
        val dataSet = BarDataSet(entries, "Water Intake (ml)").apply {
            color = Color.parseColor("#2196F3")
            valueTextColor = Color.BLACK
            valueTextSize = 10f
        }
        
        val barData = BarData(dataSet)
        barData.barWidth = 0.8f
        
        barChart.apply {
            data = barData
            description.isEnabled = false
            setDrawGridBackground(false)
            animateY(1000)
            
            // X-axis
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                labelCount = records.size
                valueFormatter = object : ValueFormatter() {
                    private val dateFormat = SimpleDateFormat("E", Locale.getDefault())
                    override fun getFormattedValue(value: Float): String {
                        return try {
                            val record = records.getOrNull(value.toInt())
                            if (record != null) {
                                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    .parse(record.date)
                                dateFormat.format(date ?: Date())
                            } else {
                                ""
                            }
                        } catch (e: Exception) {
                            ""
                        }
                    }
                }
            }
            
            // Left axis
            axisLeft.apply {
                setDrawGridLines(true)
                axisMinimum = 0f
            }
            
            // Right axis
            axisRight.isEnabled = false
            
            // Legend
            legend.isEnabled = false
            
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
