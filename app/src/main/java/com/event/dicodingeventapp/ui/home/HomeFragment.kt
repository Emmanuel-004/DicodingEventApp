package com.event.dicodingeventapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.event.dicodingeventapp.EventAdapter
import com.event.dicodingeventapp.ViewModelFactory
import com.event.dicodingeventapp.data.Result
import com.event.dicodingeventapp.databinding.FragmentHomeBinding
import com.event.dicodingeventapp.ui.dashboard.DashboardViewModel
import com.event.dicodingeventapp.ui.notifications.NotificationsViewModel
import com.event.dicodingeventapp.ui.settings.SettingPreferences
import com.event.dicodingeventapp.ui.settings.dataStore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingEventAdapter: EventAdapter
    private lateinit var finishedEventAdapter: EventAdapter
    private lateinit var preferences: SettingPreferences

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = SettingPreferences.getInstance(requireContext().applicationContext.dataStore)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), preferences)
        val dashboardViewModel: DashboardViewModel by viewModels { factory }
        val notificationsViewModel: NotificationsViewModel by viewModels { factory }

        upcomingEventAdapter = EventAdapter{ event ->
            if (event.isBookmarked) {
                dashboardViewModel.deleteBookmark(event)
            } else {
                dashboardViewModel.saveBookmark(event)
            }
        }

        finishedEventAdapter = EventAdapter{ event ->
            if (event.isBookmarked) {
                notificationsViewModel.deleteBookmark(event)
            } else {
                notificationsViewModel.saveBookmark(event)
            }
        }


        binding.rvHomeUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHomeUpcoming.adapter = upcomingEventAdapter

        binding.rvHomeFinished.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHomeFinished.adapter = finishedEventAdapter

        dashboardViewModel.getUpcomingEvents().observe(viewLifecycleOwner){ result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        upcomingEventAdapter.submitList(result.data.filter { !it.isFinished })
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        notificationsViewModel.getFinishedEvents().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        finishedEventAdapter.submitList(result.data.filter {it.isFinished})
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