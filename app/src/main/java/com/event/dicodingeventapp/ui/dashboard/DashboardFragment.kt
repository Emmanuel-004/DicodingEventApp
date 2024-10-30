package com.event.dicodingeventapp.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.event.dicodingeventapp.data.Result
import com.event.dicodingeventapp.EventAdapter
import com.event.dicodingeventapp.ViewModelFactory
import com.event.dicodingeventapp.databinding.FragmentDashboardBinding
import com.event.dicodingeventapp.ui.settings.SettingPreferences
import com.event.dicodingeventapp.ui.settings.dataStore


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SettingPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = SettingPreferences.getInstance(requireContext().applicationContext.dataStore)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), preferences)
        val viewModel: DashboardViewModel by viewModels { factory }

        val eventAdapter = EventAdapter { event ->
            if (event.isBookmarked) {
                viewModel.deleteBookmark(event)
            } else {
                viewModel.saveBookmark(event)
            }
        }

        binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcoming.adapter = eventAdapter

        viewModel.getUpcomingEvents().observe(viewLifecycleOwner){ result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        eventAdapter.submitList(result.data.filter {!it.isFinished})
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}